package org.haxelib.core;

import java.text.MessageFormat;

/**
 * Created by akalanitski on 25.08.2017.
 */
public class HaxelibException extends Exception {
	public static final String LIBRARY_NOT_INSTALLED = "Library ''{0}'' not installed";
	public static final String LIBRARY_NAME_INVALID = "Invalid library name ''{0}''";
	public static final String VERSION_NOT_INSTALLED = "Library ''{0}'' version ''{1}'' not installed";
	public static final String VERSION_INVALID = "Current version of library ''{0}'' is invalid: ''{1}''";
	public static final String VERSION_CONFLICT = "Library ''{0}'' has development and remote versions. Remote version will be ignored.";

	public HaxelibException(String message) {
		super(message);
	}

	public static HaxelibException LibraryNotInstalled(String libraryName) {
		return new HaxelibException(MessageFormat.format(LIBRARY_NOT_INSTALLED, libraryName));
	}
	public static HaxelibException InvalidLibraryName(String libraryName) {
		return new HaxelibException(MessageFormat.format(LIBRARY_NAME_INVALID, libraryName));
	}

	public static HaxelibException VersionNotInstalled(String libraryName, String version) {
		return new HaxelibException(MessageFormat.format(VERSION_NOT_INSTALLED, libraryName, version));
	}
	public static HaxelibException InvalidVersion(String libraryName, String version) {
		return new HaxelibException(MessageFormat.format(VERSION_INVALID, libraryName, version));
	}
	public static HaxelibException VersionConflict(String libraryName) {
		return new HaxelibException(MessageFormat.format(VERSION_CONFLICT, libraryName));
	}

}
