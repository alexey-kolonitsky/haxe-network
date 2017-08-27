package org.haxelib.core.data;

import java.util.ArrayList;

/**
 * Created by akalanitski on 25.08.2017.
 */
public class LibraryDefinition {
	/** URL to library website or homepage */
	public String url;

	/** User name of library creator. */
	public String owner;

	public String license;

	public ArrayList<String> tags;
	public String description;
	public String classPath;
	public String[] contributors;
	public int downloads;

	public String version;
	public String versionComment;
	public String versionDate;
}
