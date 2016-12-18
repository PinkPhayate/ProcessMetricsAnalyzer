package main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import difflib.Chunk;
import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;
import difflib.Delta.TYPE;
import lib.FileReading;

public class DiffAnalyzer {
	private List<String> prevFileStrs = null;
	private List<String> currentFileStrs = null;
	
	public TreeMap<String,Integer> getDifference (String prev_file, String crnt_file) {
		
		try {
			this.prevFileStrs = FileReading.readFile(prev_file);
			this.currentFileStrs = FileReading.readFile(crnt_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**calculate Length of Module*/
		int tortalLine = prevFileStrs.size();

		Patch diff = DiffUtils.diff(this.prevFileStrs, this.currentFileStrs);
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
		System.out.println(cntNew+", "+cntChange+", "+cntDelete);

		/**translate <key,value>*/
		TreeMap<String,Integer> differences = new TreeMap<String,Integer>();
		differences.put("totalLine", tortalLine);
		differences.put("newLine", cntNew);
		differences.put("changedLine", cntChange);
		differences.put("deletedLine", cntDelete);
		
		return differences;
	}
}
