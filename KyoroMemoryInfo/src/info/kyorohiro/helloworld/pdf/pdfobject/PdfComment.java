package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.BytePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.ExcludeEOL;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.Persent;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class PdfComment extends Token {

	private static Persent sPatternPersent = new Persent();
	private static ExcludeEOL sEolText = new ExcludeEOL();

	public PdfComment(byte[] buffer, long start, long end) {
		super(PdfLexer.ID_COMMENT, buffer, start, end);
	}

	public static PdfObjectCreator builder = new Builder();
	
	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		public Token value(PdfParser parser) throws GotoException {
			if (false) {// dummy
			} else if (pattern_a(parser)) {
			} else {
				throw new GotoException();
			}
			// Token�̎�ނ𑝂₷
			// Atom 
			// Object
			// Container
			//Token t = newPdfName();
			// t.add(parser.mCashForWork.pop());
			// return t;
			return null;
		}

		private boolean pattern_a(PdfParser parser){
			parser.getStack().mark();
			try {
				parser.getStack().push(parser.getLexer().nextToken(sPatternPersent, true));
				parser.getStack().push(parser.getLexer().nextToken(sEolText, false));
				return true;
			} catch(GotoException e) {
				parser.getStack().release();
				return false;
			}
		}
	}
}
