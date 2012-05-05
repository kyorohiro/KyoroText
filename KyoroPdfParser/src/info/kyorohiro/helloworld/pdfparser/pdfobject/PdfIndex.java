package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfIndex extends Token {
	public static PdfObjectCreator builder = new Builder();

	public PdfIndex(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfIndex() {
			PdfIndex token = new PdfIndex(999, "<index>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException {
			try {
				parser.mark();
				parser.mCashForWork.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_INDEX_BEGIN.mType));

				android.util.Log.v("pdfparser","---1--");
				parser.mCashForWork.testPrint();
				properties_1(parser);
				android.util.Log.v("pdfparser","---2--");
				parser.mCashForWork.testPrint();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_INDEX_END.mType));
				//
				//
				Token t = newPdfIndex();
				android.util.Log.v("pdfparser","---3--");
				parser.mCashForWork.testPrint();
				
				//
				//
				while(parser.mCashForWork.isMarked()){
					t.addFirst(parser.mCashForWork.pop());
				}
				android.util.Log.v("pdfparser","---4--");
				parser.mCashForWork.testPrint();
				return t;
			} finally {
				parser.mCashForWork.release();
				parser.release();
				android.util.Log.v("pdfparser","---5--");
				parser.mCashForWork.testPrint();
			}
		}

		private boolean properties_1(PdfParser parser) {
			try {
				parser.mark();
				if(parser.isHead(PdfLexer.SET_NAME.mType)){
					parser.mCashForWork.push(PdfProperty.builder.createToken(parser));
				}
				//
				if(parser.isHead(PdfLexer.SET_NAME.mType)){
					properties_1(parser);
				}
				return true;
			} catch (GotoException e) {
				parser.release();
				return false;
			}
		}
	}
}
