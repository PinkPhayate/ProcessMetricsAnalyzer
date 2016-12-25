package test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.Test;

import lib.FileListGetter;
import main.DiffAnalyzer;
import main.FileAnalyzer;
import main.Module;

public class TestDiffAnalyze {

	Method method;
	DiffAnalyzer diffAnalyzer ;
	protected void setUp(){
		this.diffAnalyzer = new DiffAnalyzer();
		method.setAccessible(true);
	}	

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetDifference () {
		TreeMap<String,Integer> actual = new TreeMap<String,Integer>();
		String previousFileName = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/net/ClientJDBCObjectFactoryImpl.java";
		String currentFileName = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/prev/net/ClientJDBCObjectFactoryImpl.java";
		DiffAnalyzer da = new DiffAnalyzer();
		actual = da.getDifference(previousFileName, currentFileName);		
		System.out.println( actual.toString());
	}
	@Test
	public void testSearchModuleByClassName () {
		ArrayList<Module> targetArrayList = new ArrayList<Module>();
		targetArrayList = this.getStubArrayList();
		String expected = "ClientJDBCObjectFactoryImpl.java";
		try {
			this.method = FileAnalyzer.class.getDeclaredMethod("searchModuleByClassName",String.class);
			Module module = (Module)this.method.invoke(diffAnalyzer, targetArrayList, expected);
			assertEquals(expected, module.getClassName() );
			
			/** bad pattern*/
			String badRequest = "GorillaEngineering.java";
			module = (Module)this.method.invoke(diffAnalyzer, targetArrayList, badRequest);
			assertNull ( module );

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public ArrayList<Module> getStubArrayList() {
		FileListGetter search = new FileListGetter();
		ArrayList<String> currFileStrs = search.getFileList( "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/net" );
		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		return fileAnalyzer.getModules( currFileStrs );
	}
}
