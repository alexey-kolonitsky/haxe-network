/*
 * Copyright 2000-2013 JetBrains s.r.o.
 * Copyright 2014-2017 AS3Boyan
 * Copyright 2014-2014 Elias Ku
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.haxelib.model;

import org.haxelib.core.data.CurrentVersion;
import org.haxelib.core.data.HaxelibException;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Alexey Kolonitsky <alexey.s.kolonitsky@gmail.com> on 22.07.2017.
 */
public class HaxelibEntity extends HaxelibDependency {

	/**  */
	public static final String HAXELIB_TYPE_REMOTE = "remote";
	/** */
	public static final String HAXELIB_TYPE_DEVELOPMENT = "development";
	public static final String HAXELIB_TYPE_VCS = "vcs";


	//-----------------------------------
	// Haxelib description properies
	//-----------------------------------

	public String url;
	public String owner;
	public String license;
	public ArrayList<String> tags;
	public String description;
	public String version;
	public String lastVersion; //2.4
	public HaxelibVersion[] versions;
	public int downloads;

	//-----------------------------------
	// Haxelib local properties
	//-----------------------------------

	public CurrentVersion currentVersion; //2.4 | dev | hg | git

	/**
	 * Path to library home directory in haxe/lib
	 */
	public String homeDirectoryPath;


	/**
	 * File object represented library home directory
	 */
	public File homeDirectory;

	/**
	 * Installed field is true for libraries available on local machine
	 */
	public boolean installed = false;

	public String toString() {
		return name + " " + currentVersion + " ";
	}

	private ArrayList<HaxelibException> _errors = new ArrayList<>();

	public boolean hasErrors() {
		return _errors.size() > 0;
	}

	public void addError(HaxelibException exception) {
		_errors.add(exception);
	}
	public ArrayList<HaxelibException> getErrors() {
		return _errors;
	}

	public boolean hasVersion(String libraryVersion) {
		if (versions == null || versions.length == 0)
			return false;

		for (HaxelibVersion version : versions)
			if (version.name.equals(libraryVersion))
				return true;
		return false;
	}
}
