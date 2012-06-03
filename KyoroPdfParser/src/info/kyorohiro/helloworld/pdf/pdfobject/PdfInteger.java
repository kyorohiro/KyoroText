package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.IntegerValue;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.PlusMinus;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class PdfInteger extends Token {

	private static IntegerValue sIntegerValue = new IntegerValue();
	private static PlusMinus sPlusMinus = new PlusMinus();

	public PdfInteger(long start, long end) {
		super(PdfLexer.ID_INTEGER, start, end);
	}

	public static PdfObjectCreator builder = new Builder();

	public static class Builder implements PdfObjectCreator {
		private SourcePattern[] mPattern = null;
		private boolean[] mEscapeWhiteSpace = null;

		public Builder() {
			mPattern = new SourcePattern[]{sIntegerValue};
			mEscapeWhiteSpace = new boolean[]{true, false};
		}

		public Token newToken(long start, long end) {
			return new PdfInteger(start, end);
		}
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
			parser.getLexer().mark();
			try {
				try {
					parser.getStack().push(parser.getLexer().nextToken(sPlusMinus, true));				
				} catch(GotoException e) {
					e.printStackTrace();
				}

				for(int i=0;i<mPattern.length;i++) {
					parser.getStack().push(parser.getLexer().nextToken(mPattern[i], mEscapeWhiteSpace[i]));
				}
				Token t = newToken(0,0);
				while(parser.getStack().isMarked()) {
					t.addFirst(parser.mCashForWork.pop());
				}
				return t;
			} catch(GotoException e) {
				parser.getLexer().backToMark();
				return null;
			} finally {
				parser.getStack().release();
				parser.getLexer().releaseMark();
			}
		}

	
	}
}
