package org.haxelib.command.info;

import org.haxelib.HaxelibConstants;
import org.haxelib.command.*;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibHelpCommand extends HaxelibBaseCommand implements ICommand {

	//Injected
	public HaxelibCommandCollection commands;

	public HaxelibHelpCommand() {
		_name = HaxelibCommands.HAXELIB_HELP;
		_category = HaxelibCategories.INFO;
		_description = "display this list of options";
	}

	@Override
	public void run(String[] arguments) {
		String result = help();
		println(result, null);
	}

	public String help() {
		//Header
		Calendar calendar = Calendar.getInstance();
		String result = HaxelibConstants.APPLICATION_NAME + " " + HaxelibConstants.APPLICATION_VERSION + " - "
			+ "(c) 2017 - " + calendar.get(Calendar.YEAR)
			+ " " + HaxelibConstants.APPLICATION_AUTHOR + " " + HaxelibConstants.APPLICATION_AUTHOR_EMAIL;
		result += "\n  Usage: haxelib [command] [options]";
		//Commands by categories
		String category = "";
		if (commands != null) {
			for (int i = 0; i < commands.numCommand(); i++) {
				ICommand command = commands.getCommandAt(i);
				boolean isNewCategory = !category.equals(command.getCategory());
				if (isNewCategory) {
					category = command.getCategory();
					result += "\n  " + category;
				}
				result += "\n    " + String.format("%-9s : %s", command.getName(), command.getDescription());
			}
		}
		return result;
	}



}
