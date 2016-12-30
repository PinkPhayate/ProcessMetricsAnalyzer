package test;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

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
}
