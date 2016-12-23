package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

import main.FileAnalyzer;
import main.Module;

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

		String line = "";
		String  expected = "";

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
	@Test
	public void testExtractClassModule() {
		this.fileAnalyzer = new FileAnalyzer();
		String filename = "";
		this.fileAnalyzer.extractClassModule(filename);
		ArrayList<Module> modules = this.fileAnalyzer.getTestModules();
		for (int index=0;index<modules.size(); index++) {
			TestModule testModule = (TestModule) modules.get(index);
			System.out.println("ClassName: " + testModule.getClassName() );
			System.out.println("BeginningPosition: " + testModule.getBegenningPosition() );
			System.out.println("EndingPosition: " + testModule.getEndingPosition() );
		}
	}
}
