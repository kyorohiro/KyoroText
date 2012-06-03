package info.kyorohiro.helloworld.pdf.pdfobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Source;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import junit.framework.TestCase;

public class TestPdfLiteralString extends TestCase {

//	(This is a string)
//	(Strings may contain newlines
//	and such.)
//	(Strings may contain balanced parentheses ( ) and
//	special characters (*!&}^% and so on).)
//	(The following is an empty string.)
//	()
	
	public void test001() throws UnsupportedEncodingException {
		try {
			Source text = new Source("./test/info/kyorohiro/helloworld/pdf/pdfobject/pdfliteralstring.txt");
			PdfLexer lexer = new PdfLexer(text);
			PdfParser parser = new PdfParser(lexer);
			{
				//(This is a string)
				PdfLiteralString t1 = (PdfLiteralString)PdfLiteralString.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t1.getChild(2).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"(", new String(value1));
				assertEquals(""+new String(value2),"This is a string", new String(value2));
				assertEquals(""+new String(value3),")", new String(value3));
			}
			{
				//(Strings may contain newlines
				//and such.)
				PdfLiteralString t1 = (PdfLiteralString)PdfLiteralString.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t1.getChild(2).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"(", new String(value1));
				assertEquals(""+new String(value2),"Strings may contain newlines\r\nand such.", new String(value2));
				assertEquals(""+new String(value3),")", new String(value3));
			}
			{
				//(aa(b)cc)
				PdfLiteralString t1 = (PdfLiteralString)PdfLiteralString.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t1.getChild(2).getBuffer(text.getVirtualMemory());
				byte[] value4 = t1.getChild(3).getBuffer(text.getVirtualMemory());
				byte[] value5 = t1.getChild(4).getBuffer(text.getVirtualMemory());
				byte[] value6 = t1.getChild(5).getBuffer(text.getVirtualMemory());
				byte[] value7 = t1.getChild(6).getBuffer(text.getVirtualMemory());

				assertEquals(""+new String(value1),"(", new String(value1));
				assertEquals(""+new String(value2),"aa", new String(value2));
				assertEquals(""+new String(value3),"(", new String(value3));
				assertEquals(""+new String(value4),"b", new String(value4));
				assertEquals(""+new String(value5),")", new String(value5));
				assertEquals(""+new String(value6),"cc", new String(value6));
				assertEquals(""+new String(value7),")", new String(value7));

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
