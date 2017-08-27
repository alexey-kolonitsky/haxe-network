package org.haxelib.command.basics;

import org.haxelib.command.HaxelibBaseCommand;
import org.haxelib.command.HaxelibCategories;
import org.haxelib.command.HaxelibCommands;
import org.haxelib.command.ICommand;
import org.haxelib.core.data.CurrentVersion;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.model.HaxelibEntity;
import org.haxelib.model.HaxelibVersion;

import java.util.ArrayList;

/**
 * Created by akalanitski on 07.08.2017.
 */
public class HaxelibListCommand extends HaxelibBaseCommand implements ICommand {

	public HaxelibRepository core;

	public HaxelibListCommand() {
		_category = HaxelibCategories.BASE;
		_name = HaxelibCommands.HAXELIB_LIST;
		_description = "list all installed libraries";
	}

	@Override
	public void run(String[] arguments) {
		String result = list();
		println(result, null);
	}

	public String list() {
		String result = "";
		ArrayList<HaxelibEntity> list = core.list();
		for (HaxelibEntity entity : list) {
			String libraryName = entity.name;
			CurrentVersion current = entity.currentVersion;
			String versions = "";
			for (HaxelibVersion version : entity.versions) {
				if (version.name.equals(current.version))
					versions += " [" + version.name + "]";
				else
					versions += " " + version.name;
				if (current.kind == HaxelibEntity.HAXELIB_TYPE_VCS) {
					// Print repository, branch and commit
				}
			}
			if (current.kind == HaxelibEntity.HAXELIB_TYPE_DEVELOPMENT) {
				versions += " [" + current.version + ":"+ current.path + "]";
			}
			result += libraryName + ":" + versions + "\n";
		}
		return result;
	}

}
