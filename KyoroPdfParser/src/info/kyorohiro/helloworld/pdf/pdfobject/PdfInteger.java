package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.BooleanValue;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.IntegerValue;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.ExcludeEOL;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.Persent;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;

public class PdfInteger extends Token {

	private static IntegerValue sIntegerValue = new IntegerValue();

	public PdfInteger(long start, long end) {
		super(PdfLexer.ID_BOOLEAN, start, end);
	}

	public static PdfObjectCreator builder = new Builder();
		
	public static class Builder extends EasyPdfObjectCreator {
		public Builder() {
			super(new SourcePattern[]{sIntegerValue}, new boolean[]{true});
		}
		@Override
		public Token newToken(long start, long end) {
			return new PdfInteger(start, end);
		}
	}
}
