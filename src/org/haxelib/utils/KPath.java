package org.haxelib.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * KPath represent system path as chain of directories separated
 * @author Alexey Kolonitsky &lt;alexey.s.kolonitsky@gmail.com>
 * @date 20.08.2017.
 */
public class KPath {
	private static final String WINDOWS_PATH_DELIMITER = "\\";
	private static final String LINUX_PATH_DELIMITER = "/";
	private static final boolean CASE_SENSETIVE = true;

	private ArrayList<String> _path;

	public KPath(String... path) {
		_path = new ArrayList<String>();
		append(path);
	}

	public void append(String... path) {
		for (String subpath : path) {
			if (KString.isEmpty(subpath))
				continue;
			String[] directories = subpath.split("[\\/]");
			for (String directory : directories) {
				if (KString.isNotEmpty(directory))
					_path.add(directory);
			}
		}
	}

	@Override
	public String toString() {
		return String.join(WINDOWS_PATH_DELIMITER, _path);
	}

	/**
	 * @return TRUE if last symbol is directory delimiter otherwise FALSE
	 */
	public boolean isDirectory() {
		String lastElement = last();
		String lastChar = lastElement.substring(lastElement.length() - 1);
		boolean result = (lastChar.equals(WINDOWS_PATH_DELIMITER) || lastChar.equals(LINUX_PATH_DELIMITER));
		return result;
	}

	public boolean isFile() {
		return !isDirectory();
	}

	/**
	 * @return path without filename this is the same path without last element.
	 */
	public String getPath() {
		if (_path == null || _path.size() == 0)
			return null;
		int lastIndex = _path.size() -1;
		List<String> subpath = _path.subList(0, lastIndex);
		return String.join(WINDOWS_PATH_DELIMITER, subpath);
	}

	/**
	 * @return file name it is just last element in the path.
	 */
	public String last() {
		if (_path == null || _path.size() == 0)
			return null;
		int lastIndex = _path.size() -1;
		String result = _path.get(lastIndex);
		return result;
	}
}
