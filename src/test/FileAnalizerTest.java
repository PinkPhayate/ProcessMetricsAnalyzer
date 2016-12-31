package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

import lib.FileListGetter;
import lib.FileReading;
import lib.FileWriting;
import main.FileAnalyzer;
import main.Module;

public class FileAnalizerTest {
	Method method;
	FileAnalyzer fileAnalyzer ;
	protected void setUp(){
		this.fileAnalyzer = new FileAnalyzer();
		method.setAccessible(true);
	}	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	/** unit test */
	@Test
	public void testÉxtractClassName() {

		String line = "public  class  ClientJDBCObjectFactoryImpl implements ClientJDBCObjectFactory{";
		String  expected = "ClientJDBCObjectFactoryImpl";

		try {
			FileAnalyzer fileAnalyzer = new FileAnalyzer(); 
			Method method = FileAnalyzer.class.getDeclaredMethod("extractClassName",String.class);
			method.setAccessible(true);
			String actual = (String)method.invoke(fileAnalyzer, line);
			assertEquals(expected, actual);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTextractFileName() {
		String filename = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/net/ClientJDBCObjectFactoryImpl.java";
		String expected = "ClientJDBCObjectFactoryImpl.java";
		try {
			this.method = FileAnalyzer.class.getDeclaredMethod("extractFileName",String.class);
			String actual = (String)this.method.invoke(fileAnalyzer, filename);
			assertEquals(expected, actual);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testExtractClassModule() {
		this.fileAnalyzer = new FileAnalyzer();
		String filename = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/net/ClientJDBCObjectFactoryImpl.java";
		this.fileAnalyzer.extractClassModule(filename);
		ArrayList<Module> modules = this.fileAnalyzer.getTestModules();
		
		Module module = modules.get(0);
		assertEquals(64, module.getBegenningPosition());
		assertEquals(465, module.getEndingPosition());
	}
	public static ArrayList<String> linesJudgedNotClassLogger ;
	@Test
	public void testExtractClassModule2 () {
		/**
		 * This test make you confirm reject line or not about begin of class
		 * */
		linesJudgedNotClassLogger = new ArrayList<String>();
		
		ArrayList<String> currFileStrs = this.getFileListFromTestData ();

		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		fileAnalyzer.getModules(currFileStrs);
		fileAnalyzer.saveModules( "current-modules.txt" );
		// reject
		try {
			FileWriting.writeFile(linesJudgedNotClassLogger, "linesJudgedNotClass.txt");
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@Test
	public void examineClassLineNotExtracted () {
		
		FileAnalyzer fileAnalyzer  = new FileAnalyzer();
		try {
			ArrayList<String> lines = FileReading.readFile( "linesJudgedNotClass.txt");
			for ( int index=1;index<lines.size();index+=2 ) {
				Method method = FileAnalyzer.class.getDeclaredMethod(
						"extractClassName", String.class );
				method.setAccessible(true);
				String actual = (String)method.invoke(fileAnalyzer, lines.get(index));
			}
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testConfirmBeginningClass () {
		try {
			FileAnalyzer fileAnalyzer  = new FileAnalyzer();
			ArrayList<String> lines = FileReading.readFile( "linesJudgedNotClass.txt" );
			for ( String line : lines) {
				Method method = FileAnalyzer.class.getDeclaredMethod(
						"confirmBeginningClass", String.class );
				method.setAccessible(true);
				boolean actual = (boolean)method.invoke(fileAnalyzer, line);
				if ( !actual) {
					if ( line.indexOf( "//" ) == -1 ) {
						System.out.println( line );
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testExtractClassNameNotExtracted () {
		/**
		 * after getting list named classLineNotExtracted
		 * by implementing testStep2 in DiffAnalyzerMainTest 
		 * */
		try {
			FileAnalyzer fileAnalyzer  = new FileAnalyzer();
			ArrayList<String> lines = FileReading.readFile( "classLineNotExtracted.txt" );
			for ( String line : lines) {
					method = FileAnalyzer.class.getDeclaredMethod(
							"extractClassName", String.class );
					method.setAccessible(true);
					String actual = (String)method.invoke(fileAnalyzer, line);
//					System.out.println( actual);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<String> moduleListLogger ;
	@Test
	public void testClassName() {
		moduleListLogger = new ArrayList<String> ();
		
		// get file list in directory
		String suffix = "cs";
		String cvDirectory = "/Users/phayate/src/TestDate/Glimpse-1.9.2-aspnet";
		ArrayList<String> currFileStrs = this.getFileList(suffix, cvDirectory);

		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		fileAnalyzer.getModules(currFileStrs);
		fileAnalyzer.saveModules( "current-modules.txt" );
		try {
			FileWriting.writeFile(moduleListLogger, "moduleListLogger.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	private ArrayList<String> getFileList (String suffix, String directory) {
		FileListGetter search = new FileListGetter(suffix);
		return search.getFileList( directory );

	}
	private ArrayList<String> getFileListFromTestData () {
		String suffix = "cs";
		String directory = "/Users/phayate/src/TestDate/Glimpse-1.9.2-aspnet";
		FileListGetter search = new FileListGetter(suffix);
		return search.getFileList( directory );

	}

}
