package manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
	

public class MetricsManagerMain {
	public static String [] metrics = {
			"CountDeclMethod",
			"PercentLackOfCohesion",
			"CountClassDerived",
			"MaxInheritanceTree",
			"CountClassCoupled",
			"CountDeclMethodAll"};
	static ArrayList<String> metricsFromUnderstand = null;
	static ArrayList<String> processMetrics = null;
	public static void main (String args[] ) {
		if (args.length > 3) {
			return ;
		}
		String usj = args[0];
		String pm = args[1];
		String ver = args[2];

		metricsFromUnderstand = readFile( usj );
		processMetrics = readFile( pm );
		
		ArrayList<Integer> selectedIndexes = selectIndexes();
		MetricsManager metricsManager = new MetricsManager();
		ArrayList< String > mergedMetrics = metricsManager.mergeMetrics ( metricsFromUnderstand, selectedIndexes, processMetrics );
		writeFile(mergedMetrics, "mergedMetrics"+ver+".csv");

	}
	private static ArrayList<Integer> selectIndexes() {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(String metric: metrics) {
			int idx = selectMetricIndex( metric );
			if(idx>0) {
				indexes.add(idx);
			}
		}
		return indexes;

	}
	private static int selectMetricIndex(String metric) {
		String[] columns = metricsFromUnderstand.get(0).split(",");
		for(int idx=0;idx<columns.length;idx++) {
			String col = columns[idx];
			if ( metric.equals(col) ) {
				return idx;
			}
		}
		
		return -1;
	}
	public static void writeFile(ArrayList<String> content, String fileName) {
		File file = new File(fileName);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			BufferedWriter br = new BufferedWriter (fw) ;
			for(String text: content ) {
				br.write(text + "\n");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static ArrayList<String> readFile(String fileName) {
		ArrayList<String> fileContainment = new ArrayList<String>();
		File file = new File(fileName);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			while(line!=null) {
				fileContainment.add(line);
				line = br.readLine();
			}
			br.close();

			return fileContainment;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
