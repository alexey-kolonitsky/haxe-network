package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.data.HaxelibException;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.core.HaxelibServer;
import org.haxelib.utils.KString;

import java.io.*;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibInstallCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository repo;
	public HaxelibServer server;

	public HaxelibInstallCommand() {
		_category = HaxelibCategories.BASE;
		_name = HaxelibCommands.HAXELIB_INSTALL;
		_description = "install a given library, or all libraries from a hxml file";
	}

	@Override
	public void run(String[] arguments) throws HaxelibException {
		switch (arguments.length) {
			case 1:
				String argument = arguments[0];
				if (argument.equals("all")) {
					installAll();
				} else {
					installFromArhive(argument);
					installFromBuildFile(argument);
					installFromServer(argument, null);
				}
				break;
			case 2:
				installFromServer(arguments[0], arguments[1]);
				break;
			default:
				wrongFormatError();
				break;
		}
	}

	public void installAll() { //Install from all files

	}

	public void installFromBuildFile(String argument) {

	}

	public void installFromArhive(String archiveName) {

	}

	public String installFromServer(String libraryName, String libraryVersion) throws HaxelibException {
		HaxelibEntity remoteLib = server.get(libraryName);
		if (remoteLib == null) {
			println("Error: No such Project : {0}", libraryName);
			return _output;
		}

		if (KString.isEmpty(libraryVersion))
			libraryVersion = remoteLib.lastVersion;

		HaxelibEntity localLib = repo.get(libraryName);
		if (localLib == null) {
			InputStream in = server.download(libraryName, libraryVersion);
			repo.install(libraryName, libraryVersion, in);
			return _output;
		}

		if (localLib.currentVersion.version.equals(libraryVersion)) {
			println("You already have {0} version {1} installed", libraryName, libraryVersion);
			return _output;
		}
		InputStream in = server.download(libraryName, libraryVersion);
		repo.install(libraryName, libraryVersion, in);
		return _output;
	}
}
