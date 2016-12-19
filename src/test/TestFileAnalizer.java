package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import main.FileAnalyzer;

public class TestFileAnalizer {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testÉxtractClassName() {
		
		String filename = "";
		String  expected = "";
		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		try {
			Method method = FileAnalyzer.class.getDeclaredMethod("extractClassName",String.class);
			method.setAccessible(true);
			String actual = (String)method.invoke(fileAnalyzer, filename);
			assertEquals(expected, actual);
		}catch (Exception e) {
			e.printStackTrace();			
		}
	}

}
