package info.kyorohiro.helloworld.pdf.pdfparser;

import java.io.IOException;

import info.kyorohiro.helloworld.io.VirtualMemory;

// memo 
// pdf is written at ascii character with 0-128 and byte stream.
public class PdfLexer {
	public int ID_WHITESPACE = 0;
	public int ID_DELIMITER = 1;
	public int ID_REGULAR_CHARACTER = 2;
	public int ID_EOF = -1;

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

	//
	// todo todo must be to change private method 
	public Source getSource() {
		return mText;
	}


	public Token nextToken(boolean escapedWhiteSpace) throws GotoException {
		Source source = getText();
		SourcePattern pattern = null;
		W: while (true) {
			pattern = mWhiteSpace;
			if (source.muchHead(pattern)) {
				if (escapedWhiteSpace) {
					continue W;
				}
				return new Token(ID_WHITESPACE, pattern.muchedData());
			}
			pattern = mDelimiter;
			if (source.muchHead(pattern)) {
				return new Token(ID_DELIMITER, pattern.muchedData());
			}
			pattern = mRegularCharacter;
			if (source.muchHead(pattern)) {
				return new Token(ID_REGULAR_CHARACTER, pattern.muchedData());
			}
			break;
		}
		return new Token(ID_EOF, new byte[] { (byte) 0xFF });
	}

	public Token nextToken(SourcePattern pattern, boolean escapedWhiteSpace) throws GotoException {
		Source source = getText();
		W: while (true) {
			if (!escapedWhiteSpace&&source.muchHead(mWhiteSpace)) {
				continue W;
			}
			if (source.muchHead(pattern)) {
				return new Token(ID_DELIMITER, pattern.muchedData());
			}
			break;
		}
		throw new GotoException();
	}

	public static class WhiteSpace extends BytePattern {
		public WhiteSpace() {
			super(new byte[][] { { 0x00 }, { 0x09 }, { 0x0A }, { 0x0C },
					{ 0x0D }, { 0x20 } });
		}
	}

	public static class Delimiter extends BytePattern {
		public Delimiter() {
			super(new byte[][] { { 0x28 }, { 0x29 }, { 0x3C }, { 0x3E },
					{ 0x5B }, { 0x5D }, { 0x7B }, { 0x7D } });
		}
	}

	public static class RegularCharacter extends BytePattern {
		public RegularCharacter() {
			super(new byte[][] { createByte((byte) 0x01, (byte) 0x08),
					{ 0x0b }, createByte((byte) 0x0e, (byte) 0x1f),
					createByte((byte) 0x21, (byte) 0x24),
					createByte((byte) 0x26, (byte) 0x27),
					createByte((byte) 0x2a, (byte) 0x3b), { 0x3d },
					createByte((byte) 0x3f, (byte) 0x5a), { 0x5c },
					createByte((byte) 0x5e, (byte) 0x7a), { 0x7c }, { 0x7E },
					{ 0x25 } });
		}
	}

	public static class BytePattern extends SourcePattern {
		private byte[][] mList = null;
		private byte[] mPattern = null;
		private long mPoint = 0;
		private boolean mIsLine = false;
		private long mStart = 0;
		private long mEnd = 0;
		

		public BytePattern(final byte[][] pattern) {
			mList = pattern;
		}

		public BytePattern(final byte[][] pattern, boolean isLine) {
			mList = pattern;
			mIsLine = isLine;
		}

		@Override
		public boolean matchHead(VirtualMemory source) {
			mPoint = -1;
			try {
				mStart = mPoint = source.getFilePointer();
				C:for (int i=0; i < mList.length; i++) {
					mPattern = mList[i];
					for (byte b : mPattern) {
	 					if (b != source.read()) {
							back(source);
							continue C;
						}
					}
					if (!mIsLine) {
						break;
					}
				}
				mEnd = source.getFilePointer();
				if (mStart == mEnd) {
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		public void back(VirtualMemory source) {
			try {
				if (mPoint < 0) {
					return;
				}
				source.seek(mPoint);
				mPoint = -1;
				mStart = 0;
				mEnd = 0;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public byte[] muchedData() {
			byte[] ret = new byte[mPattern.length];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = mPattern[i];
			}
			return ret;
		}
		
		public long start() {
			return mStart;
		}

		public long end() {
			return mEnd;
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
