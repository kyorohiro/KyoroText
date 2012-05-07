 package info.kyorohiro.helloworld.pdfparser;

// memo 
// pdf is written at ascii character with 0-128 and byte stream.
public class PdfLexer extends Lexer {
	public static Token SET_SPACE = new Token(1, "<space>", "^\\s|^\t|^\n|^\r");
	public static Token SET_COMMENT = new Token(2, "<comment>", "^%.*[\n\0]");
	public static Token SET_NUMBER = new Token(3, "<number>", "^[0-9]+");
	public static Token SET_INDEX_BEGIN = new Token(4, "<index_begin>", "^<<");
	public static Token SET_INDEX_END = new Token(5, "<index_end>", "^>>");
	public static Token SET_ARRAY_BEGIN = new Token(6, "<array_begin>", "^\\[");
	public static Token SET_ARRAY_END = new Token(7, "<array_begin>", "^\\]");
	public static Token SET_ASCII_BEGIN = new Token(8, "<ascii_begin>", "^\\(");
	public static Token SET_ASCII_CHARSET = new Token(9, "<ascii_charset>", "^[\\x00-\\x7F]+");
	public static Token SET_ASCII_END = new Token(10, "<ascii_end>", "^\\)");
	public static Token SET_NONASCII_BEGIN = new Token(11, "<nonascii_begin>", "^<");
	public static Token SET_NONASCII_END = new Token(12, "<nonascii_end>", "^>");
	public static Token SET_OBJECT_BEGIN = new Token(13, "<object_begin>", "^obj");
	public static Token SET_OBJECT_END = new Token(14, "<object_end>", "^endobj");
	public static Token SET_NAME = new Token(15, "<name>", "^/[A-Za-z0-9]+");
	public static Token SET_IDENTIFY = new Token(16, "<identify>", "^[A-Za-z0-9]+");
	public static Token SET_DELIMITER = new Token(17, "<identify>", "^[\\(\\)<>\\[\\]{}\\/%]");
	public static Token SET_BOOLEAN = new Token(18, "<boolean>", "^true|^false");

	public static Token[] SET_UNRETURN_TOKEN_LIST = {
		SET_SPACE, SET_COMMENT,
	};

	public static Token[] SET_RETURN_TOKEN_LIST = {
		SET_EOF, SET_BOOLEAN, SET_NUMBER,
		SET_INDEX_BEGIN, SET_INDEX_END,
		SET_ARRAY_BEGIN, SET_ARRAY_END,
		SET_ASCII_BEGIN, SET_ASCII_END,
		SET_NONASCII_BEGIN,SET_NONASCII_END,
		SET_OBJECT_BEGIN, SET_OBJECT_END, 
		SET_NAME,SET_IDENTIFY,SET_ASCII_CHARSET };

	public PdfLexer(Text text) {
		super(text);
	}

	@Override
	public CharSequence getTokenName() {
		return super.getTokenName();
	}

	@Override
	public Token nextToken() {
		if('\0' == getText().getCharacter()){
			return new Token(SET_EOF, "\0");
		}
		W:while(true) {
			for(Token t : SET_UNRETURN_TOKEN_LIST){
				if(getText().muchHead(t.mPattern)){
					getText().next(t.mPattern);
					continue W;
				}
			}

			for(Token t : SET_RETURN_TOKEN_LIST){
				if(getText().muchHead(t.mPattern)){
					CharSequence value = getText().next(t.mPattern);
					return new Token(t, value);
				}
			}
			break;
		}
		return new Token(SET_EOF, "\0");
	}
	
}
