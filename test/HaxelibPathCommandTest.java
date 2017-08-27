import org.haxelib.command.info.HaxelibConfigCommand;
import org.haxelib.command.info.HaxelibPathCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibPathCommandTest extends CommandBaseTest {

	@Test
	public void path() throws Exception {
		HaxelibPathCommand listCommand = new HaxelibPathCommand();
		listCommand.core = createCore("C:\\alexey\\apps\\haxe\\lib\\");;
		String actualResult = listCommand.path("utest");
		String expectedResult = exec("haxelib", "path", "utest");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
