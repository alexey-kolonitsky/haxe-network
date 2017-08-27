package org.haxelib.command.info;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.model.HaxelibDependency;
import org.haxelib.core.HaxelibServer;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibSearchCommand extends HaxelibBaseCommand implements ICommand {

	//Injected
	public HaxelibServer server;

	public HaxelibSearchCommand() {
		_category = HaxelibCategories.INFO;
		_description = "list libraries matching a word";
		_name = HaxelibCommands.HAXELIB_SEARCH;
	}

	@Override
	public void run(String[] arguments) {
		switch (arguments.length) {
			case 1:
				println(search(arguments[0]), null);
				break;
			default:
				wrongFormatError();
				break;
		}
	}

	public String search(String filter) {
		String str = "";
		HaxelibDependency[] result = server.search(filter);
		for (HaxelibDependency lib : result) {
			str += lib.name + "\n";
		}
		str += result.length + " libraries found\n";
		return str;
	}
}
