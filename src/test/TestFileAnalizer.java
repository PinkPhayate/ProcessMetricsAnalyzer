package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import main.FileAnalyzer;

public class TestFileAnalizer {
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

		String filename = "";
		String  expected = "";

		try {
			this.method = FileAnalyzer.class.getDeclaredMethod("extractClassName",String.class);
			String actual = (String)this.method.invoke(fileAnalyzer, filename);
			assertEquals(expected, actual);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTextractFileName() {
		String filename = "";
		String expected = "";
		try {
			this.method = FileAnalyzer.class.getDeclaredMethod("extractFileName",String.class);
			String actual = (String)this.method.invoke(fileAnalyzer, filename);
			assertEquals(expected, actual);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
