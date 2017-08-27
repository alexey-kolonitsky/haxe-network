package org.haxelib.cli.info;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.command.info.HaxelibUserCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibUserCommandTest extends CommandBaseTest {

	@Test
	public void user() throws Exception {
		HaxelibUserCommand userCommand = new HaxelibUserCommand();
		userCommand.server = createService();
		String actualResult = userCommand.user("ncannasse");
		String expectedResult = exec("haxelib", "user", "ncannasse");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
