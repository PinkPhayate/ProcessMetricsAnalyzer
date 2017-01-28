package property;

public enum JavaWords {
	STATIC("static"),
	ABSTRACT("abstract"),
	PARTIAL("partial"),
	SEALED("sealed"),
	FINAL("final");
    private String word;
	JavaWords(String word) { this.word = word; }
    public String getWord() { return word; }
}
