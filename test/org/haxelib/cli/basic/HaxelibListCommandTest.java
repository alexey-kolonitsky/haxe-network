package org.haxelib.cli.basic;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.cli.Constants;
import org.haxelib.command.basics.HaxelibListCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibListCommandTest extends CommandBaseTest {

	@Test
	public void list() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = createRepository(Constants.ORIGINAL_REPO_PATH);
		String actualResult = listCommand.list();
		String expectedResult = exec("haxelib", "list");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
