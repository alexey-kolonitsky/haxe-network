package org.haxelib.command.basics;

import org.haxelib.HaxelibConstants;
import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.HaxelibException;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.remote.HaxelibServer;
import org.haxelib.utils.KPath;
import org.haxelib.utils.KString;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibInstallCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository core;
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

	private void installAll() { //Install from all files

	}

	private void installFromBuildFile(String argument) {

	}

	private void installFromArhive(String archiveName) {

	}

	private void installFromServer(String libraryName, String libraryVersion) throws HaxelibException {
		HaxelibEntity remoteLib = server.get(libraryName);
		if (remoteLib == null) {
			println("Library '{0} not found in remote repository'", new Object[]{libraryName});
			return;
		}

		if (KString.isEmpty(libraryVersion))
			libraryVersion = remoteLib.lastVersion;

		HaxelibEntity localLib = core.get(libraryName);
		if (localLib == null) {
			doInstall(libraryName, libraryVersion);
			return;
		}

		if (localLib.currentVersion.version.equals(libraryVersion)) {
			println("Library '{0}' already installed", new Object[]{libraryName});
			return;
		}

		doInstall(libraryName, libraryVersion);
	}

	private void doInstall(String libraryName, String libraryVersion) throws HaxelibException {
		String homeDirectoryPath = core.createLibraryPath(libraryName, libraryVersion);
		File homeDirectory = new File(homeDirectoryPath);
		homeDirectory.mkdirs();
		try {
			InputStream in = server.download(libraryName, libraryVersion);
			BufferedInputStream buf = new BufferedInputStream(in);
			buf.mark(Integer.MAX_VALUE);

			// Find basePath inside of archive
			String basePath = null;
			ZipInputStream zis = new ZipInputStream(buf);
			ZipEntry zipEntry = zis.getNextEntry();
			while(zipEntry != null) {
				if (!zipEntry.isDirectory()) {
					KPath fileName = new KPath(zipEntry.getName());
					if (fileName.last().equals(HaxelibConstants.LIBRARY_DESCRIPTION_FILE_NAME)) {
						if (KString.isEmpty(basePath)) {
							basePath = fileName.getPath();
						}
						else {
							internalError("Two haxelib.json files in one archive", null);
							return;
						}
					}
				}
				zipEntry = zis.getNextEntry();
			}

			// copy to destination folder
			byte[] buffer = new byte[1024];
			buf.reset();
			zis = new ZipInputStream(buf);
			zipEntry = zis.getNextEntry();
			while(zipEntry != null) {
				if (!zipEntry.isDirectory()) {
					KPath kp = new KPath(homeDirectoryPath);
					String fileName = zipEntry.getName();
					if (fileName.contains(basePath)) {
						kp.append(fileName.substring(basePath.length()));
					}
					File unpackedFile = new File(kp.toString());
					File unpackedFileParent = new File(unpackedFile.getParent());
					if (!unpackedFileParent.exists())
						unpackedFileParent.mkdirs();

					println("Unpack file: {0} to {1}", new Object[]{fileName, homeDirectoryPath});
					FileOutputStream fos = new FileOutputStream(unpackedFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				zipEntry = zis.getNextEntry();
			}
			buf.close();

			// add current version file
			core.setVersion(libraryName, libraryVersion);
			// add library to the cash
			core.add(libraryName);
		} catch (IOException exception) {
			internalError(null, exception);
		}
	}
}
