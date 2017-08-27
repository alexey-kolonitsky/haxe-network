package org.haxelib.configuration;

import org.haxelib.HaxelibConstants;
import org.haxelib.utils.KString;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibConfigurator {

	Properties _properties = new Properties();

	public void set(String key, String value) {
		if (_properties == null || KString.isEmpty(key))
		_properties.setProperty(key, value);
	}

	public String get(String key) {
		if (_properties != null && KString.isNotEmpty(key))
			return _properties.get(key).toString();
		return null;
	}

	public void readConfig() throws Exception{

		String workingDir = System.getProperty("user.dir");
		System.out.println("Current working directory : " + workingDir);

		InputStream input = null;
		try {
			input = getClass().getClassLoader().getResourceAsStream(HaxelibConstants.APPLICATION_CONFIG_FILE);
			_properties.load(input);
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void writeProperties() {
		OutputStream output = null;

		try {
			output = new FileOutputStream(HaxelibConstants.APPLICATION_CONFIG_FILE);

			// set the properties value
			_properties.setProperty("database", "localhost");
			_properties.setProperty("dbuser", "mkyong");
			_properties.setProperty("dbpassword", "password");

			// save properties to project root folder
			_properties.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
