package test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.Test;

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
		String previousFileName = "";
		String currentFileName = "";
		DiffAnalyzer da = new DiffAnalyzer();
		actual = da.getDifference(previousFileName, currentFileName);		
		System.out.println( actual.toString());
	}
	public void testSearchModuleByClassName () {
		ArrayList<Module> targetArrayList = new ArrayList<Module>();
		String expected = "";
		String actual = "";
		try {
			this.method = FileAnalyzer.class.getDeclaredMethod("searchModuleByClassName",String.class);
			Module module = (Module)this.method.invoke(diffAnalyzer, targetArrayList, expected);
			assertEquals(actual, module.getClassName() );
			
			/** bat pattern*/
			expected = "bad request";
			module = (Module)this.method.invoke(diffAnalyzer, targetArrayList, expected);
			assertEquals(expected, module.getClassName() );

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
