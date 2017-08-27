package org.haxelib.command;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibCommandCollection {

	private ArrayList<Class<ICommand>> _commandClasses = new ArrayList<>();
	private HashMap<String, ICommand> _commandByNameMap = new HashMap<String, ICommand>();
	private ArrayList<ICommand> _commands = new ArrayList<>();
	private int _numCommand = 0;

	public void addCommand(Class classDefinition) {
		ICommand command = null;
		if (_commandClasses.contains(classDefinition)) {
			System.out.println("Command with name '" + classDefinition + "' already registered in collection");
		}

		try {
			command = (ICommand) classDefinition.newInstance();
			String commandName = command.getName();
			if (hasCommand(commandName)) {
				System.out.println("ERROR: Internal. Command with name '" + commandName + "' already registered.");
			}

			_commandClasses.add(classDefinition);
			_commandByNameMap.put(commandName, command);
			_commands.add(command);
			_numCommand++;
		} catch (Exception ex) {
			System.out.println("Internal error: can't instantiate command " + classDefinition);
		}
	}

	public ArrayList<Class<ICommand>> get_commandClasses() {
		return _commandClasses;
	}

	public boolean hasCommand(String commandName) {
		return _commandByNameMap.containsKey(commandName);
	}

	public ICommand getCommand(String name) {
		if (hasCommand(name)) {
			return _commandByNameMap.get(name);
		}
		return null;
	}

	public ICommand getCommandAt(int index) {
		return _commands.get(index);
	}

	public int numCommand() {
		return _numCommand;
	}
}
