package org.haxelib.cli.basic;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.cli.Constants;
import org.haxelib.command.basics.HaxelibListCommand;
import org.haxelib.command.basics.HaxelibRemoveCommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.HaxelibServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HaxelibRemoveCommandTest extends CommandBaseTest {

	private static HaxelibRepository repo = null;
	private static HaxelibServer server = null;
	String actualResult;
	String expectedResult;

	@BeforeClass
	public static void setup() throws Exception {
		repo = createRepository(Constants.TEST_REPO_1_PATH);
		exec("haxelib", "setup", Constants.TEST_REPO_2_PATH);
	}

	@AfterClass
	public static void unsetup() throws Exception {
		exec("haxelib", "setup", Constants.ORIGINAL_REPO_PATH);
	}

	@Test()
	public void remove1_ByVersion() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		String actualResult = listCommand.list();
		String expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		exec("haxelib", "remove", Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION);
		HaxelibRemoveCommand removeCommand = new HaxelibRemoveCommand();
		removeCommand.repo = repo;
		removeCommand.run(new String[]{Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION});
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories should have `" + Constants.TEST_LIBRARY_NAME + " " + Constants.TEST_LIBRARY_VERSION + "`", expectedResult, actualResult);
	}

	@Test
	public void remove2_ByName() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		exec("haxelib", "remove", Constants.TEST_LIBRARY_NAME);
		HaxelibRemoveCommand removeCommand = new HaxelibRemoveCommand();
		removeCommand.repo = repo;
		removeCommand.run(new String[]{Constants.TEST_LIBRARY_NAME});
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories shouldn't have `" + Constants.TEST_LIBRARY_NAME + "`", expectedResult, actualResult);
	}

}
