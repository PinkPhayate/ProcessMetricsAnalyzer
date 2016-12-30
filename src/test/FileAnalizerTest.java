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
	@Test
	public void testÉxtractClassName() {

		String line = "public class ClientJDBCObjectFactoryImpl implements ClientJDBCObjectFactory{";
		String  expected = "ClientJDBCObjectFactoryImpl";

		try {
			this.method = FileAnalyzer.class.getDeclaredMethod("extractClassName",String.class);
			String actual = (String)this.method.invoke(fileAnalyzer, line);
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
	public void extrsctClassName () {
		linesJudgedNotClassLogger = new ArrayList<String>();
		String suffix = "cs";
		String cvDirectory = "/Users/phayate/src/TestDate/Glimpse-1.9.2-aspnet";
		FileListGetter search = new FileListGetter(suffix);

		// file list in directory about later version
		ArrayList<String> currFileStrs = search.getFileList(cvDirectory);
		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		ArrayList<Module> currentModules = fileAnalyzer.getModules(currFileStrs);
		try {
			FileWriting.writeFile(linesJudgedNotClassLogger, "linesJudgedNotClass.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	public void testExtractClassName () {
		try {
			FileAnalyzer fileAnalyzer  = new FileAnalyzer();
			ArrayList<String> lines = FileReading.readFile( "linesJudgedNotClass.txt" );
			for ( String line : lines) {
				Method method = FileAnalyzer.class.getDeclaredMethod(
						"confirmBeginningClass", String.class );
				method.setAccessible(true);
				boolean isBeginning = (boolean)method.invoke(fileAnalyzer, line);
				if ( isBeginning) {
					method = FileAnalyzer.class.getDeclaredMethod(
							"extractClassName", String.class );
					method.setAccessible(true);
					String actual = (String)method.invoke(fileAnalyzer, line);
					System.out.println( actual);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
