package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfAscii extends Token {
	public static PdfObjectCreator builder = new Builder();

	public PdfAscii(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfAscii(){
			PdfAscii token = new PdfAscii(999, "<ascii>", "unuse");
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
				parser.mCashForWork.push(parser.next(PdfLexer.SET_ASCII_BEGIN.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_IDENTIFY.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_ASCII_END.mType));
				Token token = newPdfAscii();
				token.addFirst(parser.mCashForWork.pop());
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
