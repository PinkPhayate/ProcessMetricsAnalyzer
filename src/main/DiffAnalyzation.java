package main;

import java.io.IOException;
import java.util.List;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.Delta.TYPE;
import lib.FileReading;

public class DiffAnalyzation {
	public void getDifference (String prev_file, String crnt_file) throws IOException {
//		String prev_file = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/prev/single_test.java";
//		String crnt_file = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/single_test.java";
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
