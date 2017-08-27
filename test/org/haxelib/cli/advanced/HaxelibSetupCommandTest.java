package org.haxelib.cli.advanced;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.command.advanced.HaxelibSetupCommand;
import org.haxelib.command.info.HaxelibInfoCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibSetupCommandTest extends CommandBaseTest {

	@Test
	public void setupSuccessful() throws Exception {
		HaxelibSetupCommand setupCommand = new HaxelibSetupCommand();
		setupCommand.repository = createCore("C:\\alexey\\apps\\haxe\\lib\\");
		String actualResult = setupCommand.setup("C:\\alexey\\apps\\haxelib-super1");
		String expectedResult = exec("haxelib", "setup", "C:\\alexey\\apps\\haxelib-super1");
		assertEquals("The files differ!", expectedResult, actualResult);
	}

	@Test
	public void setupUnsuccessful() throws Exception {
		HaxelibSetupCommand setupCommand = new HaxelibSetupCommand();
		setupCommand.repository = createCore("C:\\alexey\\apps\\haxe\\lib\\");
		String actualResult = setupCommand.setup("C:\\alexey\\apps\\opam64.tar.xz");
		String expectedResult = exec("haxelib", "setup", "C:\\alexey\\apps\\opam64.tar.xz");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
