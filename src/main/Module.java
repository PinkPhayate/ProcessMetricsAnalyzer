package main;

import java.util.ArrayList;
import java.util.TreeMap;

public class Module {
	private String fileName = null;
	private String className = null;

	protected int begenningPosition = -1;
	protected int endingPosition = -1;

	private ArrayList<String> containment = null;
	private int loc = 0;
	private double M1 = 0;
	private double M2 = 0;
	private double M3 = 0;
	private double M4 = 0;
	private double M5 = 0;
	private double M6 = 0;
	private double M7 = 0;
	private double M8 = 0;

	private int isNewModule = 0;

	private static String mainDir;

	private static String version;
	private static TreeMap<String, Module> moduleMap = new TreeMap<String, Module>();

	public Module(String fileName) {
		this.fileName = fileName;
		this.className = "";
	}

	public void putPositions(int begenningPosition, int endingPosition) {
		this.begenningPosition = begenningPosition;
		this.endingPosition = endingPosition;
	}

	public void putModuleContainment(ArrayList<String> containment) {
		this.containment = containment;
	}

	public void putClassName(String className) {
		this.className = className;
	}

	public ArrayList<String> getModuleContainment() {
		return this.containment;
	}

	public String getClassName() {
		return this.className;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getExtractedFileName() {
		String[] array = this.fileName.split("/");
		return array[array.length - 1];
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

	public void calculateMetrics(TreeMap<String, Integer> differences) {
		int totalLine = differences.get("totalLine");
		int newLine = differences.get("newLine");
		int changedLine = differences.get("changedLine");
		int deletedLine = differences.get("deletedLine");

		this.M1 = newLine + changedLine;
		if (totalLine != 0) {
			this.M2 = deletedLine / (double) totalLine;
		}
		this.M6 = (newLine + changedLine + deletedLine);
		if (deletedLine != 0) {
			this.M7 = (newLine + changedLine) / (double) deletedLine;
		}
		
		this.loc = getLOC();
	}

	public String getMetricsList() {
		return fileName + "," + className + "," + isNewModule + "," + M1 + "," + M2 + "," + M6 + "," + M7 + "," + loc;
	}

	/** method for test */
	public int getBegenningPosition() {
		return this.begenningPosition;
	}

	public int getEndingPosition() {
		return this.endingPosition;
	}

	private int getLOC() {
		int loc = this.endingPosition - this.begenningPosition;
		if (0 < loc) {
			return loc;
		}
		return 0;
	}

}
