package org.haxelib.command;

import org.haxelib.core.data.HaxelibException;

/**
 * Created by akalanitski on 07.08.2017.
 */
public interface ICommand {

	/**
	 * Execute command
	 * @param arguments
	 */
	void run(String[] arguments) throws HaxelibException;

	String getCategory();
	String getDescription();
	String getUsage();
	String getName();
}
