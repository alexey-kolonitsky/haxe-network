package org.haxelib.cli;

import org.haxelib.cli.basic.HaxelibInstallCommandTest;
import org.haxelib.cli.basic.HaxelibRemoveCommandTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	HaxelibInstallCommandTest.class,
	HaxelibRemoveCommandTest.class
})

/**
 * Created by akalanitski on 01.10.2017.
 */
public class ProjectFullLifeCycleSuite {

}
