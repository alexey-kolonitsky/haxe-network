package org.haxelib.cli.advanced;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.command.advanced.HaxelibSetupCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibSetupCommandTest extends CommandBaseTest {

	@Test
	public void setupSuccessful() throws Exception {
		HaxelibSetupCommand setupCommand = new HaxelibSetupCommand();
		setupCommand.repository = createRepository("C:\\alexey\\apps\\haxe\\lib\\");
		String actualResult = setupCommand.setup("C:\\alexey\\apps\\haxelib-super1");
		String expectedResult = exec("haxelib", "setup", "C:\\alexey\\apps\\haxelib-super1");
		assertEquals("The files differ!", expectedResult, actualResult);
		//Revert Haxe lib to standart path
		exec("haxelib", "setup", "C:\\alexey\\apps\\haxe\\lib");
	}

	@Test
	public void setupUnsuccessful() throws Exception {
		HaxelibSetupCommand setupCommand = new HaxelibSetupCommand();
		setupCommand.repository = createRepository("C:\\alexey\\apps\\haxe\\lib\\");
		String actualResult = setupCommand.setup("C:\\alexey\\apps\\opam64.tar.xz");
		String expectedResult = exec("haxelib", "setup", "C:\\alexey\\apps\\opam64.tar.xz");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
