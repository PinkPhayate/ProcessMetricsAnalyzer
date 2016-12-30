package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import lib.FileListGetter;
import lib.FileWriting;
import main.DiffAnalyzerMain;

public class DiffAnalyzerMainTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testIntegration () {
		String cvDirectory = "/Users/phayate/src/TestDate/";

//		expect: there are 2 new files	
		String[] args = { cvDirectory,
							"Glimpse-1.9.2-aspnet", "Glimpse-1.7.3-ado", "cs" };
		DiffAnalyzerMain .main(args);
		int actual = DiffAnalyzerMain .originRecord.size() -1;
		
		FileListGetter search = new FileListGetter("cs");
		ArrayList<String> currFileStrs = search.getFileList(cvDirectory);
//		try {
//			FileWriting.writeFile(currFileStrs, "contain list.txt");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		int expected = currFileStrs.size();
		System.out.println( "Actual: "+actual );
		System.out.println( "Expected: "+expected );
		assertEquals(expected, actual);
		
		

	}

}
