package lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileWriting {
	public static void writeFile(ArrayList<String> content, String fileName) throws IOException {
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		BufferedWriter br = new BufferedWriter (fw) ;

		for(String text: content ) {
			br.write(text + "\n");
		}
		br.close();
	}

}
