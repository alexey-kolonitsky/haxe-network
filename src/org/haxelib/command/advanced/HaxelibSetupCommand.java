package org.haxelib.command.advanced;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.command.basics.HaxelibRemoveCommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.data.HaxelibException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibSetupCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository repository;

	public HaxelibSetupCommand() {
		_name = HaxelibCommands.HAXELIB_SETUP;
		_description = "set the haxelib repository path";
		_category = HaxelibCategories.MISC;
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {
		switch (arguments.length) {
			case 1:
				try {
					println(setup(arguments[0]), null);
				} catch (IOException exception){
					exception.printStackTrace();
				}
				break;
			default:
				wrongFormatError();
				break;
		}
	}

	public String setup(String pathToRepo) throws HaxelibException, IOException {
		Path newRepoPath = Paths.get(pathToRepo);
		File newRepo = new File(newRepoPath.toString());
		try {
			repository.setRepository(newRepo);
		} catch (HaxelibException exception) {
			return exception.getMessage() + "\n";
		}
		return "haxelib repository is now " + newRepoPath + "\n";
	}
}
