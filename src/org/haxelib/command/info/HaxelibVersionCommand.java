package org.haxelib.command.info;

import org.haxelib.HaxelibConstants;
import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibVersionCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibVersionCommand() {
		_name = HaxelibCommands.HAXELIB_VERSION;
		_category = HaxelibCategories.INFO;
		_description = "display this list of options";
	}

	@Override
	public void run(String[] arguments) {
		println(HaxelibConstants.APPLICATION_VERSION, null);
	}
}
