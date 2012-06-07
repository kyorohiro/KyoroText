package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.ArrayBegin;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.ArrayEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.IntegerValue;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralString;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralStringBegin;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralStringEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralString_EscapeBeginEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.PlusMinus;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class PdfValue extends Token {

	public PdfValue(long start, long end) {
		super(PdfLexer.ID_VALUE, start, end);
	}

	public static PdfObjectCreator builder = new Builder();

	public static class Builder implements PdfObjectCreator {

		public Builder() {
		}

		public Token newToken(long start, long end) {
			return new PdfValue(start, end);
		}

		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		public Token value(PdfParser parser) throws GotoException {
			Token t = null;
			if (false) {// dummy
			} else if (null != (t = pattern(parser))) {
			} else {
				throw new GotoException();
			}
			return t;
		}

		private Token pattern(PdfParser parser) {
			parser.getStack().mark();
			parser.getLexer().mark();
			try {
				pattern_value(parser);
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

		private void pattern_value(PdfParser parser) throws GotoException {
			parser.getStack().mark();
			parser.getLexer().mark();
			try {
				do {
					try {
						PdfBoolean.builder.createToken(parser);
						break;
					} catch (GotoException e) {e.printStackTrace();}
					try {
						PdfInteger.builder.createToken(parser);
						break;
					} catch (GotoException e) {e.printStackTrace();}
					try {
						PdfReal.builder.createToken(parser);
						break;
					} catch (GotoException e) {e.printStackTrace();}
					try {
						PdfLiteralString.builder.createToken(parser);
						break;
					} catch (GotoException e) {e.printStackTrace();}
					try {
						PdfHexadecimalString.builder.createToken(parser);
						break;
					} catch (GotoException e) {e.printStackTrace();}
					try {
						PdfName.builder.createToken(parser);
						break;
					} catch (GotoException e) {e.printStackTrace();}
					parser.getLexer().backToMark();
					parser.getStack().release();
					throw new GotoException();
				}while(false);
				// return false;
			} finally { 
				parser.getLexer().releaseMark();
			}
		}
	}
}
