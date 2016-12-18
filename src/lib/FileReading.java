package lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileReading {
	public static ArrayList<String> readFile(String fileName) throws IOException {
		ArrayList<String> fileContainment = new ArrayList<String>();
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);

		String line = br.readLine();
		while(line!=null) {
			fileContainment.add(line);
			line = br.readLine();
		}
		br.close();

		return fileContainment;
	}

}
