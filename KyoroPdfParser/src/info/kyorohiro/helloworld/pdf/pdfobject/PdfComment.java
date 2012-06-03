package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.ExcludeEOL;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.Persent;

public class PdfComment extends Token {

	private static Persent sPatternPersent = new Persent();
	private static ExcludeEOL sEolText = new ExcludeEOL();

	public PdfComment(long start, long end) {
		super(PdfLexer.ID_COMMENT, start, end);
	}

	public static PdfObjectCreator builder = new Builder();
	public static class Builder extends EasyPdfObjectCreator {
		public Builder() {
			super(new SourcePattern[]{
					sPatternPersent, sEolText},
					new boolean[]{true, false});
		}
		@Override
		public Token newToken(long start, long end) {
			return new PdfComment(start, end);
		}
	}
//*/
/*
	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		public Token value(PdfParser parser) throws GotoException {
			Token t = null;
			if (false) {// dummy
			} else if (null !=(t=pattern_a(parser))) {
			} else {
				throw new GotoException();
			}
			return t;
		}

		private Token pattern_a(PdfParser parser){
			parser.getStack().mark();
			try {
				parser.getStack().push(parser.getLexer().nextToken(sPatternPersent, true));
				parser.getStack().push(parser.getLexer().nextToken(sEolText, false));
				Token t = new PdfComment(PdfLexer.ID_COMMENT, 0, 0);
				t.addFirst(parser.mCashForWork.pop());
				t.addFirst(parser.mCashForWork.pop());
				return t;
			} catch(GotoException e) {
				return null;
			} finally {
				parser.getStack().release();
			}
		}
	}
//*/
}
