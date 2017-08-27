package org.haxelib.command;

import org.haxelib.command.info.*;
import org.haxelib.command.basics.*;
import org.haxelib.configuration.HaxelibConfigurator;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.remote.HaxelibServer;

import java.util.HashMap;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibCommandFactory {

	private HaxelibCommandCollection commands;
	private HashMap<String, ICommand> commandByNameMap;

	//Injectable
	public HaxelibConfigurator configurator;
	public HaxelibServer server;
	public HaxelibRepository core;

	public HaxelibCommandFactory(HaxelibCommandCollection commands) {
		commandByNameMap = new HashMap<String, ICommand>();
		for (Class<ICommand> commandClass : commands.getCommands()) {
			try {
				ICommand command = commandClass.newInstance();
				commandByNameMap.put(command.getName(), command);
			}
			catch (Exception ex) {
				System.out.println("Internal error: can't instantiate command " + commandClass);
			}
		}
	}

	public ICommand create(String name) {
		if (commandByNameMap.containsKey(name)){
			ICommand command = commandByNameMap.get(name);
			// injection for help command
			switch (name) {
				case HaxelibCommands.HAXELIB_HELP:
					HaxelibHelpCommand helpCommand = (HaxelibHelpCommand) command;
					helpCommand.commandByNameMap = commandByNameMap;
					break;
				case HaxelibCommands.HAXELIB_CONFIG:
					HaxelibConfigCommand configCommand = (HaxelibConfigCommand) command;
					configCommand.core = core;
					break;
				case HaxelibCommands.HAXELIB_LIST:
					HaxelibListCommand listCommand = (HaxelibListCommand) command;
					listCommand.core = core;
					break;
				case HaxelibCommands.HAXELIB_PATH:
					HaxelibPathCommand pathCommand = (HaxelibPathCommand) command;
					pathCommand.core = core;
					break;
				case HaxelibCommands.HAXELIB_SEARCH:
					HaxelibSearchCommand searchCommand = (HaxelibSearchCommand) command;
					searchCommand.server = server;
					break;
				case HaxelibCommands.HAXELIB_INFO:
					HaxelibInfoCommand infoCommand = (HaxelibInfoCommand) command;
					infoCommand.server = server;
					break;
				case HaxelibCommands.HAXELIB_REMOVE:
					HaxelibRemoveCommand removeCommand = (HaxelibRemoveCommand) command;
					removeCommand.core = core;
					break;
				case HaxelibCommands.HAXELIB_INSTALL:
					HaxelibInstallCommand installCommand = (HaxelibInstallCommand) command;
					installCommand.server = server;
					installCommand.core = core;
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
