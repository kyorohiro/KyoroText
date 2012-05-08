package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfString;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfBoolean;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfNumber;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfNumber extends TestCase {
	public static String sTestData001 = "0 \n"+"0.1 +1 -1 .12 42.";
	public static String sTestData002 = "tr";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfNumber.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try { 
			PdfNumber pars1 = (PdfNumber)PdfNumber.builder.createToken(parser);
			android.util.Log.v("pdfparser","---1--");
			PdfNumber pars2 = (PdfNumber)PdfNumber.builder.createToken(parser);
			android.util.Log.v("pdfparser","---2--");
			PdfNumber pars3 = (PdfNumber)PdfNumber.builder.createToken(parser);
			android.util.Log.v("pdfparser","---3--");
			PdfNumber pars4 = (PdfNumber)PdfNumber.builder.createToken(parser);
			android.util.Log.v("pdfparser","---4--");
			PdfNumber pars5 = (PdfNumber)PdfNumber.builder.createToken(parser);
			android.util.Log.v("pdfparser","---5--");
			PdfNumber pars6 = (PdfNumber)PdfNumber.builder.createToken(parser);
			assertEquals(0.0, pars1.parseDouble());
			assertEquals(0.1, pars2.parseDouble());
			assertEquals(1.0, pars3.parseDouble());
			assertEquals(-1.0, pars4.parseDouble());
			assertEquals(0.12, pars5.parseDouble());
			assertEquals(42.0, pars6.parseDouble());
		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	public void testBasic01() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfNumber.sTestData002));
		PdfParser parser = new PdfParser(lexer);
		try { 
			PdfNumber pars1 = (PdfNumber)PdfNumber.builder.createToken(parser);
			assertTrue(false);
		} catch (GotoException e) {
			assertTrue(true);
		}
	}
}
