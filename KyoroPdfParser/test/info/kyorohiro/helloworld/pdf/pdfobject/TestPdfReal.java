package info.kyorohiro.helloworld.pdf.pdfobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Source;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import junit.framework.TestCase;

public class TestPdfReal extends TestCase {
	public void test001() throws UnsupportedEncodingException {
		try {
			Source text = new Source("./test/info/kyorohiro/helloworld/pdf/pdfobject/pdfreal.txt");
			PdfLexer lexer = new PdfLexer(text);
			PdfParser parser = new PdfParser(lexer);

			//.012345
			{
				PdfReal t1 = (PdfReal)PdfReal.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),".", new String(value1));
				assertEquals(""+new String(value2),"012345", new String(value2));
			}

			// +67.890
			{
				PdfReal t2 = (PdfReal)PdfReal.builder.createToken(parser);
				byte[] value1 = t2.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t2.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t2.getChild(2).getBuffer(text.getVirtualMemory());
				byte[] value4 = t2.getChild(3).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"+", new String(value1));
				assertEquals(""+new String(value2),"67", new String(value2));
				assertEquals(""+new String(value3),".", new String(value3));
				assertEquals(""+new String(value4),"890", new String(value4));
			}
			//-321.
			{
				PdfReal t2 = (PdfReal)PdfReal.builder.createToken(parser);
				byte[] value1 = t2.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t2.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t2.getChild(2).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"-", new String(value1));
				assertEquals(""+new String(value2),"321", new String(value2));
				assertEquals(""+new String(value3),".", new String(value3));		
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
