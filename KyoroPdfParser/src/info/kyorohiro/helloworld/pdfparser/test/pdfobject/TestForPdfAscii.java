package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfAscii;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfAscii extends TestCase {
	public static String sTestData001 = "(abcxyz) \n"+"(ABCXYZ)";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfAscii.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try { 
			PdfAscii pars1 = (PdfAscii)PdfAscii.builder.createToken(parser);
			android.util.Log.v("pdfparser","---1--");
			PdfAscii pars2 = (PdfAscii)PdfAscii.builder.createToken(parser);
			assertEquals(3, pars1.numOfChild());
			assertEquals(3, pars2.numOfChild());
		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
