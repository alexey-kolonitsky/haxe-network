package org.haxelib.core;

import java.util.regex.Pattern;

/**
 * Created by akalanitski on 24.08.2017.
 */
public class HaxelibProtocol {

	public static final String HAXELIB_NAME = "[A-Za-z][A-Za-z0-9_.,-]+";
	public static final String HAXELIB_VERSION = "[0-9].[0-9].[0-9]";

	public static final String LIBRARY_FILE_EXTENSION = "zip";
	public static final String PROTOCOL_VERSION = "3.0";

	public boolean isVersion(String str) {
		if (str == null || str.equals(""))
			return false;
		return Pattern.matches(HAXELIB_VERSION, str);
	}

	public boolean isLibraryName(String str) {
		if (str == null || str.equals(""))
			return false;
		return Pattern.matches(HAXELIB_NAME, str);
	}

	public String decodeFilename(String filename) {
		String result = filename.replace(",", ".");
		return result;
	}

	public String encodeFilename(String filename) {
		String result = filename.replace(".", ",");
		return result;
	}
	public String libraryArchiveName(String library, String version) {
		String result = library + "-" + encodeFilename(version) + "." + LIBRARY_FILE_EXTENSION;
		return result;
	}
}
