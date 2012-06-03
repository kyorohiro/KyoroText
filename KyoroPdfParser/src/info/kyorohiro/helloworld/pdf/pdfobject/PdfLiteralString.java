package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.IntegerValue;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralString;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralStringBegin;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralStringEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.LiteralString_EscapeBeginEnd;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.PlusMinus;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class PdfLiteralString extends Token {

	private static LiteralStringBegin sLiteralStringBegin = new LiteralStringBegin();
	private static LiteralStringEnd sLiteralStringEnd = new LiteralStringEnd();
	private static LiteralString sLiteralString = new LiteralString();
	private static LiteralString_EscapeBeginEnd sLiteralStringEscape = new LiteralString_EscapeBeginEnd();

	private static PlusMinus sPlusMinus = new PlusMinus();

	public PdfLiteralString(long start, long end) {
		super(PdfLexer.ID_LITERAL_STRING, start, end);
	}

	public static PdfObjectCreator builder = new Builder();

	public static class Builder implements PdfObjectCreator {

		public Builder() {
		}

		public Token newToken(long start, long end) {
			return new PdfLiteralString(start, end);
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
						parser.getLexer().nextToken(sLiteralStringBegin, true));
				pattern_content(parser);
				parser.getStack().push(
						parser.getLexer().nextToken(sLiteralStringEnd, true));
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
				//
				try {
					parser.getStack().push(parser.getLexer().nextToken(sLiteralString, true));				
				} catch(GotoException e) {
					try {
						pattern_string(parser);
					} catch(GotoException f) {
						parser.getStack().push(parser.getLexer().nextToken(sLiteralStringEscape, true));		
					}
				}

				try {
					parser.getStack().push(parser.getLexer().nextToken(sLiteralStringEscape,false));
				} catch (GotoException e) {}
				try {
					pattern_content(parser);
				} catch (GotoException e) {}
				try {
					pattern_string(parser);
				} catch (GotoException e) {}
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
