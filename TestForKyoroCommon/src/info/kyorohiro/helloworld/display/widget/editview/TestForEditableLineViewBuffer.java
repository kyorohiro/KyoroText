package info.kyorohiro.helloworld.display.widget.editview;

import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;
import junit.framework.TestCase;

public class TestForEditableLineViewBuffer extends TestCase {

	public void testHello() {
		System.out.println("hello!!");
	}

	public void testInputText() {
		LineViewBufferSpec spec = new EmptyLineViewBufferSpecImpl(3);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
	}

	public void testDeleteText() {
		assertTrue(true);
	}


	public void testCommitText1() {
		String[] data = new String[0];//{""};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 0);
		int[] inputCursor = {
			1,1,1,1,1,1,1,
		};
		String[] inputText = {
			"a","b","c","d","e","f","g"
		};
		String[][] expected = {
				{ "a"},{"ab"},{"abc"},{"abcd"},{"abcde"},
				{"abcde","f"},{"abcde","fg"}
		};

		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.pushCommit(inputText[i], inputCursor[i]);
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testCommitText2() {
		String[] data = new String[0];//{""};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 0);
		int[] inputCursor = {
			1,1,1,1,1
		};
		String[] inputText = {
			"abcdefg","hijklmn","o","p","qrstuvwxuz"
		};
		String[][] expected = {
				{"abcde","fg"},
				{"abcde","fghij","klmn"},
				{"abcde","fghij","klmno"},
				{"abcde","fghij","klmno","p"},
				{"abcde","fghij","klmno","pqrst","uvwxu","z"}
		};

		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.pushCommit(inputText[i], inputCursor[i]);
			checkData("ms=" + i + ",", exp, buffer);
		}
	}


	public void testCommitText3() {
		String[] data = {"abcde", "fgh\r\n", "ijkl"};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 0);

		int[] inputCursor = {
			1,1,1,1,1
		};

		String[] inputText = {
			"abcdefg","hijklmn","o","p","qrstuvwxuz"
		};

		String[][] expected = {
				{"abcde","fgabc","defgh","\r\n", "ijkl"},
				{"abcde","fghij","klmna","bcdef","gh\r\n", "ijkl"},
				{"abcde","fghij","klmno","abcde", "fgh\r\n", "ijkl"},
				{"abcde","fghij","klmno","pabcd","efgh\r","\n", "ijkl"}, // irrigal case
				{"abcde","fghij","klmno","pqrst","uvwxu","zabcd","efgh\r","\n", "ijkl"}, //irregalcase
		};

		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.pushCommit(inputText[i], inputCursor[i]);
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testCommitText4() {
		String[] data = {"abcde", "fgh\r\n", "ijkl"};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(2, 2);

		int[] inputCursor = {
			1,1,1,1,1
		};

		String[] inputText = {
			"abcdefg","hijklmn","o","p","qrstuvwxuz"
		};

		String[][] expected = {
				{"abcde", "fgh\r\n", "ijabc","defgk","l"},
				{"abcde", "fgh\r\n", "ijabc","defgh","ijklm","nkl"},
				{"abcde", "fgh\r\n", "ijabc","defgh","ijklm","nokl"},
				{"abcde", "fgh\r\n", "ijabc","defgh","ijklm","nopkl"},
				{"abcde", "fgh\r\n", "ijabc","defgh","ijklm","nopqr","stuvw","xuzkl"},
		};

		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.pushCommit(inputText[i], inputCursor[i]);
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testDeleteChar1() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(2, 2);
		String[][] expected = {
				{ "abcde", "fgh\r\n", "ikl" },// 0
				{ "abcde", "fgh\r\n", "kl" },
				{ "abcde", "fghkl" },//2
				{ "abcde", "fgkl" },
				{ "abcde", "fkl" },// 4
				{ "abcde", "kl" },
				{ "abcdk", "l" },// 6
				{ "abckl" }, { "abkl" }, { "akl" }, { "kl" }, //10
				{ "kl" },{ "kl" }, { "kl" }, };
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.deleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testDeleteChar2() {
		String[] data = { "abcde", "fgh\r\n", "" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 2);
		String[][] expected = { 
				{ "abcde", "fgh" },// 0
				{ "abcde", "fg" },// 1
				{ "abcde", "f" },// 2
				{ "abcde"},// 3
				{ "abcd"},// 4
				{ "abc"},// 0
				{ "ab"},// 0
				{ "a"},// 0
				{ ""},// 0
				{ ""},// 0
				{ ""},// 0
				{ ""},// 0
		};
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.deleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testDeleteChar3() {
		String[] data = { "abc\r\n", "fgh\r\n", "" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 2);
		String[][] expected = { 
				{ "abc\r\n", "fgh"},// 0
				{ "abc\r\n", "fg"},// 0
				{ "abc\r\n", "f"},// 0
				{ "abc\r\n", ""},// 0
				{ "abc"},// 0
				{ "ab"},// 0
				{ "a"},// 0
				{ ""},// 0
		};
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.deleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testDeleteChar1_opt() {
		String[] data = { "abcde", "fgh\r\n", "kl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 2);
		String[][] expected = { { "abcde", "fghkl" }, };
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.deleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testYank1_() {		
		{
			String[] data = { "abcde", "fgh\r\n", "ijkl" };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(0, 0);

			buffer.killLine();
			{
				String[] exp = { "fgh\r\n", "ijkl" };
				checkData("ms1-a", exp, buffer);
			}
			buffer.yank();
			checkData("mr1", data, buffer);

			buffer.setCursor(0, 0);
			buffer.killLine();
			buffer.killLine();
			{
				String[] exp = { "\r\n", "ijkl" };
				checkData("ms1-a", exp, buffer);
			}
			buffer.yank();
			checkData("mr2", data, buffer);

			buffer.setCursor(0, 0);
			buffer.killLine();
			buffer.killLine();
			buffer.killLine();
			{
				String[] exp = { "ijkl" };
				checkData("ms1-a", exp, buffer);
			}
			buffer.yank();
			checkData("mr3", data, buffer);

			buffer.setCursor(0, 0);
			buffer.killLine();
			buffer.killLine();
			buffer.killLine();
			buffer.killLine();
			{
				String[] exp = { "" };
				checkData("ms1-a", exp, buffer);
			}
			buffer.yank();
			checkData("mr4", data, buffer);
		}
		{
			String[] data = { "abcde", "fgh\r\n", "ijkl" };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(
					5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 0);

			buffer.killLine();
			buffer.yank();
			checkData("ms1", data, buffer);

			buffer.setCursor(2, 0);
			buffer.killLine();
			buffer.killLine();
			buffer.yank();
			checkData("ms2", data, buffer);

			buffer.setCursor(2, 0);
			buffer.killLine();
			buffer.killLine();
			buffer.killLine();
			buffer.yank();
			checkData("ms3", data, buffer);

			buffer.setCursor(2, 0);
			buffer.killLine();
			buffer.killLine();
			buffer.killLine();
			buffer.killLine();
			buffer.yank();
			checkData("ms4", data, buffer);
		}
		{
			String[] data = { "abcde", "fgh\r\n", "ijkl" };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 1);

			buffer.killLine();// "abcde", "fg\r\n", "ijkl" | h
			assertEquals(1, buffer.getCol());
			assertEquals(2, buffer.getRow());
			buffer.yank();
			checkData("mt1", data, buffer);
			assertEquals(1, buffer.getCol());
			assertEquals(3, buffer.getRow());

			buffer.setCursor(2, 1);
			buffer.killLine();
			buffer.yank_debug();
			buffer.killLine();// "abcde", "fgijk, "l"  | h \r\n
			buffer.yank_debug();
			buffer.yank();
			checkData("mt2", data, buffer);
			assertEquals(2, buffer.getCol());
			assertEquals(0, buffer.getRow());

			
			buffer.setCursor(2, 1);
			buffer.killLine();
			buffer.yank_debug();
			buffer.killLine();
			buffer.yank_debug();
			buffer.killLine();// "abcde", "fgl" |  h \r\n ijk
			buffer.yank_debug();
			buffer.get_debug();
			buffer.yank();
			checkData("mt3", data, buffer);
			assertEquals(2, buffer.getCol());
			assertEquals(3, buffer.getRow());

			buffer.setCursor(2, 1);
			buffer.killLine();
			buffer.killLine();
			buffer.killLine();// "abcde", "fgl" |  h \r\n ijk
			assertEquals(1, buffer.getCol());
			assertEquals(2, buffer.getRow());
			{
				String[] da = {"abcde", "fgl"};
				checkData("mt3-1", da, buffer);
			}

			buffer.killLine();// "abcde", "fg" |  h \r\n ijk l
			assertEquals(1, buffer.getCol());
			assertEquals(2, buffer.getRow());
			{
				String[] yankExp = {"h", "\r\n", "ijk", "l"};
				for(int i=0;i<buffer.debugGetYankSize();i++){
					assertEquals(yankExp[i], buffer.debugGetYank(i));
				}
			}
			{
				String[] da = {"abcde", "fg"};
				checkData("mt3-1", da, buffer);
			}
			buffer.yank();
			checkData("mt4", data, buffer);
			assertEquals(2, buffer.getCol());
			assertEquals(4, buffer.getRow());
		}
	}

	public void testKillLine_opt() {
		{
			String[] data = { "abcde", "fgl" };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 1);

			buffer.killLine();
			{
				String[] da = {"abcde", "fg"};
				checkData("mt3-1", da, buffer);
			}

		}	
	}

	public void testCommit_opt() {
		{
			String[] data = { "abcde", "fgl" };
			String[][] expected = { { "abcde", "fghl" },
					{ "abcde", "fgh\r\n", "l" },
					{ "abcde", "fgh\r\n", "ijkl" }};
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 1);

			buffer.pushCommit("h", 1);
			checkData("mt3", expected[0], buffer);

			buffer.crlf();
			checkData("mt3", expected[1], buffer);

			buffer.pushCommit("ijk", 1);
			checkData("mt3", expected[2], buffer);
			assertEquals(2, buffer.getCol());
			assertEquals(3, buffer.getRow());
		}
		{
			String[] data = { "abcde", "fg" };
			String[][] expected = { { "abcde", "fgh" },
					{ "abcde", "fgh\r\n", ""},
					{ "abcde", "fgh\r\n", "ijk" },
					{ "abcde", "fgh\r\n", "ijkl" },
					};
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 1);

			buffer.pushCommit("h", 1);
			checkData("mt3", expected[0], buffer);

			buffer.crlf();
			checkData("mt3", expected[1], buffer);

			buffer.pushCommit("ijk", 1);
			checkData("mt3", expected[2], buffer);
			assertEquals(2, buffer.getCol());
			assertEquals(3, buffer.getRow());
			buffer.pushCommit("l", 1);
			checkData("mt3", expected[3], buffer);
			assertEquals(2, buffer.getCol());
			assertEquals(4, buffer.getRow());

		}

	}


	public void testKillLine1_() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };

		String[][] expected = { { "fgh\r\n", "ijkl" }, { "\r\n", "ijkl" },
				{ "ijkl" }, { "" }, { "" }, { "" }, };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 0);

		for (int i = 0; i < data.length; i++) {
			String[] exp = expected[i];
			buffer.killLine();
			checkData("ms=" + i + ",", exp, buffer);
			assertEquals(0, buffer.getCol());
			assertEquals(0, buffer.getRow());
		}
	}

	public void testKillLine2_() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		{
			String[][] expected = { { "abfgh", "\r\n", "ijkl" },
					{ "ab\r\n", "ijkl" }, { "abijk", "l" }, { "abl" },
					{ "ab" }, { "ab" }, };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(
					5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 0);

			for (int i = 0; i < data.length; i++) {
				String[] exp = expected[i];
				buffer.killLine();
				checkData(exp, buffer);
				assertEquals(0, buffer.getCol());
				assertEquals(2, buffer.getRow());
			}
		}
	}

	public void testKillLine3_() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		{
			String[][] expected = { { "abcde", "fg\r\n", "ijkl" },
					{ "abcde", "fgijk", "l" }, { "abcde", "fgl" },
					{ "abcde", "fg" }, };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(
					5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 1);

			for (int i = 0; i < data.length; i++) {
				String[] exp = expected[i];
				buffer.killLine();
				checkData(exp, buffer);
				assertEquals(1, buffer.getCol());
				assertEquals(2, buffer.getRow());
			}
		}
	}

	public void testKillLine4_() {
		String[] data = { "nn\r\n", "abcde", "fgh\r\n", "ijkl" };
		{
			String[][] expected = { { "nn\r\n", "abfgh", "\r\n", "ijkl" },
					{ "nn\r\n", "ab\r\n", "ijkl" }, { "nn\r\n", "abijk", "l" },
					{ "nn\r\n", "abl" }, { "nn\r\n", "ab" },
					{ "nn\r\n", "ab" }, };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(
					5);
			setData(data, spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 1);

			for (int i = 0; i < data.length; i++) {
				String[] exp = expected[i];
				buffer.killLine();
				checkData(exp, buffer);
				assertEquals(1, buffer.getCol());
				assertEquals(2, buffer.getRow());
			}
		}
	}

	public void testKillLine5_() {
		String[] data = { "nn\r\n", "abcde", "fgh\r\n", "ijkl" };

		String[][] expected = { { "nn\r\n", "fgh\r\n", "ijkl" },
				{ "nn\r\n", "\r\n", "ijkl" }, { "nn\r\n", "ijkl" },
				{ "nn\r\n", "" }, { "nn\r\n", "" }, { "nn\r\n", "" }, };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 1);

		for (int i = 0; i < data.length; i++) {
			String[] exp = expected[i];
			buffer.killLine();
			checkData("ms=" + i + ",", exp, buffer);
			assertEquals(1, buffer.getCol());
			assertEquals(0, buffer.getRow());
		}
	}

	public void testBackwardDeleteChar1() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 0);
		String[][] expected = { { "bcdef", "gh\r\n", "ijkl" },
				{ "cdefg", "h\r\n", "ijkl" }, { "defgh", "\r\n", "ijkl" },
				{ "efgh\r", "\n", "ijkl" }, { "fgh\r\n", "ijkl" },
				{ "gh\r\n", "ijkl" }, { "h\r\n", "ijkl" }, { "\r\n", "ijkl" },
				{ "ijkl" }, { "jkl" }, { "kl" }, { "l" }, { "" }, { "" }, };
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testBackwardDeleteChar2() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(2, 0);
		String[][] expected = { { "abdef", "gh\r\n", "ijkl" },
				{ "abefg", "h\r\n", "ijkl" }, { "abfgh", "\r\n", "ijkl" },
				{ "abgh\r", "\n", "ijkl" }, { "abh\r\n", "ijkl" },
				{ "ab\r\n", "ijkl" }, { "abijk", "l" }, { "abjkl" },
				{ "abkl" }, { "abl" }, { "ab" }, { "ab" }, };
		for (int i = 0; i < expected.length; i++) {
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testBackwardDeleteChar3() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(5, 0);
		String[][] expected = {
				{ "abcde", "gh\r\n", "ijkl" },// 0
				{ "abcde", "h\r\n", "ijkl" }, { "abcde", "\r\n", "ijkl" },
				{ "abcde", "ijkl" }, { "abcde", "jkl" },// 4
				{ "abcde", "kl" }, { "abcde", "l" }, { "abcde" }, { "abcde" }, };
		for (int i = 0; i < expected.length; i++) {
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
			assertEquals(0, buffer.getCol());
			assertEquals(5, buffer.getRow());
		}
	}

	public void testBackwardDeleteChar4() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(2, 1);
		String[][] expected = { { "abcde", "fg\r\n", "ijkl" },// 0
				{ "abcde", "fgijk", "l" },// 0
				{ "abcde", "fgjkl" },// 0
				{ "abcde", "fgkl" },// 0
				{ "abcde", "fgl" },// 0
				{ "abcde", "fg" },// 0
		};
		for (int i = 0; i < expected.length; i++) {
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
			assertEquals(1, buffer.getCol());
			assertEquals(2, buffer.getRow());
		}
	}

	public void testBackwardDeleteChar6() {
		String[] data = { "nnnnn", "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(5, 1);
		String[][] expected = {
				{ "nnnnn", "abcde", "gh\r\n", "ijkl" },// 0
				{ "nnnnn", "abcde", "h\r\n", "ijkl" },
				{ "nnnnn", "abcde", "\r\n", "ijkl" },
				{ "nnnnn", "abcde", "ijkl" },
				{ "nnnnn", "abcde", "jkl" },// 4
				{ "nnnnn", "abcde", "kl" }, { "nnnnn", "abcde", "l" },
				{ "nnnnn", "abcde" }, { "nnnnn", "abcde" }, };
		for (int i = 0; i < expected.length; i++) {
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
			assertEquals(1, buffer.getCol());
			assertEquals(5, buffer.getRow());
		}
	}

	public void testBackwardDeleteChar7() {
		String[] data = { "nn\r\n", "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(5, 1);
		String[][] expected = {
				{ "nn\r\n", "abcde", "gh\r\n", "ijkl" },// 0
				{ "nn\r\n", "abcde", "h\r\n", "ijkl" },
				{ "nn\r\n", "abcde", "\r\n", "ijkl" },
				{ "nn\r\n", "abcde", "ijkl" },
				{ "nn\r\n", "abcde", "jkl" },// 4
				{ "nn\r\n", "abcde", "kl" }, { "nn\r\n", "abcde", "l" },
				{ "nn\r\n", "abcde" }, { "nn\r\n", "abcde" }, };
		for (int i = 0; i < expected.length; i++) {
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
			assertEquals(1, buffer.getCol());
			assertEquals(5, buffer.getRow());
		}
	}

	public void testBackwardDeleteChar8() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(1, 2);
		String[][] expected = {
				{ "abcde", "fgh\r\n", "ikl" },// 0
				{ "abcde", "fgh\r\n", "il" },// 0
				{ "abcde", "fgh\r\n", "i" },// 0
				{ "abcde", "fgh\r\n", "i" },// 0
				{ "abcde", "fgh\r\n", "i" },// 0

				};
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testBackwardDeleteChar9() {
		String[] data = { "abcde", "fghjk", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(1, 2);
		String[][] expected = {
				{ "abcde", "fghjk", "ikl" },// 0
				{ "abcde", "fghjk", "il" },// 0
				{ "abcde", "fghjk", "i" },// 0
				{ "abcde", "fghjk", "i" },// 0
				{ "abcde", "fghjk", "i" },// 0

				};
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testBackwardDeleteChar10() {
		String[] data = { "abc\r\n", "fgh\r\n", "" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(3, 1);
		String[][] expected = { 
				{ "abc\r\n", "fgh"},// 0
				{ "abc\r\n", "fgh"},// 0
		};
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}

	public void testBackwardDeleteChar11() {
		String[] data = { "abc\r\n", "fgh\r\n", "" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(1, 1);
		String[][] expected = {
				{ "abc\r\n", "fh\r\n", "" },// 0
				{ "abc\r\n", "f\r\n", "" },// 0
				{ "abc\r\n", "f"},// 0
		};
		for (int i = 0; i < expected.length; i++) {
			android.util.Log.v("test", "--" + i + "--");
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
		}
	}


	public void testBackwardDeleteChar3_opt() {
		String[] data = { "abcde", "jkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(5, 0);
		String[][] expected = { { "abcde", "kl" }, { "abcde", "l" },
				{ "abcde" }, { "abcde" }, };
		for (int i = 0; i < expected.length; i++) {
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms=" + i + ",", exp, buffer);
			assertEquals(0, buffer.getCol());
			assertEquals(5, buffer.getRow());
		}
	}

	private void setData(String[] data, EmptyLineViewBufferSpecImpl buffer) {
		for (int i = 0; i < data.length; i++) {
			buffer.append(new KyoroString(data[i]));
		}
		// check this fucntion.
		checkData(data, buffer);
	}

	private void checkData(String[] data, LineViewBufferSpec buffer) {
		checkData("", data, buffer);
	}

	private void checkData(String message, String[] data,
			LineViewBufferSpec buffer) {
		int i = 0;
		try {
			assertEquals("" + message, data.length,
					buffer.getNumberOfStockedElement());
			for (i = 0; i < data.length; i++) {
				assertEquals("" + message + "[" + i + "]", data[i],
						buffer.get(i).toString());
			}
		} finally {
			if (i != data.length) {
				android.util.Log.v("test", "#message=" + message);
				android.util.Log.v("test",
						"#length=" + buffer.getNumberOfStockedElement());
				for (int j = 0; j < buffer.getNumberOfStockedElement(); j++) {
					KyoroString str = buffer.get(j);
					android.util.Log.v("test", "#buf[" + j + "]" + str.length()
							+ ":" + str);
				}
				for (int j = 0; j < data.length; j++) {
					String str = data[j];
					android.util.Log.v("test",
							"#exp[" + j + "]" + str.getBytes().length + ":"
									+ str);
				}
			}
		}
	}

}
