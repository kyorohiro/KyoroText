package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfName extends Token {

	public PdfName(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public String parseString() {
		if(numOfChild()!= 0){
			return getChild(0).mValue.toString().substring(1);
		}
		return "<null>";
	}
	
	public static PdfObjectCreator builder = new Builder();
	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfName(){
			PdfName token = new PdfName(999, "<boolean>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException{
			if(false){// dummy
			}else if(value_1(parser)){
			}else{throw new GotoException();
			}
			PdfName t = (PdfName)newPdfName();
			t.add(parser.mCashForWork.pop());
			return t;
		}

		private boolean value_1(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NAME.mType));
				return true;
			} catch(GotoException e) {
				parser.release();
				return false;
			}
		}
	}
}
