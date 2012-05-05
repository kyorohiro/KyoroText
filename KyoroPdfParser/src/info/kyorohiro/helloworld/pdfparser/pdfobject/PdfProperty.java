package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfProperty extends Token {
	public static PdfObjectCreator builder = new Builder();

	public PdfProperty(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfProperty(){
			PdfProperty token = new PdfProperty(999, "<value>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException{
			if(false){// dummy
			}else if(value_1(parser)){
			}else{throw new GotoException();
			}
			return parser.mCashForWork.pop();
		}

		private boolean value_1(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NAME.mType));
				parser.mCashForWork.push(PdfValue.builder.createToken(parser));
				Token token = newPdfProperty();
				token.addFirst(parser.mCashForWork.pop());
				token.addFirst(parser.mCashForWork.pop());
				parser.mCashForWork.push(token);
				return true;
			} catch(GotoException e) {
				parser.release();
				return false;
			}
		}

	}
}
