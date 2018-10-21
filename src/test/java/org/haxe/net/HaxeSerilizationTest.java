package org.haxe.net;

import org.hamcrest.CoreMatchers;
import org.haxe.net.HaxeDeserializer;
import org.haxe.net.HaxeSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by akalanitski on 24.07.2017.
 */
public class HaxeSerilizationTest {

	@Test
	public void serializeInteger() throws Exception {
		int originalValue = 32;
		HaxeSerializer serializer = new HaxeSerializer();
        serializer.serialize(originalValue);
		assertEquals(serializer.toString(), "i32");

		HaxeDeserializer deserializer = new HaxeDeserializer(serializer.getStringBuffer());
		int value = (Integer)deserializer.deserialize();
		assertEquals(originalValue, value);
	}

	@Test
	public void serializeZero() throws Exception {
		int originalValue = 0;
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(originalValue);
		assertEquals(serializer.toString(), "z");

		HaxeDeserializer deserializer = new HaxeDeserializer(serializer.getStringBuffer());
		int value = (int)deserializer.deserialize();
		assertEquals(originalValue, value);
	}

	@Test
	public void serializeFloat() throws Exception {
		float originalValue = 14.1f;
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(originalValue);
		assertEquals("d14.1", serializer.toString());

		HaxeDeserializer deserializer = new HaxeDeserializer(serializer.getStringBuffer());
		float value = (float)deserializer.deserialize();
		assertEquals(originalValue, value);
	}

	@Test
	public void serializeFloatInfinity() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(Float.POSITIVE_INFINITY);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		float value = (Float)deserializer.deserialize();
		assertEquals(Float.POSITIVE_INFINITY, value);
	}

	@Test
	public void serializeFloatNegativeInfinity() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(Float.NEGATIVE_INFINITY);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		float value = (Float)deserializer.deserialize();
		assertEquals(Float.NEGATIVE_INFINITY, value);
	}

	@Test
	public void serializeNaN() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(Float.NaN);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		float value = (Float)deserializer.deserialize();
		assertTrue(Float.isNaN(value));
	}

	@Test
	public void serializeBooleanTrue() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(true);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		boolean value = (Boolean)deserializer.deserialize();
		assertEquals(true, value);
	}

	@Test
	public void serializeBooleanFalse() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(false);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		boolean value = (Boolean)deserializer.deserialize();
		assertEquals(false, value);
	}

	@Test
	public void serializeString() throws Exception {
		String originalValue = "Hello world!";
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(originalValue);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		String value = (String)deserializer.deserialize();
		assertEquals(originalValue, value);
	}

	@Test
	public void serializeArray() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		int[] originalValue = new int[]{10, 11, 12};
		serializer.serialize(originalValue);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		int[] value = (int[])deserializer.deserialize();
		assertArrayEquals(originalValue, value);
	}

	@Test
	public void serializeDate() throws Exception {
		Calendar originalValue = new GregorianCalendar(2004, 10, 5, 23, 45);
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(originalValue);
		assertEquals("v2004-11-05 23:45:00", serializer.toString());

		StringBuffer s = serializer.getStringBuffer();
		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		Calendar value = (Calendar)deserializer.deserialize();
		assertEquals(originalValue.getTimeInMillis(), value.getTimeInMillis());
	}

	@Test
	public void testList() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		ArrayList<Integer> originalValue = new ArrayList<Integer>();
		originalValue.add(10);
		originalValue.add(11);
		originalValue.add(12);
		serializer.serialize(originalValue);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		ArrayList<Integer> value = (ArrayList<Integer>)deserializer.deserialize();
	}

	@Test
	public void testStringMap() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		HashMap<String, Integer> originalValue = new HashMap<String, Integer>();
		originalValue.put("first", 10);
		originalValue.put("second", 11);
		originalValue.put("third", 12);
		serializer.serialize(originalValue);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		HashMap<String, Integer> value = (HashMap<String, Integer>)deserializer.deserialize();
		Assert.assertTrue(value.containsKey("third"));
	}

	@Test
	public void testIntMap() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		HashMap<Integer, Integer> originalValue = new HashMap<Integer, Integer>();
		originalValue.put(45, 10);
		originalValue.put(13, 11);
		originalValue.put(89, 12);
		serializer.serialize(originalValue);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		HashMap<Integer, Integer> value = (HashMap<Integer, Integer>)deserializer.deserialize();
		Assert.assertTrue(value.containsKey(89));
	}

	@Test
	public void testObjectMap() throws Exception {
		HaxeSerializer serializer = new HaxeSerializer();
		ValueClass first = new ValueClass();
		first.value = "first";
		ValueClass second = new ValueClass();
		second.value = "second";
		ValueClass third = new ValueClass();
		third.value = "third";

		HashMap<Object, Integer> originalValue = new HashMap<Object, Integer>();
		originalValue.put(first, 10);
		originalValue.put(second, 11);
		originalValue.put(third, 12);
		serializer.serialize(originalValue);
		StringBuffer s = serializer.getStringBuffer();

		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		HashMap<Object, Integer> value = (HashMap<Object, Integer>)deserializer.deserialize();
		for (Object vKey : value.keySet()) {
			int oValue = originalValue.get(vKey);
			int vValue = value.get(vKey);
			Assert.assertTrue(oValue == vValue);
		}
	}

	@Test()
	public void testException() throws Exception {
		Exception originalValue = new Exception("My Exception");
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(originalValue);

		StringBuffer s = serializer.getStringBuffer();
		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		try {
			deserializer.deserialize();
		}catch (Exception e) {
			assertTrue(e.getMessage().equals(originalValue.getMessage()));
		}
	}

	@Test
	public void testClass() throws Exception {
		ValueClass originalValue = new ValueClass();
		originalValue.value = "Hello World!";
		HaxeSerializer serializer = new HaxeSerializer();
		serializer.serialize(originalValue);

		StringBuffer s = serializer.getStringBuffer();
		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		ValueClass value = (ValueClass)deserializer.deserialize();
		assertEquals(originalValue.value, value.value);
	}

	@Test
	public void deserializeData() throws Exception {
		StringBuffer s = new StringBuffer("ay7:alexey2y32:5cbcc03c43036744a2cc7dfb391988acy24:alexey1%40kolonitsky.comy20:Alexey1%20Kolonitskyh");
		HaxeDeserializer deserializer = new HaxeDeserializer(s);
		Object value = deserializer.deserialize();
		assertEquals(1, 1);
	}
}

