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
package org.haxelib.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by akalanitski on 23.07.2017.
 */
public class KFile {

	public static final Charset CHARSET = StandardCharsets.UTF_8;

	private String _path;
	private File _file;

	public KFile(String path) {
		_path = path;
		_file = new File(path);
	}

	public boolean exists() {
		return _file != null && _file.exists();
	}

	public void delete() {
		if (_file.isDirectory())
			deleteRecuriveDirectory(_file);
		else
			_file.delete();
	}

	public String getContent() throws IOException {
		if (exists()) {
			return new String(Files.readAllBytes(Paths.get(_path)), CHARSET);
		}
		return null;
	}

	public void setContent(String content) throws IOException {
		if (_file != null && !_file.exists()) {
			_file.createNewFile();
		}
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(_file), CHARSET);
		BufferedWriter buf = new BufferedWriter(out);
		buf.write(content);
		buf.close();
	}

	public InputStream getContentStream() {
		InputStream result = null;
		try {
			result = new FileInputStream(_path);
		} catch (IOException exception) {}
		return result;
	}

	private void deleteRecuriveDirectory(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteRecuriveDirectory(f);
			}
		}
		file.delete();
	}

}
