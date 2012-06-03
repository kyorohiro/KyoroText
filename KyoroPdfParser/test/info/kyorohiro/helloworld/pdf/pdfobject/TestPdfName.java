package info.kyorohiro.helloworld.pdf.pdfobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Source;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import junit.framework.TestCase;

public class TestPdfName extends TestCase {


	
	public void test001() throws UnsupportedEncodingException {
		try {
			Source text = new Source("./test/info/kyorohiro/helloworld/pdf/pdfobject/pdfname.txt");
			PdfLexer lexer = new PdfLexer(text);
			PdfParser parser = new PdfParser(lexer);
			{
				///abc
				PdfName t1 = (PdfName)PdfName.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"/", new String(value1));
				assertEquals(""+new String(value2),"abc", new String(value2));
			}
			{
				///def
				PdfName t1 = (PdfName)PdfName.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"/", new String(value1));
				assertEquals(""+new String(value2),"def", new String(value2));
			}
			{
				///ghq
				PdfName t1 = (PdfName)PdfName.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"/", new String(value1));
				assertEquals(""+new String(value2),"ghq", new String(value2));
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
