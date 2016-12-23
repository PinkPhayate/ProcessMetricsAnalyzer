package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Logger;

import lib.FileListGetter;
import lib.FileReading;

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
	static final Logger logger = Logger.getLogger( DiffAnalyzerMain.class.getName() );
	public static void main (String args[]) {
		
		String path = "/Users/phayate/src/ApacheDerby/";
		String currentVersion = "10.12";
		String previousVersion = "10.11";
		
		/** Step 1	get class module in current version*/
		FileListGetter search = new FileListGetter();
		ArrayList<String> currFileStrs = search.getFileList( path+currentVersion );
		FileAnalyzer fileAnalyzer = new FileAnalyzer();
		ArrayList<Module> currentModules = fileAnalyzer.getModules( currFileStrs );
		fileAnalyzer.saveModules("current-modules.txt");
		
		/**	Step 2	get class module in previous version*/
		ArrayList<String> prevFileStrs = search.getFileList( path+previousVersion );
		fileAnalyzer = new FileAnalyzer();
		ArrayList<Module> previousModules = fileAnalyzer.getModules( prevFileStrs );
		
		/**	Step 3 */
		DiffAnalyzer diffAnalyzer = new DiffAnalyzer();
		diffAnalyzer.compareTwoVersion(currentModules, previousModules);
		
	}

}
