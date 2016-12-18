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
	public void testDiffUtils () throws IOException {

		String path = "/Users/phayate/src/ApacheDerby/";
		String currentVersion = "10.12";
		String previousVersion = "10.11";

		String listFileName = "diff_fileDirectory_containment.txt";
		FileListGetter search = new FileListGetter();
		File[] files = search.listFiles( path+currentVersion, null );
		files = search.listFiles(path, "*.java");
		ArrayList<String> currFileStrs = search.printFileList(files);
		search.clear();

		files = search.listFiles(path+previousVersion, null);
		files = search.listFiles(path, "*.java");		
		ArrayList<String> prevFileStrs = search.printFileList(files);
		search.clear();

		ArrayList<String> metricsList = new ArrayList<String>(); 
		DiffAnalyzer diffAnalyzation = new DiffAnalyzer();
		/**find same file from previous version*/
		for ( String currFileStr: currFileStrs ) {

			Module module = new Module( currentVersion );
			int index = prevFileStrs.indexOf(currFileStr);
			/**get process metrics*/
			if (index != -1) {
				String prevFileStr = prevFileStrs.get ( index );
				ArrayList<String> currFileCont = FileReading.readFile( currFileStr );
				ArrayList<String> prevFileCont = FileReading.readFile( prevFileStr );
				diffAnalyzation.getDifference( currFileStr, prevFileStr );
			}
			/** if there was no file in previous file, set 0 to all*/
			else {
				System.out.println("This is new file!");
			}
		}
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
