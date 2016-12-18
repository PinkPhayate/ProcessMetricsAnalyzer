package main;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

public class Module {
	private String fileName;
	private String className;
	private int totalLine ;
	private int deletedLine = 0;
	private int newLine = 0;
	private int changedLine = 0;
	
	private double chum = 0;
	private double relativeChum = 0;
	private double deletedChum = 0;
	private double ncdChum = 0;

	private int isNewModule = 0;
	private int numOfBug = 0;
	
	private static String mainDir ;
	
	private static String version ;
	private static TreeMap<String, Module> moduleMap = new TreeMap<String, Module>();	

	public Module(String fileName, String className) {
		this.fileName = fileName;
		this.className = className;
	}

	public void adddeletedLine(int deletedLine) {
		this.deletedLine += deletedLine ;
	}
	public void addTotalLine(int totalLine) {
		this.totalLine = totalLine+1 ;
	}
	public void addNewLine(int newLine) {
		this.newLine += newLine ;
	}
	public void addChangeLine(int changedLine) {
		this.changedLine += changedLine ;
	}
	public void addNumOfBug(){
		this.numOfBug++;
	}
	public void putLOC(int totalLine) {
		this.totalLine = totalLine;
	}
	public void isNew() {
		this.isNewModule = 1;
	}
//	public String putMetricses(Module module) {
//		String str_numOfBug = String.valueOf(module.numOfBug) ;
//		String str_totalLine = String.valueOf(module.totalLine) ;
//		String str_chum = String.valueOf(module.chum) ;
//		String str_relativeChum = String.valueOf(module.relativeChum) ;
//		String str_deletedChum = String.valueOf(module.deletedChum) ;
//		String str_ncdChum = String.valueOf(module.ncdChum) ;
//		String metricses = 
//				str_numOfBug + ","
//				+ str_totalLine + ","
//				+ str_chum + ","
//				+ str_relativeChum + ","
//				+ str_deletedChum + ","
//				+ str_ncdChum ;
//		
//		return metricses ;
//				
//}
	public String getMetrics() {
//		this.circulateMetricses();
		String metricses = 
				String.valueOf(this.totalLine) + ","
				+ String.valueOf(this.chum) + ","
				+ String.valueOf(this.relativeChum) + ","
				+ String.valueOf(this.deletedChum) + ","
				+ String.valueOf(this.ncdChum) + ","
				+ String.valueOf(this.isNewModule);
		return metricses ;
		
	}
	public void circulateMetricses (TreeMap<String,Integer> differences) {
		this.totalLine = differences.get("totalLine");
		this.newLine = differences.get("newLine");
		this.changedLine = differences.get("changedLine");
		this.deletedLine = differences.get("deletedLine");

		if ( this.changedLine != 0 ) {
			this.chum = this.newLine + this.changedLine;
		}
		if ( this.totalLine != 0 ) {
			this.relativeChum = (double) (this.newLine + this.changedLine) / this.totalLine ;
			this.deletedChum = (double) this.deletedLine / this.totalLine ;
		}
		if ( this.deletedLine != 0 ) {
			this.ncdChum = (double) (this.newLine + this.changedLine) / this.deletedLine ;
		}

	}
	
//	public Module(String version, String mainDir) {
//		this.version = version ;
//		this.mainDir = mainDir;
//	}
	
//	public static void importModuleList() throws IOException {
///**	String mainDir ="C:\\Users\\OWNER\\Documents\\Derby\\" + "db-derby-"+version + "-bin
//*/
//		File file = new File(mainDir + "\\lib\\v"+version+"_moduleList_cls.txt");
//		FileReader fr = new FileReader(file);
//		BufferedReader br = new BufferedReader(fr) ;
//		String line = br.readLine() ;
//		while(line != null) {
//			Module module = new Module(version,mainDir);
//			moduleMap.put(line, module);
//			line= br.readLine();
//		}
//		
//		br.close();
//	}
//	public static boolean isExist(String moduleName) {
//		Module module = moduleMap.get(moduleName);
//		if(module == null) {
//			return false;
//		}
//		return true;
//	}
//	public String getModuleData() {
//		String metricses = String.valueOf(chum) +"," + String.valueOf(relativeChum) +"," + String.valueOf(deletedChum) +"," + String.valueOf(ncdChum) ;
//		return name +"," +metricses +"," + this.numOfBug;
//	}


}
