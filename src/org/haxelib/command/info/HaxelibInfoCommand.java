package org.haxelib.command.info;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.model.HaxelibVersion;
import org.haxelib.core.HaxelibServer;

/**
 * Haxe Library Manager 3.3.0 - (c)2006-2016 Haxe Foundation
 * Usage: haxelib [command] [options]
 */
public class HaxelibInfoCommand extends HaxelibBaseCommand implements ICommand {

	//Injected
	public HaxelibServer server;

	public HaxelibInfoCommand() {
		_category = HaxelibCategories.INFO;
		_description = "list information on a given library";
		_name = HaxelibCommands.HAXELIB_INFO;
	}

	@Override
	public void run(String[] arguments) {
		switch (arguments.length) {
			case 1:
				println(info(arguments[0]), null);
				break;
			default:
				wrongFormatError();
				break;
		}
	}

	public String info(String libraryName) {
		HaxelibEntity lib = server.get(libraryName);
		if (lib == null) {
			return "Error: No such Project : " + libraryName + "\n";
		}
		int tagsCount = lib.tags.size();
		String tagsString = tagsCount > 0 ? lib.tags.get(0) : "";
		for (int i = 1; i < tagsCount; i++)
			tagsString += ", " + lib.tags.get(i);
		String result = "Name: " + lib.name
			+ "\nTags: " + tagsString
			+ "\nDesc: " + lib.description
			+ "\nWebsite: " + lib.url
			+ "\nLicense: " + lib.license
			+ "\nOwner: " + lib.owner
			+ "\nVersion: " + lib.lastVersion
			+ "\nReleases: ";

		for (HaxelibVersion ver : lib.versions) {
			result += "\n   " + ver.dateString + " " + ver.name + " : " + ver.comment;
		}
		return result + "\n";
	}
}
