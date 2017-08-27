package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibException;

import java.util.ArrayList;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibSetCommand extends HaxelibBaseCommand implements ICommand {
	public HaxelibSetCommand() {
		_category = HaxelibCategories.BASE;
		_name = HaxelibCommands.HAXELIB_SET;
		_description = "set the current version for a library";
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {

	}
}
