package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import lib.FileReading;

public class FileReadingTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	@Test
	public void testReadFile() {
		String filename = "/Users/phayate/src/Eclipse-Java/ProcessMetricsAnalyzer/test-data/curr/net/ClientJDBCObjectFactoryImpl.java";
		
		try {
			ArrayList<String> containment = FileReading.readFile(filename, 64, 465);
			System.out.println( containment.toString());
			assertTrue( true );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
