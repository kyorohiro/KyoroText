package info.kyorohiro.helloworld.pdf.pdflexer;

import java.io.IOException;

import info.kyorohiro.helloworld.io.VirtualMemory;

// memo 
// pdf is written at ascii character with 0-128 and byte stream.
public class PdfLexer {
	public static int ID_WHITESPACE = 0;
	public static int ID_DELIMITER = 1;
	public static int ID_REGULAR_CHARACTER = 2;
	public static int ID_COMMENT = 3;
	public static int ID_PERSENT = 4;
	public static int ID_EXCLUDE_EOF = 5;
	public static int ID_BOOLEAN = 6;
	public static int ID_INTEGER = 7;
	public static int ID_REAL = 9;
	public static int ID_PLUSMINUS = 8;
	public static int ID_DOT = 10;
	public static int ID_EOF = -1;

	private WhiteSpace mWhiteSpace = new WhiteSpace();
	private Delimiter mDelimiter = new Delimiter();
	private RegularCharacter mRegularCharacter = new RegularCharacter();
	private Source mText = null;

	public PdfLexer(Source text) {
		mText = text;
	}

	public Source getText() {
		return mText;
	}

	public void mark() {
		getText().getVirtualMemory().pushMark();
	}

	public void backToMark() {
		getText().getVirtualMemory().backToMark();
	}

	public void releaseMark() {
		getText().getVirtualMemory().popMark();
	}

	public Token nextToken(SourcePattern pattern, boolean escapedWhiteSpace) throws GotoException {
		Source source = getText();
		W: while (true) {
			if (escapedWhiteSpace&&source.muchHead(mWhiteSpace)) {
				continue W;
			}
			if (source.muchHead(pattern)) {
				return pattern.newToken();
			}
			break;
		}
		throw new GotoException();
	}

	public static class Persent extends BytePattern {
		public Persent() {
			super(ID_PERSENT, new byte[][]{{'%'}}, false, false);
		}
	}

	public static class Dot extends  BytePattern {
		public Dot() {
			super(ID_DOT, new byte[][]{
					{'.'},
					}, 
					false,//isLine
					false);//exclude
		}
	}


	public static class PlusMinus extends  BytePattern {
		public PlusMinus() {
			super(ID_PLUSMINUS, new byte[][]{
					{'+'},
					{'-'},
					}, 
					false,//isLine
					false);//exclude
		}
	}

	public static class IntegerValue extends  BytePattern {
		public IntegerValue() {
			super(ID_INTEGER, new byte[][]{
					{'0'}, {'1'}, {'2'}, {'3'}, {'4'},
					{'5'}, {'6'}, {'7'}, {'8'}, {'9'},
					}, 
					true,//isLine
					false);//exclude
		}
	}

	public static class BooleanValue extends  BytePattern {
		public BooleanValue() {
			super(ID_BOOLEAN, new byte[][]{
					{'t','r','u','e'},
					{'f','a','l','s','e'},
					}, 
					false,//isLine
					false);//exclude
		}
	}
	public static class ExcludeEOL extends BytePattern {
		public ExcludeEOL() {
			super(ID_EXCLUDE_EOF, new byte[][]{{'\n'}}, true, true);
		}
	}

	public static class WhiteSpace extends BytePattern {
		public WhiteSpace() {
			super(PdfLexer.ID_WHITESPACE,
					new byte[][] { { 0x00 }, { 0x09 }, { 0x0A }, { 0x0C },
					{ 0x0D }, { 0x20 } });
		}
	}

	public static class Delimiter extends BytePattern {
		public Delimiter() {
			super(PdfLexer.ID_DELIMITER, 
					new byte[][] { { 0x28 }, { 0x29 }, { 0x3C }, { 0x3E },
					{ 0x5B }, { 0x5D }, { 0x7B }, { 0x7D } });
		}
	}

	public static class RegularCharacter extends BytePattern {
		public RegularCharacter() {
			super(PdfLexer.ID_REGULAR_CHARACTER,
					new byte[][] {
					createByte((byte) 0x01, (byte) 0x08),
					{ 0x0b }, createByte((byte) 0x0e, (byte) 0x1f),
					createByte((byte) 0x21, (byte) 0x24),
					createByte((byte) 0x26, (byte) 0x27),
					createByte((byte) 0x2a, (byte) 0x3b), { 0x3d },
					createByte((byte) 0x3f, (byte) 0x5a), { 0x5c },
					createByte((byte) 0x5e, (byte) 0x7a), { 0x7c }, { 0x7E },
					{ 0x25 } });
		}
	}

	public static byte[] createByte(byte a, byte b) {
		byte[] ret = new byte[b - a + 1];
		for (byte i = 0, z = a; z <= b; z++) {
			ret[i++] = z;
		}
		return ret;
	}
}
