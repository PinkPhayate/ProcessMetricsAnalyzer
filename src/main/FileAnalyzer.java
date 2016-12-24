package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import lib.FileReading;
import lib.FileWriting;

public class FileAnalyzer {
	private String MODULE_START = "public class";
	private ArrayList<Module> modules = new ArrayList<Module>();
    public FileAnalyzer() {
    	//for unit test
    }
	public ArrayList<Module> getModules(ArrayList<String> module) {
		for(String filename: module) {
			this.extractClassModule(filename);
		}
		
		return this.modules;
	}
	
	public void extractClassModule (String filename) {
		List<String> FileStrs = null;

		try {
			FileStrs = FileReading.readFile(filename);
		} catch (IOException e) {
			DiffAnalyzerMain.logger.warning("IOException occured");
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
				//count number of '{'
				numStart += this.countChar(line, "{");
			}
			if (line.indexOf("}") != -1) {
				//count number of '}'
				numEnd += this.countChar(line, "}");
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
		DiffAnalyzerMain.logger.info("All file has been read");
	}
	/**
	 * @param line: line
	 * @param str : target signature
	 * */
	private int countChar(String line, String str) {
		int count = 0;
		int fromIndex = 0;
		while(line.indexOf(str)!= -1) {
			count ++;
			fromIndex = line.indexOf(str);
			line = line.substring( fromIndex+1 );
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
	
	public ArrayList<String> saveModules(String saveFileName) {
		ArrayList<String> classNameList = new ArrayList<String> ();
		// save class name only
		for(Module module: this.modules) {
			classNameList.add( module.getClassName() );
		}
		try {
			FileWriting.writeFile(classNameList, saveFileName);
			DiffAnalyzerMain.logger.info("to record" + saveFileName + " has finished");
		} catch (IOException e) {
			DiffAnalyzerMain.logger.warning("IOException occured");
			e.printStackTrace();
		}
		return classNameList;
	}
	
	/** Method for test*/
	public ArrayList<Module> getTestModules () {
		return this.modules;
	}

}
