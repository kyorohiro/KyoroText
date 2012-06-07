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
	public static int ID_LITERAL_STRING = 11;
	public static int ID_LITERAL_STRING_BEGIN = 12;
	public static int ID_LITERAL_STRING_END = 13;
	public static int ID_LITERAL_ESCAPSE_BEGIN_END = 14;
	public static int ID_NAME_DELIMITER = 15;
	public static int ID_NAME = 16;
	public static int ID_EOF = -1;
	public static int ID_REGULAR_STRING = 17;
	public static int ID_VALUE = 18;

	public static int ID_HEXADECIMAL_STRING = 11;
	public static int ID_HEXADECIMAL_STRING_BEGIN = 12;
	public static int ID_HEXADECIMAL_STRING_END = 13;
	
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

	public static class NameDelimiter extends BytePattern {
		public NameDelimiter() {
			super(ID_NAME_DELIMITER, new byte[][]{{'/'}}, false, false);
		}
	}


	public static class HexadecimalStringBegin extends BytePattern {
		public HexadecimalStringBegin() {
			super(ID_LITERAL_STRING_BEGIN, new byte[][]{{'<'}}, false, false);
		}
	}

	public static class HexadecimalStringEnd extends BytePattern {
		public HexadecimalStringEnd() {
			super(ID_LITERAL_STRING_END, new byte[][]{{'>'}}, false, false);
		}
	}
 
	public static class HexadecimalString extends BytePattern {
		public HexadecimalString() {
			super(ID_HEXADECIMAL_STRING, new byte[][]{
					{'A'},{'B'},{'C'},{'D'},{'E'},
					{'F'},{'G'},{'H'},{'I'},{'J'},
					{'K'},{'L'},{'M'},{'N'},{'O'},
					{'P'},{'Q'},{'R'},{'S'},{'T'},
					{'U'},{'V'},{'W'},{'X'},{'Y'},
					{'Z'},
					{'0'},{'1'},{'2'},{'3'},{'4'},
					{'5'},{'6'},{'7'},{'8'},{'9'},
			}, true, false);
		}
	}

	public static class ArrayBegin extends BytePattern {
		public ArrayBegin() {
			super(ID_HEXADECIMAL_STRING_BEGIN, new byte[][]{{'['}}, false, false);
		}
	}

	public static class ArrayEnd extends BytePattern {
		public ArrayEnd() {
			super(ID_HEXADECIMAL_STRING_END, new byte[][]{{']'}}, false, false);
		}
	}

	public static class LiteralStringBegin extends BytePattern {
		public LiteralStringBegin() {
			super(ID_HEXADECIMAL_STRING_BEGIN, new byte[][]{{'('}}, false, false);
		}
	}

	public static class LiteralStringEnd extends BytePattern {
		public LiteralStringEnd() {
			super(ID_HEXADECIMAL_STRING_END, new byte[][]{{')'}}, false, false);
		}
	}

	public static class LiteralString extends BytePattern {
		public LiteralString() {
			super(ID_LITERAL_STRING, new byte[][]{					
			{'\\','('},{'\\',')'},{'('},{')'}}, true, true);
		}
	}

	public static class LiteralString_EscapeBeginEnd extends BytePattern {
		public LiteralString_EscapeBeginEnd() {
			super(ID_LITERAL_ESCAPSE_BEGIN_END, new byte[][]{					
			{'\\','('},{'\\',')'}}, true, false);
		}
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

	public static class RegularString extends BytePattern {
		public RegularString() {
			super(PdfLexer.ID_REGULAR_STRING,
					new byte[][] {
					{0x01}, {0x02}, {0x03},{0x04},{0x05},{0x06},{0x07},{0x08},
					{0x0b}, {0x0e}, {0x1f},
					{0x21}, {0x22}, {0x23}, {0x24},
					{0x26}, {0x27},
					{0x2a}, {0x2b}, {0x2c}, {0x2d}, {0x2e}, {0x2f},
					{0x30}, {0x31}, {0x32}, {0x33}, {0x34}, {0x35},
					{0x36}, {0x37}, {0x38}, {0x39}, {0x3a}, {0x3b},
					{0x3d},
					{0x3f}, {0x40}, {0x41}, {0x42}, {0x43}, {0x44},
					{0x45}, {0x46}, {0x47}, {0x48}, {0x49}, {0x4a},
					{0x4b}, {0x4c}, {0x4d}, {0x4e}, {0x4f},
					{0x50}, {0x51}, {0x52}, {0x53}, {0x54},
					{0x55}, {0x56}, {0x57}, {0x58}, {0x59}, {0x5a},
				    {0x5c},
					{0x5e}, {0x5f}, 
					{0x60}, {0x61}, {0x62}, {0x63}, {0x64},
					{0x65}, {0x66}, {0x67}, {0x68}, {0x69}, {0x6a},
					{0x6b}, {0x6c}, {0x6d}, {0x6e}, {0x6f},
					{0x70}, {0x71}, {0x72}, {0x73}, {0x74},
					{0x75}, {0x76}, {0x77}, {0x78}, {0x79}, {0x7a},
                    {0x7c}, {0x7E},
					{0x25} },
					true, false);
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
