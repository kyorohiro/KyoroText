package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.HexadecimalString;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.HexadecimalStringBegin;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.HexadecimalStringEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.IntegerValue;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralString;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralStringBegin;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralStringEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralString_EscapeBeginEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.PlusMinus;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class PdfHexadecimalString extends Token {

	private static HexadecimalStringBegin sHexadecimalStringBegin = new HexadecimalStringBegin();
	private static HexadecimalStringEnd sHexadecimalStringEnd = new HexadecimalStringEnd();
	private static HexadecimalString sHexadecimalString = new HexadecimalString();

	private static PlusMinus sPlusMinus = new PlusMinus();

	public PdfHexadecimalString(long start, long end) {
		super(PdfLexer.ID_HEXADECIMAL_STRING, start, end);
	}

	public static PdfObjectCreator builder = new Builder();

	public static class Builder implements PdfObjectCreator {

		public Builder() {
		}

		public Token newToken(long start, long end) {
			return new PdfHexadecimalString(start, end);
		}

		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		public Token value(PdfParser parser) throws GotoException {
			Token t = null;
			if (false) {// dummy
			} else if (null != (t = pattern_value(parser))) {
			} else {
				throw new GotoException();
			}
			return t;
		}

		private Token pattern_value(PdfParser parser) {
			parser.getStack().mark();
			parser.getLexer().mark();
			try {
				pattern_string(parser);
				Token t = newToken(0, 0);
				while (parser.getStack().isMarked()) {
					t.addFirst(parser.mCashForWork.pop());
				}
				parser.getStack().releaseMarkOnly();
				return t;
			} catch (GotoException e) {
				parser.getLexer().backToMark();
				parser.getStack().release();
				return null;
			} finally {
				parser.getLexer().releaseMark();
			}
		}

		private void pattern_string(PdfParser parser) throws GotoException {
			parser.getStack().mark();
			parser.getLexer().mark();
			try {
				parser.getStack().push(
						parser.getLexer().nextToken(sHexadecimalStringBegin, true));
				pattern_content(parser);
				parser.getStack().push(
						parser.getLexer().nextToken(sHexadecimalStringEnd, true));
				// return true;
				parser.getStack().releaseMarkOnly();
			} catch (GotoException e) {
				parser.getLexer().backToMark();
				parser.getStack().release();
				throw e;
				// return false;
			} finally { 
				parser.getLexer().releaseMark();
			}
		}

		private void pattern_content(PdfParser parser) throws GotoException {
			parser.getStack().mark();
			parser.getLexer().mark();
			try {
				parser.getStack().push(parser.getLexer().nextToken(sHexadecimalString, false));
				parser.getStack().releaseMarkOnly();
			} catch (GotoException e) {
				parser.getLexer().backToMark();
				parser.getStack().release();
			} finally { 
				parser.getLexer().releaseMark();
			}

		}

	}
}
