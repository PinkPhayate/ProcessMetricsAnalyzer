package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import difflib.Delta;
import difflib.Delta.TYPE;
import difflib.DiffUtils;
import difflib.Patch;
import lib.FileReading;

public class DifflibTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testDifflib() throws IOException {
		ArrayList<String> prevs = FileReading.readFile(
				"/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/single_test.java");
		ArrayList<String> currs = FileReading.readFile(
				"/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/prev/single_test.java");
		
		Patch diff = DiffUtils.diff(prevs, currs);
		List<Delta> deltas = diff.getDeltas();
		int cntNew = 0;
		int cntChange = 0;	
		int cntDelete = 0;

		for (Delta delta : deltas) {
			TYPE type = delta.getType();
			System.out.println( type.toString() );
			if (type.toString() == "INSERT") {
				cntNew++;

			}
			if (type.toString() == "CHANGE") {
				cntChange++;
			}
			if (type.toString() == "DELETE") {
				cntDelete++;
			}
		}

	}

}
