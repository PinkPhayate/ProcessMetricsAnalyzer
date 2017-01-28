package property;

public enum CsWords {
	STATIC("static"),
	ABSTRACT("abstract"),
	PARTIAL("partial"),
	SEALED("sealed");
    private String word;
    CsWords(String word) { this.word = word; }
    public String getWord() { return word; }

}
