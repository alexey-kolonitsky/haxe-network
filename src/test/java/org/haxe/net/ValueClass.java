package org.haxe.net;

public class ValueClass {
	public String value;

	public ValueClass() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ValueClass that = (ValueClass) o;

		return value.equals(that.value);

	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
