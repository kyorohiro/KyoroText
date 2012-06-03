package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.BooleanValue;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.NameDelimiter;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.RegularCharacter;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer.RegularString;
import info.kyorohiro.helloworld.pdf.pdflexer.SourcePattern;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;

public class PdfName extends Token {

	private static NameDelimiter sNameDelimiter = new NameDelimiter();
	private static RegularString sRegularString = new RegularString();

	public PdfName(long start, long end) {
		super(PdfLexer.ID_NAME, start, end);
	}

	public static PdfObjectCreator builder = new Builder();
		
	public static class Builder extends EasyPdfObjectCreator {
		public Builder() {
			super(new SourcePattern[]{sNameDelimiter, sRegularString},
					new boolean[]{true, false});
		}
		@Override
		public Token newToken(long start, long end) {
			return new PdfName(start, end);
		}
	}
}
