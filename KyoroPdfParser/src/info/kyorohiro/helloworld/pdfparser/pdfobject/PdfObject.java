package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfObject extends Token {
	public static PdfObjectCreator builder = new Builder();

	public PdfObject(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfObject() {
			PdfObject token = new PdfObject(999, "<object>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException {
			try {
				parser.mark();
				parser.mCashForWork.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NUMBER.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NUMBER.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_OBJECT_BEGIN.mType));

				android.util.Log.v("pdfparser","---00--");
				parser.mCashForWork.testPrint();
				values_1(parser);
				android.util.Log.v("pdfparser","---01--");
				parser.mCashForWork.testPrint();
			
				parser.mCashForWork.push(parser.next(PdfLexer.SET_OBJECT_END.mType));
				//
				//
				Token t = newPdfObject();
				while(parser.mCashForWork.isMarked()){
					t.addFirst(parser.mCashForWork.pop());
				}
				return t;
			} finally {
				parser.mCashForWork.release();
				parser.release();
				android.util.Log.v("pdfparser","---02--");
				parser.mCashForWork.testPrint();
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
