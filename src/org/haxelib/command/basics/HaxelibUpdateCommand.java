package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.HaxelibServer;
import org.haxelib.core.data.HaxelibException;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.model.HaxelibVersion;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibUpdateCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository repo;
	public HaxelibServer server;

	public HaxelibUpdateCommand() {
		_name = HaxelibCommands.HAXELIB_UPDATE;
		_description = "update a single library (if given) or all installed libraries";
		_category = HaxelibCategories.BASE;
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {
		switch (arguments.length) {
			case 0:
				update();
				break;
			case 1:
				updateLibrary(arguments[0]);
				break;
			default:
				wrongFormatError();
		}
	}

	public String update() throws HaxelibException{
		ArrayList<HaxelibEntity> list = repo.list();
		for (HaxelibEntity lib : list) {
			updateLibrary(lib.name);
		}
		return _output;
	}

	public String updateLibrary(String libraryName) throws HaxelibException{
		HaxelibEntity lib = server.get(libraryName);
		if (lib == null) {
			return _output;
		}
		InputStream in = server.download(libraryName, lib.lastVersion);
		repo.install(libraryName, lib.lastVersion, in);
		return _output;
	}
}
