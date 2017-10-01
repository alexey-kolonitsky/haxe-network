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
public class HaxelibInstallCommandTest extends CommandBaseTest {

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
	public void install1_ByName() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		exec("haxelib", "install", Constants.TEST_LIBRARY_NAME);
		HaxelibInstallCommand installCommand = new HaxelibInstallCommand();
		installCommand.repo = repo;
		installCommand.server = server;
		installCommand.installFromServer(Constants.TEST_LIBRARY_NAME, null);
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories should have `" + Constants.TEST_LIBRARY_NAME + "`", expectedResult, actualResult);
	}

	@Test
	public void install2_ByNameAndVersion() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		String actualResult = listCommand.list();
		String expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		exec("haxelib", "install", Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION);
		HaxelibInstallCommand installCommand = new HaxelibInstallCommand();
		installCommand.repo = repo;
		installCommand.server = server;
		installCommand.installFromServer(Constants.TEST_LIBRARY_NAME, Constants.TEST_LIBRARY_VERSION);
		// Fix for original behaviour https://github.com/HaxeFoundation/haxelib/issues/396
		HaxelibSetCommand setCommand = new HaxelibSetCommand();
		setCommand.repo = repo;
		setCommand.setVersion(Constants.TEST_LIBRARY_NAME, "1.8.7");
		//
		actualResult = listCommand.list();
		expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories should have `" + Constants.TEST_LIBRARY_NAME + " " + Constants.TEST_LIBRARY_VERSION + "`", expectedResult, actualResult);
	}

	@Test
	public void install3_installedLibrary() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		String actualResult = listCommand.list();
		String expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		expectedResult = exec("haxelib", "install", Constants.TEST_LIBRARY_NAME);
		HaxelibInstallCommand installCommand = new HaxelibInstallCommand();
		installCommand.repo = repo;
		installCommand.server = server;
		actualResult = installCommand.installFromServer(Constants.TEST_LIBRARY_NAME, null);
		//
		assertEquals("Both repositories should have `" + Constants.TEST_LIBRARY_NAME + " " + Constants.TEST_LIBRARY_VERSION + "`", expectedResult, actualResult);
	}

	@Test
	public void install4_unknownLibrary() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.repo = repo;
		//
		String actualResult = listCommand.list();
		String expectedResult = exec("haxelib", "list");
		assertEquals("Both repositories sould be empty!", expectedResult, actualResult);
		//
		expectedResult = exec("haxelib", "install", "qweasdzxc");
		HaxelibInstallCommand installCommand = new HaxelibInstallCommand();
		installCommand.repo = repo;
		installCommand.server = server;
		actualResult = installCommand.installFromServer("qweasdzxc", null);
		//
		assertEquals("Both repositories should have `" + Constants.TEST_LIBRARY_NAME + "`", expectedResult, actualResult);
	}
}
