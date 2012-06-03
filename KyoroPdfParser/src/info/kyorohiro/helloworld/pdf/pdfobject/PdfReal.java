package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.Dot;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.IntegerValue;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.PlusMinus;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class PdfReal extends Token {

	private static IntegerValue sIntegerValue = new IntegerValue();
	private static PlusMinus sPlusMinus = new PlusMinus();
	private static Dot sDot = new Dot();

	public PdfReal(long start, long end) {
		super(PdfLexer.ID_REAL, start, end);
	}

	public static PdfObjectCreator builder = new Builder();

	public static class Builder implements PdfObjectCreator {

		public Builder() {
		}

		public Token newToken(long start, long end) {
			return new PdfReal(start, end);
		}
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		public Token value(PdfParser parser) throws GotoException {
			Token t = null;
			if (false) {// dummy
			} else if (null !=(t=pattern_a(parser))) {
			} else if (null !=(t=pattern_b(parser))) {
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
				parser.getStack().push(parser.getLexer().nextToken(sIntegerValue, true));
				parser.getStack().push(parser.getLexer().nextToken(sDot, false));
				try {
					parser.getStack().push(parser.getLexer().nextToken(sIntegerValue, false));
				} catch(GotoException e) {
					e.printStackTrace();
				}
				Token t = newToken(0,0);
				while(parser.getStack().isMarked()) {
					t.addFirst(parser.mCashForWork.pop());
				}
				return t;
			} catch(Exception e) {
				parser.getLexer().backToMark();
				return null;
			}finally {
				parser.getStack().release();
				parser.getLexer().releaseMark();
			}
		}

		private Token pattern_b(PdfParser parser){
			parser.getStack().mark();
			try {
				try {
					parser.getStack().push(parser.getLexer().nextToken(sPlusMinus, true));
				} catch(GotoException e) {
					e.printStackTrace();
				}
				parser.getStack().push(parser.getLexer().nextToken(sDot, true));
				parser.getStack().push(parser.getLexer().nextToken(sIntegerValue, false));

				Token t = newToken(0,0);
				for(int i=0;parser.getStack().isMarked();i++) {
					t.addFirst(parser.mCashForWork.pop());
				}
				return t;
			} catch(Exception e) {
				return null;
			}finally {
				parser.getStack().release();
			}
		}

	
	}
}
