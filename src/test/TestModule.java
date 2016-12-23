package test;
import main.Module;
public class TestModule extends Module {

	public TestModule(String fileName) {
		super(fileName);
	}
	public void getPositions () {
		this.begenningPosition = begenningPosition;
		this.endingPosition = endingPosition;
	}
	public int getBegenningPosition  () {
		return this.begenningPosition;
	}
	public int getEndingPosition  () {
		return this.endingPosition;
	}
}
