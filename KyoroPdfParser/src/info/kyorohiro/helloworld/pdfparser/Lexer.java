package info.kyorohiro.helloworld.pdfparser;


public class Lexer {
	public static final Token SET_EOF = new Token(-1, "<eof>", "\0");
	private Text mText;

	public Lexer(Text text) {
		mText = text;
	}
	
	public Text getText() {
		return mText;
	}

	public Token nextToken() {
		// sample implements 
		return new Token(SET_EOF, "\0");
	}

	public CharSequence getTokenName() {
		return SET_EOF.getIdentify();
	}

}
