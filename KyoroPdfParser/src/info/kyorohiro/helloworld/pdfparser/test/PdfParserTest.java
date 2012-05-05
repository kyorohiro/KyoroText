package info.kyorohiro.helloworld.pdfparser.test;

import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.LookaheadParser;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import junit.framework.TestCase;

public class PdfParserTest extends TestCase {

	public void testHello() {
		android.util.Log.v("pdfparser","hello");
	}

	public void testBasic00() {
		PdfLexer lexer = new PdfLexer(new Text(TestData._TEST_INDEX));
		PdfParser parser = new PdfParser(lexer);
		//parser.trySetIndex();
	}

	public static String _TEST_DATA = 
		"1 0 obj % page object - page 1\n"+
		"<<\n"+
		" /Type /Page\n"+
		" /Parent 7 0 R       % back pointer\n"+
		" /Resources 3 0 R    % font to use\n"+
		" /Contents 2 0 R     % page image\n"+
		">>\n";
}
