[![Build Status](https://travis-ci.org/alexey-kolonitsky/haxe-network.svg?branch=master)](https://travis-ci.org/alexey-kolonitsky/haxe-network)
[![codecov](https://codecov.io/gh/alexey-kolonitsky/haxe-network/branch/master/graph/badge.svg)](https://codecov.io/gh/alexey-kolonitsky/haxe-network)

Haxe has really amazing data serialization/deserialization 
mechanism. This library provide the most common abilities to use Java with Haxe 
front-end or backend. This is mostly copy-paste from Haxe Standard Library with
corrections to use it without the rest of library.

Haxe Messaging Format
---------------------
Full definition of Haxe Messaging Format available in the 
[documentation](https://haxe.org/manual/std-serialization-format.html). Threre 
is full list of types supported for serialization/deserialization with 
haxe-networking.

| **Primitive** | **Complex**  | **Special Values** |
|---------------|--------------|--------------------|
| int           | String       | 0                  |
| float         | Date         | NaN                |
| boolean       | Array        | null               |
|               | List         | +Infinity          |
|               | Map*         | -Infinity          |
|               | Objects      |                    |
|               | Exceptions   |                    |

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

