package org.haxelib.utils;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class KString {
	public static boolean isNotEmpty(String str) {
		return str != null && str.length() > 0;
	}
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
}
