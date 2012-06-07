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

public class PdfArray extends Token {

	private static ArrayBegin sArrayBegin = new ArrayBegin();
	private static ArrayEnd sArrayEnd = new ArrayEnd();

	private static PlusMinus sPlusMinus = new PlusMinus();

	public PdfArray(long start, long end) {
		super(PdfLexer.ID_LITERAL_STRING, start, end);
	}

	public static PdfObjectCreator builder = new Builder();

	public static class Builder implements PdfObjectCreator {

		public Builder() {
		}

		public Token newToken(long start, long end) {
			return new PdfArray(start, end);
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
				pattern_array(parser);
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

		private void pattern_array(PdfParser parser) throws GotoException {
			parser.getStack().mark();
			parser.getLexer().mark();
			try {
				parser.getStack().push(
						parser.getLexer().nextToken(sArrayBegin, true));
				pattern_content(parser);
				parser.getStack().push(
						parser.getLexer().nextToken(sArrayEnd, true));
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

		// content = string [^escape]* | literalstring
		// literalstring "(" content ")"
		// escape = < "(" ")" "\(" (\)" ˆÈŠO‚Ì•¶Žš—ñ‚ðŠÜ‚Þ•¶Žš—ñ>;
		private void pattern_content(PdfParser parser) throws GotoException {
			parser.getStack().mark();
			parser.getLexer().mark();
			try {
				try {
					PdfValue.builder.createToken(parser);
					try {
						pattern_content(parser);
					} catch (GotoException e) {
						e.printStackTrace();
					}
				} catch (GotoException e) {
					e.printStackTrace();

					// parser.getStack().push(parser.getLexer().nextToken(sArrayBegin,
					// true));
					parser.getStack().releaseMarkOnly();
					parser.getLexer().backToMark();
					parser.getStack().release();
				}
			} finally {
				parser.getLexer().releaseMark();
			}

		}

	}
}
