package manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lib.FileReading;
import lib.FileWriting;

public class BugMager {
	static ArrayList< List<String> > bugListCsv = null;
	public static String bugDir = "";
	public static String pmDir = "";
	public static void main(String[] args) {
	String version = "v1";
	bugDir = args[0];
	pmDir = args[1];

	merger("bug-"+version, "ProcessMetrics-"+version);
	}

	public static void merger(String bugListName, String pmListName) {
		ArrayList<String> bugList = FileReading.readFile(bugDir + bugListName+".csv");
		bugListCsv = convertCsv(bugList);
		ArrayList<String> pmList = FileReading.readFile(pmDir + pmListName+".csv");
		ArrayList< List<String> > pmListCsv = convertCsv(pmList);

		ArrayList<String> mergedList = new ArrayList<String>();
		for(List<String> list: pmListCsv) {
			String line = join(list);
			if (foundBug(list.get(1))) {
				line += ",1";
			}else {
				line += ",0";
			}
			mergedList.add(line);
		}
		FileWriting.writeFile(mergedList, pmDir +pmListName + "Bug.csv");

	}
	private static boolean foundBug(String str) {
		if(bugListCsv != null) {
			for(List<String> bugs: bugListCsv) {
				if(bugs.indexOf(str) != -1) {
					return true;
				}
			}

		}

		return false;
	}

	private static ArrayList< List<String> > convertCsv (ArrayList< String > arrayList) {
		ArrayList< List<String> > csv = new ArrayList< List<String> >();
		for (String line: arrayList) {
			String [] array = line.split(",");
			List<String> tmp = Arrays.asList(array);
			csv.add(tmp);
		}
		return csv;
	}
	private static String join ( List<String> array ) {
		String str = "";
		for ( String element: array ) {
			str += element;
			str += ",";
//			str = str.concat( element );
		}
		return str;
	}
}
