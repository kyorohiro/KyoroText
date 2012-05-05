package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfProperty;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfProperty extends TestCase {
	public static String sTestData001 = 
		"/Type 0 2 R\n"+
		"/Type /Page\n"+
		"/Type abvde "+
		"/Type (abcdef)";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfProperty.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try {
			PdfProperty pars1 = (PdfProperty)PdfProperty.builder.createToken(parser);
			PdfProperty pars2 = (PdfProperty)PdfProperty.builder.createToken(parser);
			PdfProperty pars3 = (PdfProperty)PdfProperty.builder.createToken(parser);
			PdfProperty pars4 = (PdfProperty)PdfProperty.builder.createToken(parser);

			assertEquals(2, pars1.numOfChild());
			assertEquals(3, pars1.getChild(1).numOfChild());
			assertEquals(2, pars2.numOfChild());
			assertEquals(2, pars3.numOfChild());
			assertEquals(2, pars4.numOfChild());

		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
