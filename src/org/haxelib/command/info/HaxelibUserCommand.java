package org.haxelib.command.info;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibServer;
import org.haxelib.core.data.HaxelibUser;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.model.HaxelibVersion;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibUserCommand extends HaxelibBaseCommand implements ICommand {

	//Injected
	public HaxelibServer server;

	public HaxelibUserCommand() {
		_category = HaxelibCategories.INFO;
		_description = "list information on a given user";
		_name = HaxelibCommands.HAXELIB_USER;
	}

	@Override
	public void run(String[] arguments) {
		switch (arguments.length) {
			case 1:
				println(user(arguments[0]), null);
				break;
			default:
				wrongFormatError();
				break;
		}
	}

	public String user(String userId) {
		HaxelibUser user = server.getUser(userId);
		if (user == null) {
			return "Error: No such user : " + userId + "\n";
		}
		String result = "Id: " + user.id
			+ "\nName: " + user.name
			+ "\nMail: " + user.email
			+ "\nLibraries: ";

		for (String libraryName : user.libraries) {
			result += "\n  " + libraryName;
		}
		return result + "\n";
	}
}
