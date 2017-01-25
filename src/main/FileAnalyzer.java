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
	int numberOfLine = 0;
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
			// reset number of line certainly
			this.numberOfLine = 0;
			this.extractClassModuleRecursively(filename);
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

	/*
	 * convert array to string
	 * */
	private String join ( List<String> array ) {
		String str = "";
		for ( String element: array ) {
			str = str.concat( element );
		}
		return str;
	}
	private String removeSpace ( String line ) {
		String[] array = line.split(" ");
		List<String> list = this.removeTab( Arrays.asList(array) );
		String str = this.join( list );
		return str;
	}
	private int confirmComment ( String line ) {
		//		line = this.removeSpace( line );
		//	when // in line, return 1
		if ( line.indexOf("//") != -1 ) {
			return 1;
		}
		//	when */ in line, return 3
		if ( line.indexOf("*/") != -1 ) {
			return 3;
		}
		//	when /* in line, return 2
		if ( line.indexOf("/*") != -1 ) {
			return 2;
		}
		//	else return 0
		return 0;
	}
	public void extractClassModule (String filename) {
		List<String> fileStrs = null;
		fileStrs = FileReading.readFile(filename);


		// count line of code
		//		int numberOfLine 
		//extract filename
		//		filename = this.extractFileName(filename);

		// indicator to position
		int begenningPosition = -1;
		int endingPosition = -1;

		// indicator to count '{ or }'
		int numStart = 0;
		int numEnd = 0;
		int blockIndicator = 9999;

		// create module instance
		Module module = new Module( filename );
		ArrayList<String> containment = null;
		boolean isContinued = false;
		for(String line: fileStrs) {
			// when line means comment, getting out!
			int statusCode = this.confirmComment( line );
			if ( statusCode == 2 ) {
				isContinued = true;
			}else if( statusCode == 3 ) {
				isContinued = false;				
			}
			// status code==0 and is not continue -> script block  
			if ( statusCode == 1 || isContinued ) {
				// this is comment line
			}
			else {
				/** not comment line*/
				if (containment != null ) {
					containment.add( line );
				}
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
						/** class script starts */
						//						if ( containment == null) {
						containment = new ArrayList<String>();
						containment.add( line );
						module.putClassName(classname);
						blockIndicator = 0;
						//						}
					}
				}
				if (line.indexOf("{") != -1) {
					//count number of '{'
					numStart += this.countChar(line, "{");
					blockIndicator ++;
				}
				if (line.indexOf("}") != -1) {
					//count number of '}'
					numEnd += this.countChar(line, "}");
					blockIndicator --;
					// if number of '{' is same number of '}' -> class be over
					//					if (numStart == numEnd) {
					if ( blockIndicator == 0) {
						/** Class module was over */
						// put end position
						endingPosition = numberOfLine;
						// initialize
						numStart = 0;
						numEnd = 0;
						// when module has class name
						if(module.getClassName() != null ) {
							module.putPositions(begenningPosition, endingPosition);
							// put module to ArrayList
							module.putModuleContainment(containment);
							this.modules.add(module);
							//initialize module
							module = new Module( filename );
							containment = new ArrayList<String>();
						}
					}
				}
			}
			this.numberOfLine++;
		}
	}
	private ArrayList<Module> extractClassModuleRecursively( String filename ) {
		List<String> fileStrs = FileReading.readFile(filename);

		ArrayList<Module> modules = new ArrayList<Module>();
		ArrayList<String>containment = new ArrayList<String>();
		Module module = new Module( filename );
		int blockIndicator = 0;
		int begenningPosition = 0;
		int endingPosition = 0;
		boolean isContinued = false;
		for(int idx=this.numberOfLine;idx<fileStrs.size();idx++) {
			String line = fileStrs.get(idx);
			// when line means comment, getting out!
			int statusCode = this.confirmComment( line );
			if ( statusCode == 2 ) {
				isContinued = true;
			}else if( statusCode == 3 ) {
				isContinued = false;				
			}
			// status code==0 and is not continue -> script block  
			if ( statusCode == 1 || isContinued ) {
				// this is comment line
			}
			else {
				if ( isClassLine ( line ) ) {
					// put beginning position
					begenningPosition = this.numberOfLine;
					if( containment.size() > 0) {
						ArrayList<Module> tmpModules = this.extractClassModuleRecursively(filename);
						modules.addAll( tmpModules );
						idx = this.numberOfLine;
						if (line.indexOf("{") != -1) blockIndicator -= this.countChar(line, "{");
						if (line.indexOf("}") != -1) blockIndicator += this.countChar(line, "}");
					}
					else {
						// extract class name
						String classname = this.extractClassName(line);
						if (classname != null ) {
							/** class script starts */
							module.putClassName(classname);
//							containment.add( line );
							blockIndicator = 0;
						}
					}
				}
				containment.add(line);
				if (line.indexOf("{") != -1){
					// if '{' are in line, not count
					//e.g
					//	argIdx = text.indexOf( '{', argIdx );
					if(line.indexOf("'{'") == -1 ){
						blockIndicator += this.countChar(line, "{");
					}
				}
				if (line.indexOf("}") != -1) {
					blockIndicator -= this.countChar(line, "}");
					if ( blockIndicator == 0) {
						/** Class module was over */
						// put end position
						endingPosition = this.numberOfLine;
						// initialize
						// when module has class name
						if(module.getClassName() != null ) {
							module.putPositions(begenningPosition, endingPosition);
							// put module to ArrayList
							module.putModuleContainment(containment);
							modules.add( module);
						}
						break;
					}
				}
				
			}
			this.numberOfLine++;
		}


		// when looking out end of class, return
		return modules;
	}

	private List<String> goStartLine(List<String> fileStrs) {
		List<String> skippedFileStrs = new ArrayList<String>();
		for(int i=this.numberOfLine; i< fileStrs.size(); i++) {
			skippedFileStrs.add( fileStrs.get(i) );
		}
		return skippedFileStrs;
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
		FileWriting.writeFile(classNameList, saveFileName);
		DiffAnalyzerMain.logger.info("to record" + saveFileName + " has finished");
		return classNameList;
	}

	/** Method for test*/
	public ArrayList<Module> getTestModules () {
		return this.modules;
	}

}
