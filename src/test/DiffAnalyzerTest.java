package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import main.DiffAnalyzer;
import main.DiffAnalyzerMain;
import main.FileAnalyzer;
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
	@Test
	public void testSearchModuleByFilePath () {

		ArrayList<Module> currentModules =
				this.getStubModules("/Users/phayate/src/TestDate/Glimpse-1.9.2-aspnet");
		ArrayList<Module> prevModules =
				this.getStubModules("/Users/phayate/src/TestDate/Glimpse-1.7.3-ado");
		Module module = currentModules.get(0);
		try {
			DiffAnalyzer diffAnallyzer = new DiffAnalyzer();
			Method method = FileAnalyzer.class.getDeclaredMethod(
					"searchModuleByFilePath", ArrayList.class, Module.class );
			method.setAccessible(true);
			Module prevModule = (Module)method.invoke(diffAnallyzer, prevModules, module );
			assertEquals(module.getClassName(), prevModule.getClassName());
		}catch (Exception e) {

		}

	}
	@Test
	public void testSearchModuleByFilePath2 () {
		/*
		 * this test executes all file 
		 * */
		ArrayList<Module> currentModules =
				this.getStubModules("/Users/phayate/src/TestDate/Glimpse-1.9.2-aspnet");
		ArrayList<Module> prevModules =
				this.getStubModules("/Users/phayate/src/TestDate/Glimpse-1.7.3-ado");
		for ( Module module: currentModules) {
			try {

				DiffAnalyzer diffAnallyzer = new DiffAnalyzer();
				Method method = FileAnalyzer.class.getDeclaredMethod(
						"searchModuleByFilePath", ArrayList.class, Module.class );
				method.setAccessible(true);
				Module prevModule = (Module)method.invoke(diffAnallyzer, prevModules, module );
				assertEquals(module.getClassName(), prevModule.getClassName());
			}catch (Exception e) {

			}

		}
	}

	@Test
	public void testCompareTwoVersion () {
		ArrayList<Module> currentModules =
				this.getStubModules("/Users/phayate/src/TestDate/Glimpse-1.9.2-aspnet");
		ArrayList<Module> previousModules =
				this.getStubModules("/Users/phayate/src/TestDate/Glimpse-1.7.3-ado");

		DiffAnalyzer diffAnalyzer = new DiffAnalyzer();
		diffAnalyzer.compareTwoVersion(currentModules, previousModules);
	}
	private ArrayList<Module> getStubModules ( String directoryName) {		
		FileListGetter search = new FileListGetter("cs");
		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		ArrayList<String> prevfileStrs = search.getFileList(directoryName);
		fileAnalyzer = new FileAnalyzer();
		ArrayList<Module> modules = fileAnalyzer.getModules(prevfileStrs);
		return modules;


	}
	

}
