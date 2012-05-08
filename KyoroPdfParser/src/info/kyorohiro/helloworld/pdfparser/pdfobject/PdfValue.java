package info.kyorohiro.helloworld.pdfparser.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Token;

//
//
// todo about this class extends Token. 
public class PdfValue extends Token {
	public static PdfObjectCreator builder = new Builder();

	public PdfValue(int type, String text, String patterm) {
		super(type, text, patterm);
	}

	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfValue(){
			PdfValue token = new PdfValue(999, "<value>", "unuse");
			return token;
		}

		public Token value(PdfParser parser) throws GotoException{
			if(false){// dummy
			}else if(value_1(parser)){
			}else if(value_2(parser)){
			}else if(value_3(parser)){
			}else if(value_5(parser)){
			}else if(value_6(parser)){
			}else if(value_7(parser)){
			}else if(value_4(parser)){
			}else{throw new GotoException();
			}
			return parser.mCashForWork.pop();
		}

		private boolean value_1(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NUMBER.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NUMBER.mType));
				parser.mCashForWork.push(parser.next(PdfLexer.SET_IDENTIFY.mType));
				Token token = newPdfValue();
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
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NUMBER.mType));
				Token token = newPdfValue();
				token.add(parser.mCashForWork.pop());
				parser.mCashForWork.push(token);
				return true;
			} catch(GotoException e) {
				parser.release();
				return false;
			}
		}

		private boolean value_3(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_NAME.mType));
				Token token = newPdfValue();
				token.add(parser.mCashForWork.pop());
				parser.mCashForWork.push(token);
				return true;
			} catch(GotoException e) {
				parser.release();
				return false;
			}
		}

		private boolean value_4(PdfParser parser){
			try {
				parser.mark();
				parser.mCashForWork.push(parser.next(PdfLexer.SET_IDENTIFY.mType));
				Token token = newPdfValue();
				token.add(parser.mCashForWork.pop());
				parser.mCashForWork.push(token);
				return true;
			} catch(GotoException e) {
				parser.release();
				return false;
			}
		}

		private boolean value_5(PdfParser parser){
			try {
				parser.mCashForWork.push(PdfString.builder.createToken(parser));
				Token token = newPdfValue();
				token.add(parser.mCashForWork.pop());
				parser.mCashForWork.push(token);
				return true;
			} catch(GotoException e) {
   				parser.release();
				return false;
			}
		}
		private boolean value_6(PdfParser parser){
			try {
				parser.mCashForWork.push(PdfIndex.builder.createToken(parser));
				Token token = newPdfValue();
				token.add(parser.mCashForWork.pop());
				parser.mCashForWork.push(token);
				return true;
			} catch(GotoException e) {
   				parser.release();
				return false;
			}
		}
		private boolean value_7(PdfParser parser){
			try {
				parser.mCashForWork.push(PdfArray.builder.createToken(parser));
				Token token = newPdfValue();
				token.add(parser.mCashForWork.pop());
				parser.mCashForWork.push(token);
				return true;
			} catch(GotoException e) {
   				parser.release();
				return false;
			}
		}

	}
}
