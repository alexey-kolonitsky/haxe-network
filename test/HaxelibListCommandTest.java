import org.haxelib.command.basics.HaxelibListCommand;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class HaxelibListCommandTest extends CommandBaseTest {

	@Test
	public void list() throws Exception {
		HaxelibListCommand listCommand = new HaxelibListCommand();
		listCommand.core = createCore("C:\\alexey\\apps\\haxe\\lib\\");;
		String actualResult = listCommand.list();
		String expectedResult = exec("haxelib", "list");
		assertEquals("The files differ!", expectedResult, actualResult);
	}
}
