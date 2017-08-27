package org.haxelib.cli.info;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.command.info.HaxelibInfoCommand;
import org.haxelib.command.info.HaxelibSearchCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibInfoCommandTest extends CommandBaseTest {

	@Test
	public void searchSuccessful() throws Exception {
		HaxelibInfoCommand infoCommand = new HaxelibInfoCommand();
		infoCommand.server = createService();
		String actualResult = infoCommand.info("utest");
		String expectedResult = exec("haxelib", "info", "utest");
		assertEquals("The files differ!", expectedResult, actualResult);
	}

	@Test
	public void searchUnsuccessful() throws Exception {
		HaxelibInfoCommand infoCommand = new HaxelibInfoCommand();
		infoCommand.server = createService();
		String actualResult = infoCommand.info("Joulupukki");
		String expectedResult = exec("haxelib", "info", "Joulupukki");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
