package org.haxelib.cli.basic;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.cli.Constants;
import org.haxelib.command.basics.HaxelibInstallCommand;
import org.haxelib.command.basics.HaxelibListCommand;
import org.haxelib.command.basics.HaxelibUpdateCommand;
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.HaxelibServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibUpdateCommandTest extends CommandBaseTest {

	private static HaxelibRepository repo = null;
	private static HaxelibServer server = null;
	String actualResult;
	String expectedResult;

	@BeforeClass
	public static void setup() throws Exception {
		repo = createRepository(Constants.TEST_REPO_1_PATH);
		server = createService();

		// Set haxelib playground repository
		exec("haxelib", "setup", Constants.TEST_REPO_2_PATH);
	}

	@AfterClass
	public static void unsetup() throws Exception {
		exec("haxelib", "setup", Constants.ORIGINAL_REPO_PATH);
	}

	@Test
	public void updateByName() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Repositories should be equal", expectedResult, actualResult);
		//
		exec("haxelib", "install", Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION);
		HaxelibInstallCommand installCommand = new HaxelibInstallCommand();
		installCommand.repo = repo;
		installCommand.server = server;
		installCommand.installFromServer(Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION);
		//
		expectedResult = exec("haxelib", "update", Constants.TEST_LIBRARY_NAME);
		HaxelibUpdateCommand updateCommand = new HaxelibUpdateCommand();
		updateCommand.repo = repo;
		updateCommand.server = server;
		actualResult = updateCommand.updateLibrary(Constants.TEST_LIBRARY_NAME);
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
	}

	@Test
	public void updateAll() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		exec("haxelib", "install", "jsprop", "1.0.0");
		HaxelibInstallCommand installCommand = new HaxelibInstallCommand();
		installCommand.repo = repo;
		installCommand.server = server;
		installCommand.installFromServer("jsprop", "1.0.0");
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals(expectedResult, actualResult);
		//
		expectedResult = exec("haxelib", "update", Constants.TEST_LIBRARY_NAME);
		HaxelibUpdateCommand updateCommand = new HaxelibUpdateCommand();
		updateCommand.repo = repo;
		updateCommand.server = server;
		actualResult = updateCommand.updateLibrary(Constants.TEST_LIBRARY_NAME);
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
	}

}
