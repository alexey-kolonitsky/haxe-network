# haxe-network
implementation of Haxe serialization protocol for java. Currently it support 
serialization of primitive types and common classes supported both java and haxe i.e.
 * int
 * float
 * boolean
 * strings
 * date
 * arrays and ArrayList
 * Objects

and a few special values
 * null
 * NaN
 * positive and negative infinity

How to use
----------

```java
import org.haxe.net.HaxeDeserializer;
import org.haxe.net.HaxeSerializer;

public class Main {

    public static void main(String[] args) throws Exception {
        HaxeSerializer serializer = new HaxeSerializer();
        serializer.serialize("foo");
        serializer.serialize(12);
        String s = serializer.toString();
        System.out.println(s); // y3:fooi12

        HaxeDeserializer deserializer = new HaxeDeserializer(new StringBuffer(s));
        System.out.println(deserializer.deserialize()); // foo
        System.out.println(deserializer.deserialize()); // 12
    }
}
```

