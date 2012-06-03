package info.kyorohiro.helloworld.pdf.pdfobject;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import info.kyorohiro.helloworld.pdf.pdflexer.GotoException;
import info.kyorohiro.helloworld.pdf.pdflexer.PdfLexer;
import info.kyorohiro.helloworld.pdf.pdflexer.Source;
import info.kyorohiro.helloworld.pdf.pdflexer.Token;
import info.kyorohiro.helloworld.pdf.pdfparser.PdfParser;
import junit.framework.TestCase;

public class TestPdfHexadecimalString extends TestCase {

//	(This is a string)
//	(Strings may contain newlines
//	and such.)
//	(Strings may contain balanced parentheses ( ) and
//	special characters (*!&}^% and so on).)
//	(The following is an empty string.)
//	()
	
	public void test001() throws UnsupportedEncodingException {
		try {
			Source text = new Source("./test/info/kyorohiro/helloworld/pdf/pdfobject/pdfhexadecimalstring.txt");
			PdfLexer lexer = new PdfLexer(text);
			PdfParser parser = new PdfParser(lexer);
			{
				//<AZ09>
				PdfHexadecimalString t1 = (PdfHexadecimalString)PdfHexadecimalString.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t1.getChild(2).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"<", new String(value1));
				assertEquals(""+new String(value2),"AZ09", new String(value2));
				assertEquals(""+new String(value3),">", new String(value3));
			}
			{
				//<AZ09><012ABC>
				//<789XYZ>
				PdfHexadecimalString t1 = (PdfHexadecimalString)PdfHexadecimalString.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t1.getChild(2).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"<", new String(value1));
				assertEquals(""+new String(value2),"012ABC", new String(value2));
				assertEquals(""+new String(value3),">", new String(value3));
			}
			{
				//<789XYZ>
				PdfHexadecimalString t1 = (PdfHexadecimalString)PdfHexadecimalString.builder.createToken(parser);
				byte[] value1 = t1.getChild(0).getBuffer(text.getVirtualMemory());
				byte[] value2 = t1.getChild(1).getBuffer(text.getVirtualMemory());
				byte[] value3 = t1.getChild(2).getBuffer(text.getVirtualMemory());
				assertEquals(""+new String(value1),"<", new String(value1));
				assertEquals(""+new String(value2),"789XYZ", new String(value2));
				assertEquals(""+new String(value3),">", new String(value3));
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
