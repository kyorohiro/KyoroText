package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.ExcludeEOL;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.Persent;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class EasyPdfObjectCreator implements PdfObjectCreator {
	private SourcePattern[] mPattern = null;
	private boolean[] mEscapeWhiteSpace = null;
	private int mId = 0;

	public EasyPdfObjectCreator(SourcePattern[] pattern, boolean[] escapeWhiteSpace) {
		mPattern = pattern;
		mEscapeWhiteSpace = escapeWhiteSpace;
	}

	public Token newToken(long start, long end) {
		return new Token(0/*todo*/, start, end);
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
		try {
			for(int i=0;i<mPattern.length;i++) {
				parser.getStack().push(parser.getLexer().nextToken(mPattern[i], mEscapeWhiteSpace[i]));
			}
			Token t = newToken(0,0);
			for(int i=0;i<mPattern.length;i++) {
				t.addFirst(parser.mCashForWork.pop());
			}
			return t;
		} catch(GotoException e) {
			return null;
		} finally {
			parser.getStack().release();
		}
	}
}
