package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import lib.FileListGetter;

public class FileListGetterTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetFileList () {
		
		FileListGetter search = new FileListGetter("cs");
		String cvDirectory = "/Users/phayate/src/TestDate/curr";
		int expected = 7;
		ArrayList<String> currFileStrs = search.getFileList(cvDirectory);
		int actual = currFileStrs.size();
		assertEquals(expected, actual);

	}
	

}
