/*
 * Copyright (C)2005-2017 Haxe Foundation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package org.haxe.net;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class HaxeSerializer {

  /**
   If the values you are serializing can contain circular references or
   objects repetitions, you should set `USE_CACHE` to true to prevent
   infinite loops.
   This may also reduce the size of serialization Strings at the expense of
   performance.
   This value can be changed for individual instances of Serializer by
   setting their useCache field.
   **/
  public static final boolean USE_CACHE = false;

  /**
   Use constructor indexes for enums instead of names.
   This may reduce the size of serialization Strings, but makes them less
   suited for long-term storage: If constructors are removed or added from
   the enum, the indices may no longer match.
   This value can be changed for individual instances of Serializer by
   setting their useEnumIndex field.
   **/
  public static final boolean USE_ENUM_INDEX = false;

  private static final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789%:";
  private static final String BASE64_CODES = null;

  private StringBuffer buf;
  private int scount;
  /** Cashe of used strings with their position */
  private HashMap<String, Integer> stringCash;
  /** Cashe of used object with their position */
  private ArrayList<Object> objectCache;

  /**
   The individual cache setting for `this` Serializer instance.
   See USE_CACHE for a complete description.
   **/
  public boolean useCache;

  /**
   The individual enum index setting for `this` Serializer instance.
   See USE_ENUM_INDEX for a complete description.
   **/
  public boolean useEnumIndex;

  /**
   Creates a new Serializer instance.
   Subsequent calls to `this.serialize` will append values to the
   internal buffer of this String. Once complete, the contents can be
   retrieved through a call to `this.toString`.
   Each Serializer instance maintains its own cache if this.useCache` is
   true.
   **/
  public HaxeSerializer() {
    buf = new StringBuffer();
    objectCache = new ArrayList<Object>();
    useCache = USE_CACHE;
    useEnumIndex = USE_ENUM_INDEX;
    stringCash = new HashMap<String, Integer>();
    scount = 0;
  }

  /**
   Return the String representation of `this` Serializer.
   The exact format specification can be found here:
   https://haxe.org/manual/serialization/format
   **/
  public String toString() {
    return buf.toString();
  }

  public StringBuffer getStringBuffer() { return buf; }

	/* prefixes :
		a : array
		b : hash
		c : class
		d : Float
		e : reserved (float exp)
		f : false
		g : object end
		h : array/list/hash end
		i : Int
		j : enum (by index)
		k : NaN
		l : list
		m : -Inf
		n : null
		o : object
		p : +Inf
		q : haxe.ds.IntMap
		r : reference
		s : bytes (base64)
		t : true
		u : array nulls
		v : date
		w : enum
		x : exception
		y : urlencoded string
		z : zero
		A : Class<Dynamic>
		B : Enum<Dynamic>
		M : haxe.ds.ObjectMap
		C : custom
	*/

  private void serializeString(String value) throws Exception {

    if(stringCash.containsKey(value)) {
      buf.append('R');
      buf.append(stringCash.get(value));
      return;
    }

    stringCash.put(value, scount++);
    value = postProcessUrlEncode(java.net.URLEncoder.encode(value, "UTF-8"));
    buf.append("y");
    buf.append(value.length());
    buf.append(":");
    buf.append(value);
  }

  private boolean serializeRef(Object value) {
    for(int i = 0; i < objectCache.size(); i++) {
      if(objectCache.get(i) == value) {
        buf.append("r");
        buf.append(i);
        return true;
      }
    }
    objectCache.add(value);
    return false;
  }

    /**
     Serializes `v`.
     All haxe-defined values and objects with the exception of functions can
     be serialized. Serialization of external/native objects is not
     guaranteed to work.
     The values of `this.useCache` and `this.useEnumIndex` may affect
     serialization output.
     **/
  public void serialize(Object value) throws Exception {

    if (value instanceof Integer) {
      int intValue = (Integer)value;
      if (intValue == 0) {
        buf.append('z');
        return;
      }
      buf.append('i');
      buf.append(intValue);
      return;
    }
    if (value instanceof Float) {
      float floatValue = (Float)value;
      if (Float.isNaN(floatValue)) {
        buf.append('k');
      }
      else if (Float.isInfinite(floatValue)) {
        buf.append(floatValue < 0 ? 'm' : 'p');
      }
      else {
        buf.append('d');
        buf.append(floatValue);
      }
      return;
    }
    if (value instanceof Double) {
      double doubleValue = (Double)value;
      if (Double.isNaN(doubleValue)) {
        buf.append('k');
      }
      else if (Double.isInfinite(doubleValue)) {
        buf.append(doubleValue < 0 ? 'm' : 'p');
      }
      else {
        buf.append('d');
        buf.append(doubleValue);
      }
      return;
    }
    if (value instanceof Boolean) {
      buf.append((Boolean)value ? 't' : 'f');
      return;
    }
    if (value instanceof String) {
      serializeString((String)value);
      return;
    }
    if (value instanceof Calendar) {
      serializeDate((Calendar)value);
      return;
    }

    if (value instanceof List) {
      serializeList((List)value);
      return;
    }
    if (value instanceof Map) {
      serializeMap((Map)value);
      return;
    }

    if (value instanceof Object) {
      Class valueClass = value.getClass();
      if (valueClass.isArray()) {
        serializeArray(value);
        return;
      }
      else {
        serializeClass(value);
        return;
      }
    }
  }

  private void serializeDate(Calendar value) {
    int year = value.get(Calendar.YEAR);
    int month = value.get(Calendar.MONTH);
    int day = value.get(Calendar.DATE);
    int hours = value.get(Calendar.HOUR_OF_DAY);
    int minutes = value.get(Calendar.MINUTE);
    int seconds = value.get(Calendar.SECOND);
    String strYear = Integer.toString(year);
    String strMonth = twoDigitString(month + 1);
    String strDay = twoDigitString(day);
    String strHour = twoDigitString(hours);
    String strMin = twoDigitString(minutes);
    String strSec = twoDigitString(seconds);
    buf.append("v" + strYear + "-" + strMonth + "-" + strDay + " " + strHour + ":" + strMin + ":" + strSec);
  }

  private String twoDigitString(int value) {
    return value < 10 ? "0" + value : Integer.toString(value);
  }

  public void serializeArray(Object value) throws Exception  {
    buf.append('a');
    int length = Array.getLength(value);
    for (int i = 0; i < length; i++) {
      Object arrayElement = Array.get(value, i);
      serialize(arrayElement);
    }
    buf.append('h');
  }

  public void serializeList(List value) throws Exception  {
    buf.append('l');
    for (int i = 0; i < value.size(); i++) {
      Object arrayElement = value.get(i);
      serialize(arrayElement);
    }
    buf.append('h');
  }

  public void serializeMap(Map value) throws Exception  {
    Object[] keys = value.keySet().toArray();
    Object key = keys[0];
    if (key instanceof String) buf.append('b');
    else if (key instanceof Integer) buf.append('q');
    else if (key instanceof Objects) buf.append('M');

    for (int i = 0; i < keys.length; i++) {
      key = keys[i];
      if (key instanceof Integer) {
	      buf.append(':');
	      buf.append((Integer)key);
      } else {
        serialize(key);
      }
      serialize(value.get(key));
    }
    buf.append('h');
  }

  public void serializeObject(Object value) throws Exception  {
    buf.append('o');
    Class valueClass = value.getClass();
    Field[] fields = valueClass.getDeclaredFields();
    for (Field field : fields) {
      serializeString(field.getName());
      try {
        Object fieldValue = field.get(value);
        serialize(fieldValue);
      }
      catch (Exception e) {
        System.out.println(e);
      }
    }
    buf.append('g');
  }

  public void serializeClass(Object value) throws Exception  {
    buf.append('c');
    Class valueClass = value.getClass();
    String className = valueClass.getCanonicalName();
    serializeString(className);

    Field[] fields = valueClass.getDeclaredFields();
    for (Field field : fields) {
      serializeString(field.getName());
      try {
        Object fieldValue = field.get(value);
        serialize(fieldValue);
      }
      catch (Exception e) {
        System.out.println(e);
      }
    }
    buf.append('g');
  }

  public void serializeException(Object e) throws Exception  {
    buf.append('x');
    serialize(e);
  }

  private static String postProcessUrlEncode(String s) {
    StringBuffer result = new StringBuffer();
    int i = 0;
    int len = s.length();
    while (i < len) {
      char c = s.charAt(i++);
      if (c == '+') {
        result.append("%20");
      } else if (c == '%' && i <= len - 2) {
        char c1 = s.charAt(i++);
        char c2 = s.charAt(i++);
        if (c1 == '2' && c2 == '1') {
           result.append('!');
        } else if (c1 == '2' && c2 == '7') {
          result.append('\'');
        } else if (c1 == '2' && c2 == '8') {
          result.append('(');
        } else if (c1 == '2' && c2 == '9') {
          result.append(')');
        } else if ((c1 == '2' && c2 == 'E') || (c1 == '7' && c2 == 'e')){
          result.append('~');
        } else{
          result.append('%');
          result.append(c1);
          result.append(c2);
        }
      } else {
        result.append(c);
      }
    }
    return result.toString();
  }
}