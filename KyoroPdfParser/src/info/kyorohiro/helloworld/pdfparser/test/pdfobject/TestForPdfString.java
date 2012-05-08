package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfString;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfString extends TestCase {
	public static String sTestData001 = "(abcxyz) \n"+"(ABCXYZ)<OMOM>";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfString.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try { 
			PdfString pars1 = (PdfString)PdfString.builder.createToken(parser);
			android.util.Log.v("pdfparser","---1--");
			PdfString pars2 = (PdfString)PdfString.builder.createToken(parser);
			android.util.Log.v("pdfparser","---2--");
			PdfString pars3 = (PdfString)PdfString.builder.createToken(parser);

			assertEquals(3, pars1.numOfChild());
			assertEquals(3, pars2.numOfChild());
			assertEquals(3, pars3.numOfChild());

			assertEquals("abcxyz", pars1.parseString());
			assertEquals("ABCXYZ", pars2.parseString());
			assertEquals("OMOM", pars3.parseString());

			assertEquals(true, pars1.isAscii());
			assertEquals(true, pars2.isAscii());
			assertEquals(false, pars3.isAscii());

		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
