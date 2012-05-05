package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfArray;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfIndex;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfArray extends TestCase {
	public static String sTestData001 = 
		"[ "+
		"/Type "+
		"0 2 R\n"+
		"/Type "+
		"abvde "+
		"(abcdef) "+
		" ]";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfArray.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try {
			PdfArray pars1 = (PdfArray)PdfArray.builder.createToken(parser);
			assertEquals(7, pars1.numOfChild());
		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
