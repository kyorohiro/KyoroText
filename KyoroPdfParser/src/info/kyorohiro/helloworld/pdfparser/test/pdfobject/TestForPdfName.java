package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfAscii;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfName;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfName extends TestCase {
	public static String sTestData001 = "/abcxyz \n"+"/ABCXYZ";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfName.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try { 
			PdfName pars1 = (PdfName)PdfName.builder.createToken(parser);
			android.util.Log.v("pdfparser","---1--");
			PdfName pars2 = (PdfName)PdfName.builder.createToken(parser);
			assertEquals(1, pars1.numOfChild());
			assertEquals(1, pars2.numOfChild());
			assertEquals("abcxyz", pars1.parseString());
			assertEquals("ABCXYZ", pars2.parseString());
		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
