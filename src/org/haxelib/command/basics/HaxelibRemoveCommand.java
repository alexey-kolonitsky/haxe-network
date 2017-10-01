package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.data.HaxelibException;
import org.haxelib.utils.KString;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibRemoveCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository repo;

	public HaxelibRemoveCommand() {
		_category = HaxelibCategories.BASE;
		_name = HaxelibCommands.HAXELIB_REMOVE;
		_description = "remove a given library/version";
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {
		switch (arguments.length) {
			case 1:
				remove(arguments[0], "");
				break;
			case 2:
				remove(arguments[0], arguments[1]);
				break;
			default:
				wrongFormatError();
		}
	}

	public void remove(String libraryName, String libraryVersion) throws HaxelibException {
		if (KString.isEmpty(libraryVersion)) {
			repo.remove(libraryName);
		}
		else {
			repo.removeVersion(libraryName, libraryVersion);
		}
	}
}
