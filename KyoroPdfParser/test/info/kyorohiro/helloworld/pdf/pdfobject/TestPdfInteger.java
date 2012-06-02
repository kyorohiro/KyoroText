package info.kyorohiro.helloworld.pdf.pdfobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Source;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import junit.framework.TestCase;

public class TestPdfInteger extends TestCase {
	public void test001() throws UnsupportedEncodingException {
		try {
			Source text = new Source("./test/info/kyorohiro/helloworld/pdf/pdfobject/pdfinteger.txt");
			PdfLexer lexer = new PdfLexer(text);
			PdfParser parser = new PdfParser(lexer);


			{
				PdfInteger t1 = (PdfInteger)PdfInteger.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"012345", new String(value1));
			}
			{
				PdfInteger t2 = (PdfInteger)PdfInteger.builder.createToken(parser);
				byte[] value2 = t2.getChild(0).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value2),"67890", new String(value2));
			}
			{
				PdfInteger t3 = (PdfInteger)PdfInteger.builder.createToken(parser);
				byte[] value3 = t3.getChild(0).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value3),"321", new String(value3));
			}
		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);			
		}
	}

}
