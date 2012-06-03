package info.kyorohiro.helloworld.pdf.pdfparser;

import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;

//thread unsafe.
public class PdfParser {

	// work cash in any methodÅB
	// this field is only used by PdfObjectCreator's class.
	public PdfStack mCashForWork = new PdfStack();
	private PdfLexer mLexer = null;

	public PdfParser(PdfLexer lexer) {
		mLexer = lexer;
	}

	public PdfLexer getLexer() {
		return mLexer;
	}

	public PdfStack getStack() {
		return mCashForWork;
	}

}
