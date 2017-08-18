package org.haxe.net;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by akalanitski on 23.07.2017.
 */
public class HaxeDeserializer {

	public static TypeResolver DEFAULT_RESOLVER  = new TypeResolver();

	private static String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789%:";

	private StringBuffer buf;
	private int pos;
	private int length;
	private ArrayList<Object> cache;
	private ArrayList<String> scache;
	private TypeResolver resolver;

	public HaxeDeserializer(StringBuffer buf) {
		this.buf = buf;
		length = buf.length();
		pos = 0;
		scache = new ArrayList<String>();
		cache = new ArrayList<Object>();
		resolver = DEFAULT_RESOLVER;
	}

	public void setResolver(TypeResolver value ) {
		resolver = value;
	}

	public TypeResolver getResolver() {
		return resolver;
	}

	private char get(int position) {
		if (position < 0 || position > length)
			return 0;
		return buf.charAt(position);
	}
	private boolean isEof(int pos) {
		return pos >= length;
	}

	private int readInteger() {
		int k = 0;
		boolean isNegative = false;
		int fpos = pos;
		while(true) {
			if(isEof(pos))
				break;
			char c = get(pos);
			if(c == '-') {
				if(pos != fpos)
					break;
				isNegative = true;
				pos++;
				continue;
			}
			if( c < '0' || c > '9') {
				break;
			}
			k = k * 10 + (c - '0');
			pos++;
		}
		if(isNegative)
			k *= -1;
		return k;
	}

	private float readFloat() {
		int startPosition = pos;
		while( true ) {
			if(isEof(pos))
				break;
			char c = get(pos);
			// + - . , 0-9
			if( (c >= 43 && c < 58) || c == 'e' || c == 'E')
				pos++;
			else
				break;
		}
		String sub = buf.substring(startPosition, pos);
		float result = Float.parseFloat(sub);
		return result;
	}

	private void unserializeObject(Object o) throws Exception {
		Class<?> classDefinition = o.getClass();
		while( true ) {
			if(isEof(pos)) {
				throw new Exception("Invalid object");
			}
			if( get(pos) == 'g')
				break;
			pos++;
			String fieldName  = readString();
			if( !(fieldName instanceof String) )
				throw new Exception("Invalid object key");
			Object fieldValue = deserialize();

			Field field = classDefinition.getField(fieldName);
			field.set(o, fieldValue);
		}
		pos++;
	}

	private Object unserializeEnum(Class<Enum> edecl, String constructorName) throws Exception {
		throw new Exception("Not implemented");
//		if( get(pos++) != ':')
//			throw new Exception("Invalid enum format");
//		int nargs = readInteger();
//		if( nargs == 0 )
//			return Type.createEnum(edecl,constructorName);
//		ArrayList<Object> args = new ArrayList<Object>();
//		while( nargs-- > 0 )
//			args.add(deserialize());
//		return Type.createEnum(edecl,constructorName,args);
	}

	private String readString() throws Exception {
		int stringLength = readInteger();
		if( get(pos++) != ':')
			throw new Exception(buf.toString() + "\nDeserilization exception. Position " + pos + " ':' expected");
		if (length - pos < stringLength)
			throw new Exception(buf.toString() + "\nDeserilization exception. Position " + pos + " String longer then input");
		String result = buf.substring(pos, pos + stringLength);
		pos += stringLength;
		result = java.net.URLDecoder.decode(result, "UTF-8");
		scache.add(result);
		return result;
	}

	private Calendar readDate() throws ParseException {
		Date d;
		if(get(pos) >= '0' && get(pos) <= '9' &&
				get(pos + 1) >= '0' && get(pos + 1) <= '9' &&
				get(pos + 2) >= '0' && get(pos + 2) <= '9' &&
				get(pos + 3) >= '0' && get(pos + 3) <= '9' &&
				get(pos + 4) == '-'
				) {

			String dateSubstring = buf.substring(pos, 19);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// Included for backwards compatibility
			d = format.parse(dateSubstring);
			pos += 19;
		} else
			d = new Date(readInteger());
		Calendar result = Calendar.getInstance();
		result.setTime(d);
		cache.add(result);
		return result;
	}

	public Object deserialize() throws Exception {
		switch( get(pos++) ) {
			case 'n':
				return null;
			case 't':
				return true;
			case 'f':
				return false;
			case 'z':
				return 0;
			case 'i':
				return readInteger();
			case 'd':
				return readFloat();
			case 'y':
				return readString();
			case 'k':
				return Float.NaN;
			case 'm':
				return Float.NEGATIVE_INFINITY;
			case 'p':
				return Float.POSITIVE_INFINITY;
			case 'a':
				return readArray();
			case 'o':
				HashMap o = new HashMap<String, Object>();
				cache.add(o);
				while( true ) {
					if(isEof(pos)) {
						throw new Exception("\nDeserilization exception. Unexpected EoL");
					}
					char marker = get(pos);
					if( marker == 'g') {
						pos++;
						break;
					}
					String fieldName = null;
					if (marker == 'y') {
						pos++;
						fieldName = readString();
					} else if (marker == 'R') {
						pos++;
						fieldName = readStringCashReference();
					}
					if (fieldName == null)
						throw new Exception("\n" + buf.toString() + "\nDeserilization exception. Position " + pos + ". Object field is NULL. Marker '" + marker + " has been found.");
					Object fieldValue = deserialize();
					o.put(fieldName, fieldValue);
				}
				return o;
			case 'r':
				return readObjectCashReference();
			case 'R':
				return readStringCashReference();
			case 'x':
				throw new Exception(readString());
			case 'c':
				return readClass();
			case 'w':
				return readNameEnum();
			case 'j':
				return readIndexEnum();
			case 'l':
				return readList();
			case 'b':
				HashMap<String, Object> stringHash = new HashMap<String, Object>();
				cache.add(stringHash);
				while( get(pos) != 'h') {
					String stringKey = (String) deserialize();
					stringHash.put(stringKey, deserialize());
				}
				pos++;
				return stringHash;
			case 'q':
				HashMap<Integer, Object> intHash = new HashMap<Integer, Object>();
				cache.add(intHash);
				char c = get(pos++);
				while(c == ':') {
					int i = readInteger();
					intHash.put(i, deserialize());
					c = get(pos++);
				}
				if(c != 'h')
					throw new Exception("Invalid IntMap format");
				return intHash;
			case 'M':
				return readObjectMap();
			case 'v':
				return readDate();
			case 's':
				return readBytes();
			case 'C':
				return readC();
			case 'A':
				return readA();
			case 'B':
				return readB();
			default:
		}
		pos--;
		throw new Exception("Invalid char "+buf.charAt(pos)+" at position "+pos);
	}

	private Object readObjectCashReference() throws Exception {
		int n = readInteger();
		if( n < 0 || n >= cache.size() )
			throw new Exception("Invalid reference");
		return cache.get(n);
	}

	private String readStringCashReference() throws Exception {
		int n = readInteger();
		if( n < 0 || n >= scache.size() )
			throw new Exception("Invalid string reference");
		return scache.get(n);
	}

	private byte[] readBytes() throws Exception {
		int stringLength = readInteger();
		if( get(pos++) != ':' || length - pos < stringLength )
			throw new Exception("Invalid bytes length");
		int i = pos;
		int rest = stringLength & 3;
		int size = (stringLength >> 2) * 3 + ((rest >= 2) ? rest - 1 : 0);
		int max = i + (stringLength - rest);
		byte[] bytes = new byte[size];
		int bpos = 0;
		while( i < max ) {
			int c1 = BASE64.indexOf(buf.charAt(i++));
			int c2 = BASE64.indexOf(buf.charAt(i++));
			bytes[bpos++] = (byte) ((c1 << 2) | (c2 >> 4));
			int c3 = BASE64.indexOf(buf.charAt(i++));
			bytes[bpos++] = (byte) ((c2 << 4) | (c3 >> 2));
			int c4 = BASE64.indexOf(buf.charAt(i++));
			bytes[bpos++] = (byte) ((c3 << 6) | c4);
		}
		if( rest >= 2 ) {
			int c1 = BASE64.indexOf(buf.charAt(i++));
			int c2 = BASE64.indexOf(buf.charAt(i++));
			bytes[bpos++] = (byte) ((c1 << 2) | (c2 >> 4));
			if( rest == 3 ) {
				int c3 = BASE64.indexOf(buf.charAt(i++));
				bytes[bpos++] = (byte) ((c2 << 4) | (c3 >> 2));
			}
		}
		pos += stringLength;
		cache.add(bytes);
		return bytes;
	}

	private Object readIndexEnum()throws Exception {
		throw new Exception("Not implemented");
//		String name = readString();
//		Class<Enum> edecl = resolver.resolveEnum(name);
//		if( edecl == null )
//			throw new Exception("Enum not found " + name);
//		pos++; /* skip ':' */
//		int index = readInteger();
//		String tag = Type.getEnumConstructs(edecl)[index];
//		if( tag == null )
//			throw new Exception("Unknown enum index "+name+"@"+index);
//		Object e = unserializeEnum(edecl, tag);
//		cache.add(e);
//		return e;
	}

	private Object readNameEnum() throws Exception {
		String name = readString();
		Class<Enum> edecl = resolver.resolveEnum(name);
		if( edecl == null )
			throw new Exception("Enum not found " + name);
		Object e = unserializeEnum(edecl, readString());
		cache.add(e);
		return e;
	}

	private Object readC() throws Exception {
		throw new Exception("Not implemented");
//		String name = readString();
//		Class<?> cl = resolver.resolveClass(name);
//		if( cl == null )
//			throw new Exception("Class not found " + name);
//		Object o = Type.createEmptyInstance(cl);
//		cache.add(o);
//		if( get(pos++) != 'g')
//			throw new Exception("Invalid custom data");
//		return o;
	}

	private Class<?> readA() throws Exception {
		String name = readString();
		Class cl = resolver.resolveClass(name);
		if( cl == null )
			throw new Exception("Class not found " + name);
		return cl;
	}

	private Class<?> readB() throws Exception {
		String name = readString();
		Class<Enum> e = resolver.resolveEnum(name);
		if( e == null )
			throw new Exception("Enum not found " + name);
		return e;
	}

	private Object readClass() throws Exception{
		String className = readString();
		Class<?> cl = resolver.resolveClass(className);
		if( cl == null ) {
			throw new Exception("Class not found " + className);
		}
		Constructor<?> ctor = cl.getConstructor(String.class);
		Object o = ctor.newInstance();
		cache.add(o);
		unserializeObject(o);
		return o;
	}

	private HashMap<Object, Object> readObjectMap() throws Exception {
		HashMap<Object, Object> objectMap = new HashMap<Object, Object>();
		cache.add(objectMap);
		while(get(pos) != 'h') {
			Object objectKey = deserialize();
			objectMap.put(objectKey, deserialize());
		}
		pos++;
		return objectMap;
	}

	private ArrayList<Object> readList() throws Exception {
		ArrayList<Object> l = new ArrayList<Object>();
		cache.add(l);
		while( get(pos) != 'h')
			l.add(deserialize());
		pos++;
		return l;
	}

	private Object readArray() throws Exception {
		ArrayList<Object> arrayList = new ArrayList<Object>();
		Object firstNotNullElement = null;
		cache.add(arrayList);
		while( true ) {
			char c = get(pos);
			if( c == 'h') {
				pos++;
				break;
			}
			if( c == 'u') {
				pos++;
				int n = readInteger();
				arrayList.set(arrayList.size()+n-1, null);
			} else {
				if (firstNotNullElement == null) {
					firstNotNullElement = deserialize();
					arrayList.add(firstNotNullElement);
				} else {
					arrayList.add(deserialize());
				}
			}
		}
		int n = arrayList.size();
		if (n > 0 && firstNotNullElement != null) {
			Class<?> c = firstNotNullElement.getClass();
			if (c.equals(Integer.class)) {
				c = int.class;
			} else if (c.equals(Float.class)) {
				c = float.class;
			}
			Object result = Array.newInstance(c, n);
			for (int i = 0; i < arrayList.size(); i++) {
				Array.set(result, i, arrayList.get(i));
			}
			return result;
		}

		return arrayList.toArray();
	}

}