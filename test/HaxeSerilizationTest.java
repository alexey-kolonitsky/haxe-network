import org.haxe.net.HaxeDeserializer;
import org.haxe.net.HaxeSerializer;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
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
}
