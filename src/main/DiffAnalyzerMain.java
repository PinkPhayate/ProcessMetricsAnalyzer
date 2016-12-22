package main;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import lib.FileListGetter;

public class DiffAnalyzerMain {
/**
 * 
 * 1. classModuleを全て取得する。
 * 	・クラスが書かれているファイル名
 * 	・クラスモジュール名
 * 	・クラスの始まりの行
 * 	・クラスの終わりの行
 * 2. 実験対象バージョンと、過去のバージョン共に、１を行う
 * 3. 実験対象バージョンのクラスを一つずつ見ていき、過去のバージョンで一致するクラスがあればdiffを取得する
 * 
 * 
 * */
	public static void main (String args[]) {
		final Logger logger = Logger.getLogger("SampleLogging");
		String path = "/Users/phayate/src/ApacheDerby/";
		String currentVersion = "10.12";
		String previousVersion = "10.11";
		
		String listFileName = "diff_fileDirectory_containment.txt";

		/** Step 1	get class module in current version*/
		FileListGetter search = new FileListGetter();
		ArrayList<String> currFileStrs = search.getFileList( path+currentVersion );
		FileAnalyzer fileAnalyzer = new FileAnalyzer(logger);
		ArrayList<Module> currentModules = fileAnalyzer.getModules( currFileStrs );
		fileAnalyzer.saveModules("current-modules.txt");
		
		/**	Step 2	get class module in previous version*/
		ArrayList<String> prevFileStrs = search.getFileList( path+previousVersion );
		fileAnalyzer = new FileAnalyzer(logger);
		ArrayList<Module> previousModules = fileAnalyzer.getModules( prevFileStrs );
		fileAnalyzer.saveModules("previous-modules.txt");
		
		
	}
}
