package org.haxelib.cli.info;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.command.info.HaxelibSearchCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibSearchCommandTest extends CommandBaseTest {

	@Test
	public void search() throws Exception {
		HaxelibSearchCommand listCommand = new HaxelibSearchCommand();
		listCommand.server = createService();
		String actualResult = listCommand.search("as3");
		String expectedResult = exec("haxelib", "search", "as3");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
