package org.haxelib.cli.basic;

import org.haxelib.cli.CommandBaseTest;
import org.haxelib.cli.Constants;
import org.haxelib.command.basics.HaxelibInstallCommand;
import org.haxelib.command.basics.HaxelibListCommand;
import org.haxelib.command.basics.HaxelibSetCommand;
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
public class HaxelibSetCommandTest extends CommandBaseTest {

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
	public void setVersion() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		exec("haxelib", "install", Constants.TEST_LIBRARY_NAME);
		//
		HaxelibInstallCommand installCommand = new HaxelibInstallCommand();
		installCommand.repo = repo;
		installCommand.server = server;
		installCommand.installFromServer(Constants.TEST_LIBRARY_NAME, null);
		//
		exec("haxelib", "install", Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION);
		installCommand.installFromServer(Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION);
		//
		HaxelibSetCommand setCommand = new HaxelibSetCommand();
		setCommand.repo = repo;
		actualResult = setCommand.setVersion(Constants.TEST_LIBRARY_NAME, "1.8.1");
		expectedResult = exec("haxelib", "set", Constants.TEST_LIBRARY_NAME, "1.8.1");
		assertEquals(expectedResult, actualResult);
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories should have `" + Constants.TEST_LIBRARY_NAME + "`", expectedResult, actualResult);
	}
}
