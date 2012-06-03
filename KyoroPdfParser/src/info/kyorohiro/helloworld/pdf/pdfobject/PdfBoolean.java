package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.BooleanValue;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;

public class PdfBoolean extends Token {

	private static BooleanValue sBooleanValue = new BooleanValue();

	public PdfBoolean(long start, long end) {
		super(PdfLexer.ID_BOOLEAN, start, end);
	}

	public static PdfObjectCreator builder = new Builder();
		
	public static class Builder extends EasyPdfObjectCreator {
		public Builder() {
			super(new SourcePattern[]{sBooleanValue}, new boolean[]{true});
		}
		@Override
		public Token newToken(long start, long end) {
			return new PdfBoolean(start, end);
		}
	}
}
