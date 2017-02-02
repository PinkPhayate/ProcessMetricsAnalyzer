package manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class MetricsManagerMain {
	public static void main (String args[] ) {
		if (args.length > 2) {
			return ;
		}
		String usj = args[0];
		String pm = args[1];

		ArrayList<String> metricsFromUnderstand = readFile( usj );
		ArrayList<String> processMetrics = readFile( pm );

		MetricsManager metricsManager = new MetricsManager();
		ArrayList< String > mergedMetrics = metricsManager.mergeMetrics ( metricsFromUnderstand, processMetrics );
		writeFile(mergedMetrics, "mergedMetrics.csv");

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
