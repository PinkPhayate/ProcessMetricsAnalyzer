package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import lib.FileListGetter;
import lib.FileReading;
import lib.FileWriting;

public class DiffAnalyzerMain extends Thread{
	/**
	 * mainDirecotory :豈碑ｼ�縺吶ｋ繝舌�ｼ繧ｸ繝ｧ繝ｳ縺悟曙譁ｹ鄂ｮ縺�縺ｦ縺ゅｋ繝輔か繝ｫ繝� cvDirectory �ｼ壽怙譁ｰ繝舌�ｼ繧ｸ繝ｧ繝ｳ縺檎ｽｮ縺九ｌ縺ｦ縺�繧九ョ繧｣繝ｬ繧ｯ繝医Μ蜷�
	 * pvDirectory �ｼ夐℃蜴ｻ縺ｮ繝舌�ｼ繧ｸ繝ｧ繝ｳ縺檎ｽｮ縺九ｌ縺ｦ縺�繧九ョ繧｣繝ｬ繧ｯ繝医Μ蜷� suffix �ｼ壹�励Ο繧ｰ繝ｩ繝溘Φ繧ｰ險�隱槭�ゅた繝ｼ繧ｹ繧ｳ繝ｼ繝峨�ｮ隱槫ｰｾ繧呈嶌縺上��
	 * 
	 * 1. classModule繧貞�ｨ縺ｦ蜿門ｾ励☆繧九�� 繝ｻ繧ｯ繝ｩ繧ｹ縺梧嶌縺九ｌ縺ｦ縺�繧九ヵ繧｡繧､繝ｫ蜷� 繝ｻ繧ｯ繝ｩ繧ｹ繝｢繧ｸ繝･繝ｼ繝ｫ蜷� 繝ｻ繧ｯ繝ｩ繧ｹ縺ｮ蟋九∪繧翫�ｮ陦� 繝ｻ繧ｯ繝ｩ繧ｹ縺ｮ邨ゅｏ繧翫�ｮ陦�
	 * 2. 螳滄ｨ灘ｯｾ雎｡繝舌�ｼ繧ｸ繝ｧ繝ｳ縺ｨ縲�驕主悉縺ｮ繝舌�ｼ繧ｸ繝ｧ繝ｳ蜈ｱ縺ｫ縲�ｼ代ｒ陦後≧ 3.
	 * 螳滄ｨ灘ｯｾ雎｡繝舌�ｼ繧ｸ繝ｧ繝ｳ縺ｮ繧ｯ繝ｩ繧ｹ繧剃ｸ�縺､縺壹▽隕九※縺�縺阪��驕主悉縺ｮ繝舌�ｼ繧ｸ繝ｧ繝ｳ縺ｧ荳�閾ｴ縺吶ｋ繧ｯ繝ｩ繧ｹ縺後≠繧後�ｰdiff繧貞叙蠕励☆繧�
	 * 
	 * 
	 */
	public static final Logger logger = Logger.getLogger(DiffAnalyzerMain.class.getName());
	/**
	 * indicator for report
	 */
	public static int numOfExistFile = 0;
	public static int numOfNewModule = 0;

	public static ArrayList<String> originRecord;
	public void run (String args[]) {
		
	}
	public static void main(String args[]) {
		// save log to file
		try {
		    FileHandler fh = new FileHandler("DiffAnalyzerLog.log");
		    fh.setFormatter(new java.util.logging.SimpleFormatter());
		    logger.addHandler(fh);
		    removeConsoleHandler( logger );
		} catch (IOException e) {
		    e.printStackTrace();
		}
		if (args.length != 3) {
			System.out.println("length of argument IS INVARID.");
			return;
			
		}

//		String mainDirecotory = args[0];
		String cvDirectory = args[0];
		String pvDirectory = args[1];
		String suffix = args[2];
		String currentVer = extractCurrentVersionString( cvDirectory );
		String saveFile = "ProcessMetrics.csv";

		/** Step 1 get class module in current version */
		FileListGetter search = new FileListGetter(suffix);

		// file list in directory about later version
		ArrayList<String> currFileStrs = search.getFileList(cvDirectory);
		FileAnalyzer fileAnalyzer = new FileAnalyzer(suffix);
		ArrayList<Module> currentModules = fileAnalyzer.getModules(currFileStrs);
		fileAnalyzer.saveModules( "current-modules.txt" );
		System.out.println("Step 1 has finished");
		search.clear();

		/** Step 2 get class module in previous version */
		ArrayList<String> prevFileStrs = search.getFileList(pvDirectory);
		fileAnalyzer = new FileAnalyzer(suffix);
		ArrayList<Module> previousModules = fileAnalyzer.getModules(prevFileStrs);
		fileAnalyzer.saveModules( "previous-modules.txt" );
		System.out.println("Step 2 has finished");

		/** Step 3 */
		DiffAnalyzer diffAnalyzer = new DiffAnalyzer();
		ArrayList<String> record =
				diffAnalyzer.compareTwoVersion(currentModules, previousModules);
		originRecord = record;
		FileWriting.writeFile(record, saveFile);

		DiffAnalyzerMain.logger.info("Step 3 has finished");
		
		/** report */
		ArrayList<String> report = new ArrayList<String>();
		report.add("Number of files in Directory\t\t: "+currFileStrs.size());
		report.add("Number of current class\t\t\t: "+currentModules.size());
		report.add("Number of previous class\t\t: "+previousModules.size());
		report.add("Number of exist module\t\t\t: "+numOfExistFile);
		report.add("Number of new module\t\t\t: "+numOfNewModule);
		FileWriting.writeFile(report,  "report.txt" );
		
		System.out.println("\nResult");
		System.out.println("===================================");
		for (String line: report) {
			System.out.println( line );
		}

		return;

	}
	private static void removeConsoleHandler(Logger logger) {
		Handler[] handlerArr = logger.getHandlers();
		for (Handler handler : handlerArr) {
			if (handler instanceof ConsoleHandler) {
				logger.removeHandler(handler);
			}
		}
		Logger parentLogger = logger.getParent();
		if (parentLogger != null) {
			removeConsoleHandler(parentLogger);
		}
	}
	
	private static String extractCurrentVersionString (String cvDirectory) {
//		String [] array = cvDirectory.split("/");
		String [] array = cvDirectory.split("\\\\");
		return array[ array.length-1 ];
	}
}
