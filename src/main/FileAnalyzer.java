package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


import lib.FileReading;
import lib.FileWriting;

public class FileAnalyzer {
	private String MODULE_START = "public class";
	private ArrayList<Module> modules = new ArrayList<Module>();
    private Logger logger;
    public FileAnalyzer() {
    	//for unit test
    }
    public FileAnalyzer(Logger logger) {
    	this.logger = logger;
    }
    
	public void extractClassModule (String filename) {
		List<String> FileStrs = null;

		try {
			FileStrs = FileReading.readFile(filename);
		} catch (IOException e) {
			logger.warning("IOException occured");
			return ;
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
						// put module to ArrayList
						this.modules.add(module);
						//initialize module
						module = new Module( filename );
					}
				}
			}
			numberOfLine++;
		}
		logger.info("All file has been read");
	}
	private int countChar(String line, String str) {
		int count = 0;
		int fromIndex = 0;
		while(line.indexOf(str, fromIndex)!= -1) {
			count ++;
			fromIndex = line.indexOf(str, fromIndex);
		}
		return count;
	}
	private String extractClassName(String line) {
		String[] array = line.split(" ");
		for(int i=0; i<array.length; i++) {
			if (array[i].equals("class")) {
				return array[i+1];
			}
		}
		return null;
	}
	private String extractFileName(String filename) {
		String[] array = filename.split("/");
		return array[array.length - 1];
	}
	public ArrayList<Module> getModules(ArrayList<String> module) {
		for(String filename: module) {
			this.extractClassModule(filename);
		}
		
		return this.modules;
	}
	
	public ArrayList<String> saveModules(String saveFileName) {
		ArrayList<String> classNameList = new ArrayList<String> ();
		// save class name only
		for(Module module: this.modules) {
			classNameList.add( module.getClassName() );
		}
		try {
			FileWriting.writeFile(classNameList, saveFileName);
			logger.info("to record" + saveFileName + " has finished");
		} catch (IOException e) {
			logger.warning("IOException occured");
			e.printStackTrace();
		}
		return classNameList;
	}
}
