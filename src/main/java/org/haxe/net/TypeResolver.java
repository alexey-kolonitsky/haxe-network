package main.java;

/**
 * Created by akalanitski on 24.07.2017.
 */
public class TypeResolver {
	public Class<?> resolveClass(String name) throws ClassNotFoundException {
		return Class.forName(name);
	};
	public Class<Enum> resolveEnum(String name) throws ClassNotFoundException {
		return (Class<Enum>)Class.forName(name);
	};
}