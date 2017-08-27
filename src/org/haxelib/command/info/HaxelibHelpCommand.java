package org.haxelib.command.info;

import org.haxelib.HaxelibConstants;
import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibHelpCommand extends HaxelibBaseCommand implements ICommand {

	//Injected
	public HashMap<String, ICommand> commandByNameMap;

	public HaxelibHelpCommand() {
		_name = HaxelibCommands.HAXELIB_HELP;
		_category = HaxelibCategories.INFO;
		_description = "display this list of options";
	}

	@Override
	public void run(String[] arguments) {
		String header = "{0} {1} - (c) 2017 - {2} {3} {4}";
		Calendar calendar = Calendar.getInstance();
		println(header,new Object[]{
			HaxelibConstants.APPLICATION_NAME,
			HaxelibConstants.APPLICATION_VERSION,
			calendar.get(Calendar.YEAR),
			HaxelibConstants.APPLICATION_AUTHOR,
			HaxelibConstants.APPLICATION_AUTHOR_EMAIL});
		println("Usage: haxelib [command] [options]", null);
		String category = "";
		if (commandByNameMap != null) {
			for (ICommand command : commandByNameMap.values()) {
				boolean isNewCategory = !category.equals(command.getCategory());
				if (isNewCategory) {
					category = command.getCategory();
					println(category + ":", null);
				}
				println("\t" + command.getName() + "\t: " + command.getDescription(), null);
			}
		}
	}



}
