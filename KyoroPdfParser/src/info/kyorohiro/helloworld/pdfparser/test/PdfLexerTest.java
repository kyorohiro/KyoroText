package info.kyorohiro.helloworld.pdfparser.test;

import info.kyorohiro.helloworld.pdfparser.Lexer;
import info.kyorohiro.helloworld.pdfparser.PdfLexer;
import info.kyorohiro.helloworld.pdfparser.PdfParser;
import info.kyorohiro.helloworld.pdfparser.Text;
import info.kyorohiro.helloworld.pdfparser.Token;
import junit.framework.TestCase;

public class PdfLexerTest extends TestCase {

	public void testPrimitive(){
		String []scriptList = {
//			" ", PdfLexer.SET_SPACE.mText,
			"0123456789", PdfLexer.SET_NUMBER.mText,
			"<<", PdfLexer.SET_INDEX_BEGIN.mText,
			">>", PdfLexer.SET_INDEX_END.mText,
			"[", PdfLexer.SET_ARRAY_BEGIN.mText,
			"]", PdfLexer.SET_ARRAY_END.mText,
			"(", PdfLexer.SET_ASCII_BEGIN.mText,
			")", PdfLexer.SET_ASCII_END.mText,
			"obj", PdfLexer.SET_OBJECT_BEGIN.mText,
			"endobj", PdfLexer.SET_OBJECT_END.mText,
//			"%comment", PdfLexer.SET_COMMENT.mText,
			"/Name", PdfLexer.SET_NAME.mText,
			"Name", PdfLexer.SET_IDENTIFY.mText,
		};
		for(int i=0;i<scriptList.length;i+=2){
			String script = scriptList[i];
			String expectedID = scriptList[i+1];
			String expectedValue = scriptList[i];
			Text text = new Text(script); 

			PdfLexer lexer = new PdfLexer(text);
			Token current = lexer.nextToken();
			String value = current.getValue().toString();
			String id = current.getIdentify().toString();
			int type = current.getType();
			android.util.Log.v("pdfparser", "ret="+value+","+id+","+type);
			assertEquals(expectedID, id);
			assertEquals(expectedValue, value);
		}
	}

	public void testIndex00(){
		PdfLexer lexer = new PdfLexer(new Text(TestData._TEST_INDEX));
		Token t = null;
		int[] type = {
				PdfLexer.SET_INDEX_BEGIN.mType,	
				//
				PdfLexer.SET_NAME.mType,
				PdfLexer.SET_NAME.mType,
				//
				PdfLexer.SET_NAME.mType,
				PdfLexer.SET_NUMBER.mType,
				PdfLexer.SET_NUMBER.mType,
				PdfLexer.SET_IDENTIFY.mType,
				//
				PdfLexer.SET_NAME.mType,
				PdfLexer.SET_NUMBER.mType,
				PdfLexer.SET_NUMBER.mType,
				PdfLexer.SET_IDENTIFY.mType,
				//
				PdfLexer.SET_NAME.mType,
				PdfLexer.SET_NUMBER.mType,
				PdfLexer.SET_NUMBER.mType,
				PdfLexer.SET_IDENTIFY.mType,
				//
				PdfLexer.SET_INDEX_END.mType,	
				//
				PdfLexer.SET_EOF.mType,
		};
		for(int ty:type) {
			t = lexer.nextToken();
			android.util.Log.v("pdfparser",""+t+"=="+ty);
			if(t.mType != ty) {
				assertTrue(false);
			}
		}
	}
}
