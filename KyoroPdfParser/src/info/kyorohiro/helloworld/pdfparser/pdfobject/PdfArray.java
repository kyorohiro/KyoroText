package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfArray extends Token {
	public static PdfObjectCreator builder = new Builder();

	public PdfArray(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfArray() {
			PdfArray token = new PdfArray(999, "<index>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException {
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_ARRAY_BEGIN.mType));
				values_1(parser);
				parser.mCashForWork.push(parser.next(PdfLexer.SET_ARRAY_END.mType));
				//
				//
				Token t = newPdfArray();
				while(parser.mCashForWork.isMarked()){
					t.addFirst(parser.mCashForWork.pop());
				}
				return t;
			} finally {
				parser.release();
			}
		}

		private boolean values_1(PdfParser parser) {
			try {
				parser.mark();
				parser.mCashForWork.push(PdfValue.builder.createToken(parser));
				//
				if(!parser.isHead(PdfLexer.SET_ARRAY_END.mType)){
					values_1(parser);
				}
				return true;
			} catch (GotoException e) {
				parser.release();
				return false;
			}
		}
	}
}
