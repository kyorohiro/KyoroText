package info.kyorohiro.helloworld.pdf.pdfobject;

import info.kyorohiro.helloworld.pdf.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdf.pdfparser.Token;


public interface PdfObjectCreator {
	Token createToken(PdfParser parser) throws GotoException;
}
