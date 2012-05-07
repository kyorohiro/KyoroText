package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfBoolean extends Token {

	public PdfBoolean(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public boolean isTrue() {
		if(numOfChild()!= 0&&"true".equals(getChild(0).mValue)){
			return true;
		}
		return false;
	}
	
	public static PdfObjectCreator builder = new Builder();
	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfBoolean(){
			PdfBoolean token = new PdfBoolean(999, "<boolean>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException{
			if(false){// dummy
			}else if(value_1(parser)){
			}else{throw new GotoException();
			}
			PdfBoolean t = (PdfBoolean)newPdfBoolean();
			t.add(parser.mCashForWork.pop());
			return t;
		}

		private boolean value_1(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_BOOLEAN.mType));
				return true;
			} catch(GotoException e) {
				parser.release();
				return false;
			}
		}
	}
}
