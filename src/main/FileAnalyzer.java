package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import difflib.StringUtills;
import lib.FileReading;
import lib.FileWriting;
import test.FileAnalizerTest;

public class FileAnalyzer {
	//	private String MODULE_START = "public class";
	private List<String> MODULE_START = Arrays.asList(
			"public class",
			"public static class",
			"public abstract class",
			"public partial class",
			"public sealed class"
//			"internal class",
//			"internal static class",
//			"private class",
//			"partial class",
//			"sealed partial class"
			); 
	
	private ArrayList<Module> modules = new ArrayList<Module>();
    public FileAnalyzer() {
    	//for unit test
    }
	public ArrayList<Module> getModules(ArrayList<String> module) {
		for(String filename: module) {
			this.extractClassModule(filename);
		}
		this.removeNullClass();
		return this.modules;
	}
	/**if using iteration, error occurs named "ConcurrentModificationException"*/
	private void removeNullClass() {
		for ( int i=0; i<this.modules.size(); i++) {
			Module module = modules.get( i );
			if ( module.getClassName() == null ) {
				this.modules.remove( i );
			}
		}
	}
	public void extractClassModule (String filename) {
		List<String> fileStrs = null;

		try {
			fileStrs = FileReading.readFile(filename);
		} catch (IOException e) {
			DiffAnalyzerMain.logger.warning( filename + "IOException occured");
			return ;
		}
		
		// count line of code
		int numberOfLine = 0;
		//extract filename
//		filename = this.extractFileName(filename);
		
		// indicator to position
		int begenningPosition = -1;
		int endingPosition = -1;
		
		// indicator to count '{'
		int numStart = 0;
		int numEnd = 0;
		
		// create module instance
		Module module = new Module( filename );
		for(String line: fileStrs) {
			
			/** Class Module is beginning*/
			if ( this.confirmBeginningClass(line) ) {
				// put beginning position
				begenningPosition = numberOfLine;
				// extract class name
				String className = this.extractClassName(line);
				if(className == null
						&& FileAnalizerTest.linesJudgedNotClassLogger != null) {
//					FileAnalizerTest.linesJudgedNotClassLogger.add(filename +":"+line);
					FileAnalizerTest.linesJudgedNotClassLogger.add( line );
				}else if ( className == null ) {
					DiffAnalyzerMain.logger.warning( filename +" has null classname" );
				}
				else {
					module.putClassName(className);
				}
				
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
	}
	private boolean confirmBeginningClass ( String line ) {
		for ( String formal: this.MODULE_START) {
			if ( line.indexOf( formal ) != -1) {
				return true;
			}
		}

		return false;
	}

	private String extractClassName(String line) {
		// if line means comment, out
		if( line.indexOf( "//") != -1) {
			return null;
		}
		String[] array = line.split(" ");
		if (!this.isClassLine( array ) ) {
			return null;
		}
		for(int i=0; i<array.length-1; i++) {
			if (array[i].equals("class")) {
				return array[i+1];
			}
		}
		return null;
	}
	/** confirm that line if it is beginning of class */
	private boolean isClassLine (String[] array) {
		List<String> list = Arrays.asList(array);
		list = this.removeExtracts(list);
		int PUBLIC = list.indexOf("public");
		int CLASS = list.indexOf("class");
        int STATIC = list.indexOf("static");
        int ABSTRACT = list.indexOf("abstract");
        int PARTIAL = list.indexOf("partial");
        int SEALED = list.indexOf("sealed");
        
        
		/** constrain to public class line*/
		if( PUBLIC == -1)	return false;
		if( CLASS == -1)	return false;
		if( PUBLIC+1 == CLASS)	return true;
		if( PUBLIC+2 == CLASS) {
			if( PUBLIC+1 == STATIC )	return true;
			if( PUBLIC+1 == ABSTRACT )	return true;
			if( PUBLIC+1 == PARTIAL )	return true;
			if( PUBLIC+1 == SEALED )	return true;			
		}

		/** constrain to public static class line*/		
		if( STATIC == -1 || ABSTRACT == -1 ||
				PARTIAL == -1 || SEALED == -1 ) {
			return false;
		}
		if( PUBLIC+2 == CLASS)	return true;

		return false;
	}
	
	private List<String> removeExtracts(List<String> list) {
		List<String> modifiedList = new ArrayList<String>();
		for ( String element: list) {
			modifiedList.add( element.replaceAll("\t", ""));
		}
		return modifiedList;
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

	private String extractFileName(String filename) {
		String[] array = filename.split("/");
		return array[array.length - 1];
	}
	
	public ArrayList<String> saveModules(String saveFileName) {
		ArrayList<String> classNameList = new ArrayList<String> ();
		// save class name only
		for ( Module module : this.modules) {
			classNameList.add( module.getFileName() +": "+module.getClassName() );
		}
//		for ( int i=0; i<this.modules.size(); i++) {
//			Module module = modules.get( i );
//			classNameList.add( module.getClassName() );
//		}
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
