package org.haxelib.command;

import org.haxelib.command.info.*;
import org.haxelib.command.basics.*;
import org.haxelib.configuration.HaxelibConfigurator;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.HaxelibServer;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibCommandFactory {

	private HaxelibCommandCollection commands;

	//Injectable
	public HaxelibConfigurator configurator;
	public HaxelibServer server;
	public HaxelibRepository core;

	public HaxelibCommandFactory(HaxelibCommandCollection commands) {
		this.commands = commands;
	}

	public ICommand create(String name) {
		if (commands.hasCommand(name)) {
			ICommand command = commands.getCommand(name);
			// injection for help command
			switch (name) {
				case HaxelibCommands.HAXELIB_HELP:
					HaxelibHelpCommand helpCommand = (HaxelibHelpCommand) command;
					helpCommand.commands = commands;
					break;
				case HaxelibCommands.HAXELIB_CONFIG:
					HaxelibConfigCommand configCommand = (HaxelibConfigCommand) command;
					configCommand.core = core;
					break;
				case HaxelibCommands.HAXELIB_LIST:
					HaxelibListCommand listCommand = (HaxelibListCommand) command;
					listCommand.repo = core;
					break;
				case HaxelibCommands.HAXELIB_PATH:
					HaxelibPathCommand pathCommand = (HaxelibPathCommand) command;
					pathCommand.core = core;
					break;
				case HaxelibCommands.HAXELIB_SEARCH:
					HaxelibSearchCommand searchCommand = (HaxelibSearchCommand) command;
					searchCommand.server = server;
					break;
				case HaxelibCommands.HAXELIB_USER:
					HaxelibUserCommand userCommand = (HaxelibUserCommand) command;
					userCommand.server = server;
					break;
				case HaxelibCommands.HAXELIB_INFO:
					HaxelibInfoCommand infoCommand = (HaxelibInfoCommand) command;
					infoCommand.server = server;
					break;
				case HaxelibCommands.HAXELIB_REMOVE:
					HaxelibRemoveCommand removeCommand = (HaxelibRemoveCommand) command;
					removeCommand.repo = core;
					break;
				case HaxelibCommands.HAXELIB_INSTALL:
					HaxelibInstallCommand installCommand = (HaxelibInstallCommand) command;
					installCommand.server = server;
					installCommand.repo = core;
					break;
			}
			return command;
		}
		else {
			System.out.println("Internal error: unknown command name " + name);
			return null;
		}
	}
}
