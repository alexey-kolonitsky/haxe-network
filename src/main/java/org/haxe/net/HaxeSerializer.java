package main.java;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class HaxeSerializer {

	private StringBuffer buf;
	private int scount;

	private HashMap<String, Integer> stringCash;

	private ArrayList<Object> objectCache;

	public HaxeSerializer() {
		buf = new StringBuffer();
		objectCache = new ArrayList<Object>();
		stringCash = new HashMap<String, Integer>();
		scount = 0;
	}

	public String toString() {
		return buf.toString();
	}

	public StringBuffer getStringBuffer() {
		return buf;
	}

	/**
	 **/
	public void serialize(Object value) throws Exception {

		if (value instanceof Integer) {
			int intValue = (Integer) value;
			if (intValue == 0) {
				buf.append('z');
				return;
			}
			buf.append('i');
			buf.append(intValue);
			return;
		}
		if (value instanceof Float) {
			float floatValue = (Float) value;
			if (Float.isNaN(floatValue)) {
				buf.append('k');
			} else if (Float.isInfinite(floatValue)) {
				buf.append(floatValue < 0 ? 'm' : 'p');
			} else {
				buf.append('d');
				buf.append(floatValue);
			}
			return;
		}
		if (value instanceof Double) {
			double doubleValue = (Double) value;
			if (Double.isNaN(doubleValue)) {
				buf.append('k');
			} else if (Double.isInfinite(doubleValue)) {
				buf.append(doubleValue < 0 ? 'm' : 'p');
			} else {
				buf.append('d');
				buf.append(doubleValue);
			}
			return;
		}
		if (value instanceof Boolean) {
			buf.append((Boolean) value ? 't' : 'f');
			return;
		}
		if (value instanceof String) {
			serializeString((String) value);
			return;
		}
		if (value instanceof Calendar) {
			serializeDate((Calendar) value);
			return;
		}

		if (value instanceof List) {
			serializeList((List) value);
			return;
		}
		if (value instanceof Map) {
			serializeMap((Map) value);
			return;
		}
		if (value instanceof Enum) {
			serializeEnum((Enum) value);
			return;
		}
		if (value instanceof Exception) {
			serializeException((Exception)value);
			return;
		}

		if (value instanceof Object) {
			Class valueClass = value.getClass();
			if (valueClass.isArray()) {
				serializeArray(value);
				return;
			} else {
				serializeClass(value);
				return;
			}
		}
	}

	private void serializeString(String value) throws Exception {

		if (stringCash.containsKey(value)) {
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
		for (int i = 0; i < objectCache.size(); i++) {
			if (objectCache.get(i) == value) {
				buf.append("r");
				buf.append(i);
				return true;
			}
		}
		objectCache.add(value);
		return false;
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

	private void serializeArray(Object value) throws Exception {
		buf.append('a');
		int length = Array.getLength(value);
		for (int i = 0; i < length; i++) {
			Object arrayElement = Array.get(value, i);
			serialize(arrayElement);
		}
		buf.append('h');
	}

	private void serializeList(List value) throws Exception {
		buf.append('l');
		for (int i = 0; i < value.size(); i++) {
			Object arrayElement = value.get(i);
			serialize(arrayElement);
		}
		buf.append('h');
	}

	private void serializeMap(Map value) throws Exception {
		Object[] keys = value.keySet().toArray();
		Object key = keys[0];
		if (key instanceof String) {
			buf.append('b');
		} else if (key instanceof Integer) {
			buf.append('q');
		} else { // if (key instanceof Objects)
			buf.append('M');
		}

		for (int i = 0; i < keys.length; i++) {
			key = keys[i];
			if (key instanceof Integer) {
				buf.append(':');
				buf.append((Integer) key);
			} else {
				serialize(key);
			}
			serialize(value.get(key));
		}
		buf.append('h');
	}

	private void serializeEnum(Enum value) throws Exception {
		throw new Exception("Serialization of enums is not supported");
	}

	private void serializeObject(Object value) throws Exception {
		buf.append('o');
		Class valueClass = value.getClass();
		Field[] fields = valueClass.getDeclaredFields();
		for (Field field : fields) {
			serializeString(field.getName());
			try {
				Object fieldValue = field.get(value);
				serialize(fieldValue);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		buf.append('g');
	}

	private void serializeClass(Object value) throws Exception {
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
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		buf.append('g');
	}

	private void serializeException(Exception e) throws Exception {
		buf.append('x');
		serializeString(e.getMessage());
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
				} else if ((c1 == '2' && c2 == 'E') || (c1 == '7' && c2 == 'e')) {
					result.append('~');
				} else {
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