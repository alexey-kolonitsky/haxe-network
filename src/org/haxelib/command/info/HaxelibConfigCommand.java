package org.haxelib.command.info;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibConfigCommand extends HaxelibBaseCommand implements ICommand {

	//Injected
	public HaxelibRepository core;

	public HaxelibConfigCommand() {
		_name = HaxelibCommands.HAXELIB_CONFIG;
		_category = HaxelibCategories.INFO;
		_description = "print the repository path";
	}

	@Override
	public void run(String[] arguments) {
		println(config(), null);
	}

	public String config() {
		return core.getRepository().getAbsolutePath() + "\\\n";
	}
}
