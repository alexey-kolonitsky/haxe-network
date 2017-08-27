package org.haxelib.command.info;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.data.HaxelibException;
import org.haxelib.model.HaxelibEntity;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibPathCommand extends HaxelibBaseCommand implements ICommand {

	//Injected
	public HaxelibRepository core;

	public HaxelibPathCommand() {
		_category = HaxelibCategories.INFO;
		_description = "give paths to libraries";
		_name = HaxelibCommands.HAXELIB_PATH;
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {
		String libraryName = arguments[0];
		String result = path(libraryName);
		println(result, null);

	}

	public String path(String libraryName) throws HaxelibException {
		HaxelibEntity result = core.get(libraryName);
		String text = result.currentVersion.path
			+ "\n-D " + result.name + "=" + result.currentVersion.version
			+ "\n";
		return text;
	}
}
