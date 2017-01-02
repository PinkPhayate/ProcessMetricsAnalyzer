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
	private final String[] reservedWords = {
			"static",
			"abstract",
			"partial",
			"sealed"
	};
	private final List<String> MODULE_START = Arrays.asList(
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
	private String removeSpace ( String line ) {
		
	}
	private int confirmComment ( String line ) {
		//	when // in line, return 0
		//	when */ in line, return 1
		//	when /* in line, return 2
		//	else return 0
	}
	public void extractClassModule (String filename) {
		List<String> fileStrs = null;
		fileStrs = FileReading.readFile(filename);

		
		// count line of code
		int numberOfLine = 0;
		//extract filename
//		filename = this.extractFileName(filename);
		
		// indicator to position
		int begenningPosition = -1;
		int endingPosition = -1;
		
		// indicator to count '{ or }'
		int numStart = 0;
		int numEnd = 0;
		
		// create module instance
		Module module = new Module( filename );
		for(String line: fileStrs) {
			// when line means comment, getting out!
			if ( line.indexOf( "//") != -1 ) {
				// comment line
			}else {
				/** Class Module is beginning*/
				if ( isClassLine ( line ) ) {
					// put beginning position
					begenningPosition = numberOfLine;
					// extract class name
					String classname = this.extractClassName(line);
					if (classname == null ) {
						String errorMsg = filename+" has null classname\n"+line;
						DiffAnalyzerMain.logger.warning( errorMsg );
						// when test
						if ( FileAnalizerTest.linesJudgedNotClassLogger != null ) {
							FileAnalizerTest.linesJudgedNotClassLogger.add( errorMsg );
						}
					}else {
						module.putClassName(classname);
					}
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
						if(module.getClassName() != null ) {
							module.putPositions(begenningPosition, endingPosition);
							// put module to ArrayList
							this.modules.add(module);
							//initialize module
							module = new Module( filename );
						}
					}
				}
			}
			numberOfLine++;
		}
	}

	private String extractClassName(String line) {
		String[] array = line.split(" ");
		List<String> list = this.removeTab( Arrays.asList(array) );

		int index = list.indexOf("class");
		if ( index+1 < list.size() ) {
			return list.get(index + 1);
		}
		return null;
	}
	private boolean isClassLine ( String line ) {
		String[] array = line.split(" ");
		List<String> list = this.removeTab( Arrays.asList(array) );
		return this.isClassLine( list );
	}

	/** confirm that line if it is beginning of class */
	private boolean isClassLine ( List<String> list ) {
		// remove space and null

		// there are 'class' in array
		int CLASS = list.indexOf("class");        
		int PUBLIC = list.indexOf("public");        
        if( CLASS == -1)	return false;
        
        // there are reserved word in array
        for ( String rw : this.reservedWords ) {
        	int position = list.indexOf( rw );
        	if ( position != -1 ) return true;				
        }
        if( PUBLIC > -1) {
        	return true;
        }

		return false;
	}
	
	
	private List<String> removeTab(List<String> list) {
		List<String> modifiedList = new ArrayList<String>();
		for ( String element: list) {
			int num = element.indexOf("\t");
			while ( num != -1 ) {
				element = element.replaceAll("\t", "");
				num = element.indexOf("\t");				
			}
			if ( element.length() > 0) {
				modifiedList.add(element);
			}
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
	public ArrayList<String> getFilenameList () {
		ArrayList<String> filenameList = new ArrayList<String> ();
		// save class name only
		for ( Module module : this.modules) {
			filenameList.add( module.getFileName() );
		}
		return filenameList;
	}
	
	public ArrayList<String> saveModules(String saveFileName) {
		ArrayList<String> classNameList = new ArrayList<String> ();
		// save class name only
		for ( Module module : this.modules) {
			classNameList.add( module.getFileName() +": "+module.getClassName() );
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
