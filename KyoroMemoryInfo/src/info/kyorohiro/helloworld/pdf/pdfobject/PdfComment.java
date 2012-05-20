package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdf.pdfparser.Token;

public class PdfComment extends Token {

	public PdfComment(int type, byte[] buffer) {
		super(type, buffer);
	}

	public static PdfObjectCreator builder = new Builder();
	public static class Builder implements PdfObjectCreator {
		@Override
		public Token createToken(PdfParser parser) throws GotoException {
			return value(parser);
		}

		private Token newPdfName(){
			PdfComment token = new PdfComment(999, null);
			return token;
		}

		public Token value(PdfParser parser) throws GotoException{
			if(false){// dummy
			}else if(value_1(parser)){
			}else{throw new GotoException();
			}
			Token t = newPdfName();
			t.add(parser.mCashForWork.pop());
			return t;
		}

		private boolean value_1(PdfParser parser){
			parser.getStack().mark();
			try {
				parser.push(parser.next(
						0,
						0xFF,
						false
						));
				return true;
			} catch(GotoException e) {
				return false;
			} finally {
				parser.getStack().release();				
			}
		}
	}
}
