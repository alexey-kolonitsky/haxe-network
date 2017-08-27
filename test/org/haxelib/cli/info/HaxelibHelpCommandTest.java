package org.haxelib.cli.info;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.command.HaxelibCommandCollection;
import org.haxelib.command.advanced.HaxelibSetupCommand;
import org.haxelib.command.basics.*;
import org.haxelib.command.info.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibHelpCommandTest extends CommandBaseTest {

	private HaxelibCommandCollection createCommandCollection() {
		HaxelibCommandCollection collection = new HaxelibCommandCollection();
		collection.addCommand(HaxelibInstallCommand.class);
		collection.addCommand(HaxelibUpdateCommand.class);
		collection.addCommand(HaxelibRemoveCommand.class);
		collection.addCommand(HaxelibListCommand.class);
		collection.addCommand(HaxelibSetCommand.class);

		collection.addCommand(HaxelibSearchCommand.class);
		collection.addCommand(HaxelibInfoCommand.class);
		collection.addCommand(HaxelibUserCommand.class);
		collection.addCommand(HaxelibConfigCommand.class);
		collection.addCommand(HaxelibPathCommand.class);
		collection.addCommand(HaxelibVersionCommand.class);
		collection.addCommand(HaxelibHelpCommand.class);

		collection.addCommand(HaxelibSetupCommand.class);
		return collection;
	}

	@Test
	public void help() throws Exception {
		HaxelibHelpCommand helpCommand = new HaxelibHelpCommand();
		helpCommand.commands = createCommandCollection();
		String actualResult = helpCommand.help();
		String expectedResult = exec("haxelib", "help");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
