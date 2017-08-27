package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;

import java.util.ArrayList;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibUpdateCommand extends HaxelibBaseCommand implements ICommand {
	public HaxelibUpdateCommand() {
		_name = HaxelibCommands.HAXELIB_UPDATE;
		_description = "update a single library (if given) or all installed libraries";
		_category = HaxelibCategories.BASE;
	}
}
