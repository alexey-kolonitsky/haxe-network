package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.data.HaxelibException;

import java.io.IOException;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibSetCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository repo;

	public HaxelibSetCommand() {
		_category = HaxelibCategories.BASE;
		_name = HaxelibCommands.HAXELIB_SET;
		_description = "set the current version for a library";
	}

	@Override
	public void run(String[] arguments) throws HaxelibException{
		switch (arguments.length) {
			case 2:
				setVersion(arguments[0], arguments[1]);
			default:
				wrongFormatError();
		}
	}

	public String setVersion(String libraryName, String libraryVersion) throws HaxelibException {
		repo.setVersion(libraryName, libraryVersion);
		println("Library {0} current version is now {1}", libraryName, libraryVersion);
		return _output;
	}
}
