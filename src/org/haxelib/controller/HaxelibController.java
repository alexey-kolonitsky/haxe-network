package org.haxelib.controller;


import org.haxelib.command.HaxelibCommandCollection;
import org.haxelib.command.HaxelibCommandFactory;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.configuration.HaxelibConfigurator;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.data.HaxelibException;
import org.haxelib.core.HaxelibServer;

import java.io.IOException;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibController {

	private HaxelibCommandCollection commands;
	private HaxelibConfigurator configurator;
	private HaxelibCommandFactory factory;
	private HaxelibServer server;
	private HaxelibRepository core;

	public HaxelibController(HaxelibCommandCollection commands) {
		this.commands = commands;
		configurator = new HaxelibConfigurator();
		try {
			configurator.readConfig();
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		String path = configurator.get("haxelib.path");
		core = new HaxelibRepository(path);
		try {
			core.scanRepository();
		} catch (HaxelibException exception) {
			System.out.println("ERROR: " + exception.getMessage());
		} catch (IOException exception) {
			System.out.println("File ERROR: " + exception.getMessage());
		}

		String host = configurator.get("haxelib.server.host");
		String port = configurator.get("haxelib.server.port");
		String dir = configurator.get("haxelib.server.dir");
		String ver = configurator.get("haxelib.server.ver");
		String url = configurator.get("haxelib.server.url");
		server = new HaxelibServer(host, port, dir, ver, url);

		factory = new HaxelibCommandFactory(commands);
		factory.configurator = configurator;
		factory.server = server;
		factory.core = core;
	}

	public void runCommand(String name, String[] arguments) {
		ICommand cmd = factory.create(name);
		if (cmd == null)
			cmd = factory.create(HaxelibCommands.HAXELIB_HELP);
		try {
			cmd.run(arguments);
		} catch (HaxelibException exception) {
			System.out.println("ERROR: " + exception.getMessage());
		}
	}
}
