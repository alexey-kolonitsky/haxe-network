package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.HaxelibException;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibRemoveCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository core;

	public HaxelibRemoveCommand() {
		_category = HaxelibCategories.BASE;
		_name = HaxelibCommands.HAXELIB_REMOVE;
		_description = "remove a given library/version";
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {
		String libraryName;
		String libraryVersion;
		switch (arguments.length) {
			case 1:
				libraryName = arguments[0];
				core.remove(libraryName);
				break;
			case 2:
				libraryName = arguments[0];
				libraryVersion = arguments[1];
				core.remoteVersion(libraryName, libraryVersion);
				break;
			default:
				wrongFormatError();
		}
	}
}
