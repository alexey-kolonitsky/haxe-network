
import org.haxelib.core.HaxelibRepository;
import org.haxelib.core.HaxelibException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by akalanitski on 27.08.2017.
 */
public class CommandBaseTest {


	public HaxelibRepository createCore(String repositoryPath) {
		HaxelibRepository core = new HaxelibRepository(repositoryPath);
		try {
			core.scanRepository();
		} catch (HaxelibException exception) {
			System.out.println("ERROR: " + exception);
		} catch (IOException ioException) {
			System.out.println("IO ERROR: " + ioException);
		}
		return core;
	}

	public String exec(String... arguments) throws IOException {
		List t = Arrays.asList(arguments);
		ArrayList<String> list = new ArrayList<String>(t);
		Process process = new ProcessBuilder(list).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		String expectedResult = "";
		while ((line = br.readLine()) != null) {
			expectedResult += line + "\n";
		}
		return expectedResult;
	}
}
