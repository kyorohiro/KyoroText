package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfNumber extends Token {

	public PdfNumber(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public double parseDouble() {
		if(numOfChild()!= 0){
			return Double.parseDouble(getChild(0).mValue.toString());
		}
		return 0;
	}
	
	public static PdfObjectCreator builder = new Builder();
	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfNumber(){
			PdfNumber token = new PdfNumber(999, "<boolean>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException{
			if(false){// dummy
			}else if(value_1(parser)){
			}else{throw new GotoException();
			}
			PdfNumber t = (PdfNumber)newPdfNumber();
			t.add(parser.mCashForWork.pop());
			return t;
		}

		private boolean value_1(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NUMBER.mType));
				return true;
			} catch(GotoException e) {
				parser.release();
				return false;
			}
		}
	}
}
