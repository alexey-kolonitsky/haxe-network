package org.haxelib.command;

import org.haxelib.HaxelibConstants;
import org.haxelib.core.data.HaxelibException;

import java.text.MessageFormat;

/**
 * Created by akalanitski on 18.08.2017.
 */
public class HaxelibBaseCommand implements ICommand {

	//---------------------------------
	// category
	//---------------------------------

	protected String _category = HaxelibCategories.MISC;

	@Override
	public String getCategory() {
		return _category;
	}


	//---------------------------------
	// descriptor
	//---------------------------------

	protected String _description;

	@Override
	public String getDescription() {
		return _description;
	}


	//---------------------------------
	// name
	//---------------------------------

	protected String _name;

	public String getName() {
		return _name;
	}


	//---------------------------------
	// usage
	//---------------------------------

	protected String _usage;

	@Override
	public String getUsage() {
		return _usage;
	}


	//---------------------------------
	// output
	//---------------------------------

	protected String _output;

	public String getOutput() {
		return _output;
	}


	//---------------------------------
	// Constructor
	//---------------------------------

	public HaxelibBaseCommand() {
		_output = "";
		_usage = HaxelibConstants.APPLICATION_COMMAND + " " + _name;
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {
		println("{0}: Internal error. Command ''{1}'' is not implemented", HaxelibConstants.APPLICATION_COMMAND, _name);
	}


	public void internalError(String message, Exception exception) {
		String str = "Internal error: " + message + exception.toString();
		_output += str + "\n";
		println(str, null);
	}

	public void wrongFormatError() {
		String message = MessageFormat.format("{0} {1} Wrong format: ", HaxelibConstants.APPLICATION_COMMAND, _name);
		_output += message + "\n";
		System.out.println(message);
	}

	public void println(String message, Object...params) {
		MessageFormat temp = new MessageFormat(message);
		message = temp.format(params);
		_output += message + "\n";
		System.out.println(message);
	}

}
