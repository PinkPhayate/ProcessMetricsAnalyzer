package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Logger;

import lib.FileListGetter;
import lib.FileReading;
import lib.FileWriting;

public class DiffAnalyzerMain {
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
	public static ArrayList<String> originRecord;
	public static void main(String args[]) {
// TODO	Bug: there are same named file in current-modules.txt for eg. TestMessage
		if (args.length != 4) {
			return;
		}

		String mainDirecotory = args[0];
		String cvDirectory = args[0] + args[1];
		String pvDirectory = args[0] + args[2];
		String suffix = args[3];

		// String mainDirecotory = "/Users/phayate/src/TestDate/";
		// String cvDirectory=
		// "/Users/phayate/src/TestDate/Glimpse-1.9.2-aspnet";
		// String pvDirectory= "/Users/phayate/src/TestDate/Glimpse-1.7.3-ado";
		// String suffix = "cs";

		/** Step 1 get class module in current version */
		FileListGetter search = new FileListGetter(suffix);

		// file list in directory about later version
		ArrayList<String> currFileStrs = search.getFileList(cvDirectory);
		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		ArrayList<Module> currentModules = fileAnalyzer.getModules(currFileStrs);
		fileAnalyzer.saveModules( mainDirecotory + "current-modules.txt" );
		DiffAnalyzerMain.logger.info("Step 1 has finished");

		/** Step 2 get class module in previous version */
		ArrayList<String> prevFileStrs = search.getFileList(pvDirectory);
		fileAnalyzer = new FileAnalyzer();
		ArrayList<Module> previousModules = fileAnalyzer.getModules(prevFileStrs);
		fileAnalyzer.saveModules( mainDirecotory + "previous-modules.txt" );		DiffAnalyzerMain.logger.info("Step 2 has finished");

		/** Step 3 */
		DiffAnalyzer diffAnalyzer = new DiffAnalyzer();
		ArrayList<String> record =
				diffAnalyzer.compareTwoVersion(currentModules, previousModules);
		originRecord = record;
		try {
			FileWriting.writeFile(record, mainDirecotory + "processmetrics.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		DiffAnalyzerMain.logger.info("Step 3 has finished");
		return;

	}
	

}
