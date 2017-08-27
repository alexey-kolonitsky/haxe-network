package org.haxelib.command.info;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.model.HaxelibVersion;
import org.haxelib.remote.HaxelibServer;

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
		String libraryName = arguments[0];
		HaxelibEntity result = server.get(libraryName);
		println("Name: " + result.name, null);
		println("Tags: " + result.tags, null);
		println("Desc: " + result.description, null);
		println("Website: " + result.url, null);
		println("Licence: " + result.license, null);
		println("Owner: " + result.owner, null);
		println("Last version: " + result.lastVersion, null);

		for (HaxelibVersion ver : result.versions) {
			println("\t" + ver.dateString + " " + ver.name + " : " + ver.comment, null);
		}
	}
}
