package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
public class PdfString extends Token {
	public static PdfObjectCreator builder = new Builder();

	public PdfString(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public boolean isAscii(){
		if(numOfChild()!= 0&&!getChild(0).mValue.equals("(")){
			return false;
		}
		return true;
	}

	public String parseString(){
		if(numOfChild()!= 0){
			CharSequence c = getChild(1).mValue;
			return c.toString();
		}
		return "<null>";
	}

	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfAscii(){
			PdfString token = new PdfString(999, "<string>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException{
			if(false){// dummy
			}else if(value_1(parser)){
			}else if(value_2(parser)){
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

		private boolean value_2(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NONASCII_BEGIN.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_IDENTIFY.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NONASCII_END.mType));
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
