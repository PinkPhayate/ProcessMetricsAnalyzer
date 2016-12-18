package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Line;

import lib.FileReading;

public class FileAnalyzer {
	private String MODULE_START = "public class";
	public void extractClassModule (String filename) {
		List<String> FileStrs = null;

		try {
			FileStrs = FileReading.readFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// count line of code
		int numberOfLine = 0;
		//extract filename
		filename = this.extractFileName(filename);
		
		// indicator to position
		int begenningPosition = -1;
		int endingPosition = -1;
		
		// indicator to count '{'
		int numStart = 0;
		int numEnd = 0;
		
		// create module instance
		Module module = new Module( filename );
		for(String line: FileStrs) {
			
			//count number of '{'
			numStart++;
			/** Class Module is beginning*/
			if (line.indexOf( MODULE_START) != -1) {
				// put beginning position
				begenningPosition = numberOfLine;
				// extract class name
				String className = this.extractClassName(line);
				module.putClassName(className);
				
				// put class name
//				Module module = new Module( filename, classname );
			}
			if (line.indexOf("{") != -1) {
				numStart += this.countChar(line, "{");
			}
			if (line.indexOf("}") != -1) {
				numEnd += this.countChar(line, "{");
				// if number of '{' is same number of '}' -> class be over
				if (numStart == numEnd) {
					/** Class module was over */
					// put end position
					endingPosition = numberOfLine;
					// initialize
					numStart = 0;
					numEnd = 0;
					// if module has class name
					if(module.getClassName() != "") {
						module.putPositions(begenningPosition, endingPosition);
					}
				}
			}

			numberOfLine++;
		}

	}
	private int countChar(String line, String string) {
		String tmp[] = line.split("string", 0);
		return tmp.length -1;
	}
	private String extractClassName(String line) {
		// TODO Auto-generated method stub
		return null;
	}
	private String extractFileName(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

}
