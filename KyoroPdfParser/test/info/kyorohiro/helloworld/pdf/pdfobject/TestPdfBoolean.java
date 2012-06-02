package info.kyorohiro.helloworld.pdf.pdfobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Source;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import junit.framework.TestCase;

public class TestPdfBoolean extends TestCase {
	public void test001() throws UnsupportedEncodingException {
		try {
			Source text = new Source("./test/info/kyorohiro/helloworld/pdf/pdfobject/pdfboolean.txt");
			PdfLexer lexer = new PdfLexer(text);
			PdfParser parser = new PdfParser(lexer);


			{
				PdfBoolean t1 = (PdfBoolean)PdfBoolean.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"true", new String(value1));
			}
			{
				PdfBoolean t2 = (PdfBoolean)PdfBoolean.builder.createToken(parser);
				byte[] value2 = t2.getChild(0).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value2),"false", new String(value2));
			}
			{
				PdfBoolean t3 = (PdfBoolean)PdfBoolean.builder.createToken(parser);
				byte[] value3 = t3.getChild(0).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value3),"true", new String(value3));
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
