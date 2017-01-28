package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import difflib.StringUtills;
import lib.FileReading;
import lib.FileWriting;
import property.CsWords;
import property.JavaWords;
import test.FileAnalizerTest;

public class FileAnalyzer {
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

	private int numberOfLine = 0;
	private ArrayList<Module> modules = new ArrayList<Module>();

	private ArrayList<String> reservedWords = new ArrayList<String>();
	public FileAnalyzer() {
		//for unit test
	}
	public FileAnalyzer(String language) {
		switch(language) {
		case "java":
			for (JavaWords w : JavaWords.values()) this.reservedWords.add( w.getWord() );
		case "cs":
			for (CsWords w : CsWords.values()) this.reservedWords.add( w.getWord() );		
		}
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
	private int confirmComment ( String line ) {
		String trmmedLine = line.trim();
		int position = line.indexOf("//");
		if (position != -1) {
			position = trmmedLine.indexOf("//");
			if (position == 0) {
				return 1;
			}
		}
		int beginningPosition = trmmedLine.indexOf("/*");
		int endingPosition = trmmedLine.indexOf("*/");
		
		if(		
				endingPosition != -1 &&
				endingPosition+2 < trmmedLine.length()
//				endingPosition != -1 &&
//				beginningPosition < endingPosition &&
//				0 < beginningPosition  &&
//				endingPosition < trmmedLine.length() -1
				) {
//		eg.)	public hoge /**huga*/ {		
//				this line is not comment line.
//				but /** public hoge {*/
//				this is comment line			
			return 0;			
		}

		//	when */ in line, return 3
		if ( endingPosition != -1 ) {
			return 3;
		}
		//	when /* in line, return 2
		if ( beginningPosition != -1 ) {
			return 2;
		}
		//	else return 0
		return 0;
	}
	private String removeCommentBlock (String line) {
		return line;

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
				List<String> tmp = this.splitLine(line);
				if ( isClassLine ( tmp ) ||
						isInterface(tmp)
						) {
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
	private void extractClassModuleRecursively( String filename ) {
		List<String> fileStrs = FileReading.readFile(filename);

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
			if ( statusCode > 0 || isContinued ) {
				// this is comment line
//				System.out.println(line);	// debug
			}
			else {
				
				List<String> tmp = this.splitLine(line);
				if ( isClassLine( tmp ) ||
						isInterface( tmp ) ) {
					// put beginning position
					begenningPosition = this.numberOfLine;
					if( containment.size() > 0) {
						this.extractClassModuleRecursively(filename);
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
				if ( this.isBorder(line, "{") ) {
					blockIndicator += this.countChar(line, "{");
				}
				if ( this.isBorder(line, "}") ) {
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
							this.modules.add( module);
						}
						break;
					}
				}
				
			}
			this.numberOfLine++;
		}
		// when looking out end of class, return
	}
	private String enoding( String line) {
		line = line.replaceAll(" ", "");
		// NOTE: { or } are require escape via \\  
		line = line.replaceAll("'.*\\{.*'", "");
		line = line.replaceAll("\".*\\{.*\"", "");
		line = line.replaceAll("'\\}.*\'", "");
		line = line.replaceAll("\".*\\}.*\"", "");
		return line;
	}

	private boolean isBorder(String line, String target) {
		line = this.enoding(line);

		if( line.indexOf(target) != -1) {
			return true;
		}
		return false;
	}
	private void addModulesAll(ArrayList<Module> modules2) {
		for( Module module : modules2) {
			this.modules.add(module);
		}
	}
	private List<String> goStartLine(List<String> fileStrs) {
		List<String> skippedFileStrs = new ArrayList<String>();
		for(int i=this.numberOfLine; i< fileStrs.size(); i++) {
			skippedFileStrs.add( fileStrs.get(i) );
		}
		return skippedFileStrs;
	}
	private String extractClassName(String line) {
		List<String> list = this.splitLine(line);

		int index = list.indexOf("class");
		if ( index+1 < list.size() && index != -1) {
			return list.get(index + 1);
		}
		// interface ?
		index = list.indexOf("interface");
		if ( index+1 < list.size() && index != -1) {
			return list.get(index + 1);
		}

		return null;
	}
	private List<String> splitLine ( String line ) {
		line = line.replaceAll("\t", " ");
		String[] array = line.split(" ");
//		List<String> list = this.removeTab( Arrays.asList(array) );
		List<String> list = Arrays.asList(array);
		list = removeElementOnlySpace(list);
		return list;
	}

	private List<String> removeElementOnlySpace(List<String> list) {
		List<String> revicedList = new ArrayList<String> ();
		for(String e: list) {			
			if( 0 < e.length() ) revicedList.add(e);
		}
		return revicedList;
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
		if( PUBLIC > -1)  return true;

		// normal class like "class Hoge {"
		if (CLASS == 0)	return true;
		return false;
	}

	// adapt version 2.0~
	private boolean isInterface(List<String> list) {
		int INTERFACE = list.indexOf("interface");
		int PUBLIC = list.indexOf("public");
		if( PUBLIC == 0 && INTERFACE == 1)	return true;
		// normal interface like "interface Hoge {"
		if (INTERFACE == 0)	return true;

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
