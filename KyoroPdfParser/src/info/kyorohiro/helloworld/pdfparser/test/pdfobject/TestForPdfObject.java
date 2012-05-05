package info.kyorohiro.helloworld.pdfparser.test.pdfobject;

import info.kyorohiro.helloworld.pdfparser.GotoException;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfIndex;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfObject;
import info.kyorohiro.helloworld.pdfparser.pdfobject.PdfValue;
import junit.framework.TestCase;

public class TestForPdfObject extends TestCase {
	public static String sTestData001 = 
		"0 1 obj"+
		"<< "+
		"/Type 0 2 R\n"+
		"/Type /Page\n"+
		"/Type abvde "+
		"/Type (abcdef) "+
		" >> "+
		"endobj";

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestForPdfObject.sTestData001));
		PdfParser parser = new PdfParser(lexer);
		try {
			PdfObject pars1 = (PdfObject)PdfObject.builder.createToken(parser);
			int num = pars1.numOfChild();

			android.util.Log.v("pdfparser","-----------");			
			for(int i=0;i<num;i++){
				android.util.Log.v("pdfparser",""+i+"="+pars1.getChild(i).mType);
				android.util.Log.v("pdfparser",""+i+"="+pars1.getChild(i).mText);
				android.util.Log.v("pdfparser",""+i+"="+pars1.getChild(i).mPattern);
				android.util.Log.v("pdfparser",""+i+"="+pars1.getChild(i).mValue);
			}
			assertEquals(5, pars1.numOfChild());
			assertEquals(6, pars1.getChild(3).getChild(0).numOfChild());
		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
