package info.kyorohiro.helloworld.pdf.pdfobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Source;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import junit.framework.TestCase;

public class TestPdfComment extends TestCase {
	public void test001() throws UnsupportedEncodingException {
		try {
			Source text = new Source("./test/info/kyorohiro/helloworld/pdf/pdfobject/pdfcomment.txt");
			PdfLexer lexer = new PdfLexer(text);
			PdfParser parser = new PdfParser(lexer);


			PdfComment t1 = (PdfComment)PdfComment.builder.createToken(parser);
			String persent = new String(t1.getChild(0).getBuffer(text.getVirtualMemory()));
			byte[] comment = t1.getChild(1).getBuffer(text.getVirtualMemory());
			assertEquals("%", persent);
			assertEquals(""+new String(comment),"comment1  \r", new String(comment));
			
			PdfComment t2 = (PdfComment)PdfComment.builder.createToken(parser);			
			String persent2 = new String(t2.getChild(0).getBuffer(text.getVirtualMemory()));
			byte[] comment2 = t2.getChild(1).getBuffer(text.getVirtualMemory());
			assertEquals("%", persent2);
			assertEquals(""+new String(comment2),"comment2", new String(comment2));


		} catch (GotoException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			assertTrue(false);			
		}
	}

}
