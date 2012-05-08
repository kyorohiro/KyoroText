package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfString;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfBoolean;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfBoolean extends TestCase {
	public static String sTestData001 = "true \n"+"false ";
	public static String sTestData002 = "tr";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfBoolean.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try { 
			PdfBoolean pars1 = (PdfBoolean)PdfBoolean.builder.createToken(parser);
			android.util.Log.v("pdfparser","---1--");
			PdfBoolean pars2 = (PdfBoolean)PdfBoolean.builder.createToken(parser);
			assertEquals(1, pars1.numOfChild());
			assertEquals(1, pars2.numOfChild());
			assertEquals(true, pars1.parseBoolean());
			assertEquals(false, pars2.parseBoolean());
		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testBasic01() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfBoolean.sTestData002));
		PdfParser parser = new PdfParser(lexer);
		try { 
			PdfBoolean pars1 = (PdfBoolean)PdfBoolean.builder.createToken(parser);
			assertTrue(false);
		} catch (GotoException e) {
			assertTrue(true);
		}
	}
}
