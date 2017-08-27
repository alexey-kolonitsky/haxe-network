package org.haxelib.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibCommandCollection {

	private ArrayList<Class<ICommand>> commands = new ArrayList<>();

	public void addCommand(Class classDefinition) {
		if (commands.contains(classDefinition)) {
			new Exception("Command with name `" + classDefinition + "` already registered in collection");
		}
		commands.add(classDefinition);
	}

	public ArrayList<Class<ICommand>> getCommands() {
		return commands;
	}
}
