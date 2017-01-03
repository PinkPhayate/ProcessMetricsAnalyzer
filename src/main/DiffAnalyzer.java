package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.Delta.TYPE;
import lib.FileReading;

public class DiffAnalyzer {
	//	private ArrayList<String> prevFileStrs = null;
	//	private ArrayList<String> currentFileStrs = null;

	public TreeMap<String,Integer> getDifference (String prev_file, String crnt_file) {

		ArrayList<String> prevFileStrs = FileReading.readFile(prev_file);
		ArrayList<String> currentFileStrs = FileReading.readFile(crnt_file);
		return getDifference(prevFileStrs, currentFileStrs);
	}
	private TreeMap<String,Integer> getDifference (ArrayList<String> prevs, ArrayList<String> currs) {

		/**calculate Length of Module*/
		int tortalLine = prevs.size();

		Patch diff = DiffUtils.diff(prevs, currs);
		List<Delta> deltas = diff.getDeltas();
		int cntNew = 0;
		int cntChange = 0;	
		int cntDelete = 0;

		for (Delta delta : deltas) {
			TYPE type = delta.getType();
			if (type.toString() == "INSERT") {
				cntNew++;

			}
			if (type.toString() == "CHANGE") {
				cntChange++;
			}
			if (type.toString() == "DELETE") {
				cntDelete++;
			}
		}
//		System.out.println(cntNew+", "+cntChange+", "+cntDelete);

		/**translate <key,value>*/
		TreeMap<String,Integer> differences = new TreeMap<String,Integer>();
		differences.put("totalLine", tortalLine);
		differences.put("newLine", cntNew);
		differences.put("changedLine", cntChange);
		differences.put("deletedLine", cntDelete);

		return differences;

	}
	private Module searchModuleByClassName( ArrayList<Module> targetArrayList, String key ) {
		for(Module module: targetArrayList) {
			if( module.getClassName() == null) {
				DiffAnalyzerMain.logger.warning( module.getClassName() );
			}
			if( module.getClassName() .equals(key)) {
				return module;
			}
		}
		DiffAnalyzerMain.logger.info( "There are no module named in: " + key );
		return null;
	}
	private Module searchModuleByFilePath( ArrayList<Module> targetArrayList, Module key ) {
		for(Module module: targetArrayList) {
			if( key.getFileName().indexOf( module.getExtractedFileName() ) != -1 ){
				if( module.getClassName() .equals(key.getClassName()) ) {
					DiffAnalyzerMain.numOfExistFile += 1;
					return module;					
				}
			}
		}
		DiffAnalyzerMain.numOfNewModule += 1;
//		DiffAnalyzerMain.logger.warning( "There are no module named: " + key.getFileName() );
		return null;
	}
	public ArrayList<String> compareTwoVersion (ArrayList<Module> currentModules, ArrayList<Module> previousModules) {
		ArrayList<String> record = new ArrayList<String> ();
		// add header to record file
		record.add( this.getHeader() );
		
		/** iterate module in current version */
		for (Module currentModule: currentModules) {
			// get module with same class name as same as current version
			Module previousModule =
					this.searchModuleByFilePath( previousModules, currentModule );
//			DiffAnalyzerMain.logger.info( currentModule.getClassName()  );
			if (previousModule != null) {
				ArrayList<String> currentClass = currentModule.getModuleContainment();
				ArrayList<String> previousClass = previousModule.getModuleContainment();

				// get differences
				TreeMap<String,Integer> differences = this.getDifference(previousClass, currentClass);
				// calculate process metrics
				currentModule.calculateMetrics(differences);				
			}else {
				currentModule.isNew();
				DiffAnalyzerMain.logger.info( currentModule.getClassName()+" in "+currentModule.getFileName()+" has new class"  );

			}
			record.add(currentModule.getMetricsList() );
//			DiffAnalyzerMain.logger.info( currentModule.getMetricsList()  );
		}
		return record;
	}
	private String getHeader() {
		return "fileName,className,isNewModule,M1,M2,M6,M7";
	}

	private ArrayList<String> putClassText(Module module) throws IOException {
		return FileReading.readFile( module.getFileName(),
				module.getBegenningPostion(),
				module.getEndingPostion() );
	}
}