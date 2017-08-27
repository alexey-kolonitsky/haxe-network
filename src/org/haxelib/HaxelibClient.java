package org.haxelib;

import org.haxelib.command.HaxelibCommandCollection;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.basics.*;
import org.haxelib.command.info.*;
import org.haxelib.controller.HaxelibController;

import java.util.Arrays;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibClient {

	public static HaxelibController controller = null;

	public static void main(String[] args) throws Exception {
		initilizeController();
		runCommand(args);
	}

	private static void runCommand(String[] args) {
		int n = args.length;
		if (args == null || n == 0) {
			controller.runCommand(HaxelibCommands.HAXELIB_HELP, args);
		}
		else {
			controller.runCommand(args[0], Arrays.copyOfRange(args, 1, n));
		}
	}

	private static void initilizeController() {
		HaxelibCommandCollection collection = new HaxelibCommandCollection();
		collection.addCommand(HaxelibInstallCommand.class);
		collection.addCommand(HaxelibUpdateCommand.class);
		collection.addCommand(HaxelibRemoveCommand.class);
		collection.addCommand(HaxelibListCommand.class);
		collection.addCommand(HaxelibSetCommand.class);

		collection.addCommand(HaxelibHelpCommand.class);
		collection.addCommand(HaxelibPathCommand.class);
		collection.addCommand(HaxelibVersionCommand.class);
		collection.addCommand(HaxelibConfigCommand.class);
		collection.addCommand(HaxelibSearchCommand.class);
		collection.addCommand(HaxelibInfoCommand.class);
		controller = new HaxelibController(collection);
	}


}
