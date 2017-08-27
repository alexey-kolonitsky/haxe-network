import org.haxelib.command.basics.HaxelibListCommand;
import org.haxelib.command.info.HaxelibConfigCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibConfigCommandTest extends CommandBaseTest {

	@Test
	public void config() throws Exception {
		HaxelibConfigCommand listCommand = new HaxelibConfigCommand();
		listCommand.core = createCore("C:\\alexey\\apps\\haxe\\lib\\");;
		String actualResult = listCommand.config();
		String expectedResult = exec("haxelib", "config");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
