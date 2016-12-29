package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.DiffAnalyzerMain;

public class DiffAnalyzerMainTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testIntegration () {
//		expect: there are 2 new files	
		String[] args = { "/Users/phayate/src/TestDate/",
							"curr", "prev", "cs" };
		DiffAnalyzerMain .main(args);
	}

}
