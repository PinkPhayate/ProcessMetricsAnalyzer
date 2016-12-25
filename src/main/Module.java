package main;


import java.util.TreeMap;

public class Module {
	private String fileName;
	private String className;
	
	protected int begenningPosition = -1;
	protected int endingPosition = -1;
	
	private double M1 = 0;
	private double M2 = 0;
	private double M3 = 0;
	private double M4 = 0;
	private double M5 = 0;
	private double M6 = 0;
	private double M7 = 0;
	private double M8 = 0;

	private int isNewModule = 0;
	
	private static String mainDir ;
	
	private static String version ;
	private static TreeMap<String, Module> moduleMap = new TreeMap<String, Module>();	

	public Module(String fileName) {
		this.fileName = fileName;
		this.className = "";
	}
	public void putPositions (int begenningPosition, int endingPosition) {
		this.begenningPosition = begenningPosition;
		this.endingPosition = endingPosition;
	}
	public void putClassName (String className) {
		this.className = className;
	}
	public String getClassName() {
		return this.className;
	}
	public int getBegenningPostion() {
		return this.begenningPosition;
	}
	public int getEndingPostion() {
		return this.endingPosition;
	}

	public void isNew() {
		this.isNewModule = 1;
	}

	public void calculateMetrics(TreeMap<String,Integer> differences) {
		int totalLine = differences.get("totalLine");
		int newLine = differences.get("cntNew");
		int changedLine = differences.get("cntChange");
		int deletedLine = differences.get("cntDelete");
		
		this.M1 = newLine + changedLine;
		if (totalLine!= 0) {
			this.M2 = deletedLine / totalLine;
		}
		this.M6 = (newLine + changedLine + deletedLine);
		if (deletedLine != 0) {
			this.M7 = (newLine + changedLine) / deletedLine;			
		}
		
	}
	public String getMetricsList () {
		return fileName + "," +className + "," + isNewModule + "," + M1 + "," + M2 + "," + M6 + "," + M7;
	}
	
	/** method for test*/
	public int getBegenningPosition  () {
		return this.begenningPosition;
	}
	public int getEndingPosition  () {
		return this.endingPosition;
	}
	

}

