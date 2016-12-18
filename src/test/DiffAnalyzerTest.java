package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.DiffAnalyzer;
import main.Module;

import org.junit.Test;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.Delta.TYPE;
import lib.FileListGetter;
import lib.FileReading;

public class DiffAnalyzerTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	public void testGetDifference () {
		DiffAnalyzer diffAnalyzer = new DiffAnalyzer();
		String prev_file = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/prev/single_test.java";
		String crnt_file = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/single_test.java";
		diffAnalyzer.getDifference(prev_file, crnt_file);
	}
	private static void printFileList(File[] files) {
		for (File file: files) {
			System.out.println(file);

		}
	}
	public static void testDiffUtil () throws IOException {

		String prev_file = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/prev/single_test.java";
		String crnt_file = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/single_test.java";
		List<String> prevFileStrs = FileReading.readFile(prev_file);
		List<String> currentFileStrs = FileReading.readFile(crnt_file);

		Patch diff = DiffUtils.diff(prevFileStrs, currentFileStrs);

		List<Delta> deltas = diff.getDeltas();
		for (Delta delta : deltas) {
			TYPE type = delta.getType();
			System.out.println(type);
			Chunk oc = delta.getOriginal();
			System.out.printf("del: position=%d, lines=%s%n", oc.getPosition(), oc.getLines());
			Chunk rc = delta.getRevised();
			System.out.printf("add: position=%d, lines=%s%n", rc.getPosition(), rc.getLines());
		}

	}

}
