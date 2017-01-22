package main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import lib.FileListGetter;
import lib.FileWriting;

public class DiffAnalyzerMainThread extends Thread{



	/**
	 * mainDirecotory :比較するバージョンが双方置いてあるフォルダ cvDirectory ：最新バージョンが置かれているディレクトリ名
	 * pvDirectory ：過去のバージョンが置かれているディレクトリ名 suffix ：プログラミング言語。ソースコードの語尾を書く。
	 * 
	 * 1. classModuleを全て取得する。 ・クラスが書かれているファイル名 ・クラスモジュール名 ・クラスの始まりの行 ・クラスの終わりの行
	 * 2. 実験対象バージョンと、過去のバージョン共に、１を行う 3.
	 * 実験対象バージョンのクラスを一つずつ見ていき、過去のバージョンで一致するクラスがあればdiffを取得する
	 * 
	 * 
	 */
	public static final Logger logger = Logger.getLogger(DiffAnalyzerMain.class.getName());
	/**
	 * indicator for report
	 */
	public static int numOfExistFile = 0;
	public static int numOfNewModule = 0;

	private String cvDirectory = null;
	private String pvDirectory = null;
	private String suffix = null;
	private String saveFile = "ProcessMetrics.csv";

	public static ArrayList<String> originRecord;
	public DiffAnalyzerMainThread(String[] args) {
		if (args.length != 3) {
			return;
		}
		this.cvDirectory = args[0]+"/";
		this.pvDirectory = args[1]+"/";
		this.suffix = args[2];		
	}
	public void run () {

		// save log to file
		try {
			FileHandler fh = new FileHandler("DiffAnalyzerLog.log");
			fh.setFormatter(new java.util.logging.SimpleFormatter());
			logger.addHandler(fh);
			removeConsoleHandler( logger );
		} catch (IOException e) {
			e.printStackTrace();
		}

		/** Step 1 get class module in current version */
		FileListGetter search = new FileListGetter(suffix);

		// file list in directory about later version
		ArrayList<String> currFileStrs = search.getFileList(cvDirectory);
		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		ArrayList<Module> currentModules = fileAnalyzer.getModules(currFileStrs);
		fileAnalyzer.saveModules( "current-modules.txt" );
		System.out.println("Step 1 has finished");
		search.clear();

		/** Step 2 get class module in previous version */
		ArrayList<String> prevFileStrs = search.getFileList(pvDirectory);
		fileAnalyzer = new FileAnalyzer();
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

//		return;

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



}