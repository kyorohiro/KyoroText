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
	
	public void testKillLine() {
		String[] data = {
				"abcde",
				"fgh\r\n",
				"ijkl"
		};
		{
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data,spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(0, 0);
			buffer.killLine();
			String[] expected1 = {
					"fgh\r\n",
					"ijkl"
			};
			checkData(expected1, buffer);
			buffer.killLine();
			String[] expected2 = {
					"\r\n",
					"ijkl"
			};
			checkData(expected2, buffer);
			assertEquals(0, buffer.getCol());

			buffer.killLine();
			String[] expected3 = {
					"ijkl"
			};
			checkData(expected3, buffer);
			assertEquals(0, buffer.getCol());

			buffer.killLine();
			String[] expected4 = {
					""
			};
			assertEquals(1, buffer.getNumberOfStockedElement());
			assertEquals(0, buffer.getCol());
			checkData(expected4, buffer);

			buffer.killLine();
			assertEquals(0, buffer.getNumberOfStockedElement());
			assertEquals(0, buffer.getCol());

			buffer.killLine();
			assertEquals(0, buffer.getNumberOfStockedElement());
			assertEquals(0, buffer.getCol());
		}
	}
	public void testKillLine2() {
		String[] data = {
				"abcde",
				"fgh\r\n",
				"ijkl"
		};
		{
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data,spec);
			EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
			buffer.IsCrlfMode(true);
			buffer.setCursor(2, 0);
			buffer.killLine();
			String[] expected1 = {
					"abfgh",
					"\r\n",
					"ijkl"
			};
			checkData(expected1, buffer);

			buffer.killLine();
			String[] expected2 = {
					"ab\r\n",
					"ijkl"
			};
			checkData(expected2, buffer);

			buffer.killLine();
			String[] expected3 = {
					"abijk",
					"l"
			};
			checkData(expected3, buffer);

			buffer.killLine();
			String[] expected4 = {
					"abl"
			};
			checkData(expected4, buffer);


			buffer.killLine();
			String[] expected5 = {
					"ab"
			};
			checkData(expected5, buffer);

			buffer.killLine();
			String[] expected6 = {
					"ab"
			};
			checkData(expected6, buffer);

		}
	}


	public void testBackwardDeleteChar1() {
		String[] data = {
				"abcde",
				"fgh\r\n",
				"ijkl"
		};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data,spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(0, 0);
		String[][] expected = {
				{"bcdef", "gh\r\n", "ijkl"},
				{"cdefg", "h\r\n", "ijkl"},
				{"defgh", "\r\n", "ijkl"},
				{"efgh\r", "\n", "ijkl"},
				{"fgh\r\n", "ijkl"},
				{"gh\r\n", "ijkl"},
				{"h\r\n", "ijkl"},
				{"\r\n", "ijkl"},
				{"ijkl"},
				{"jkl"},
				{"kl"},
				{"l"},
				{""},
				{""},
		};
		for(int i=0;i<expected.length;i++){
			android.util.Log.v("test","--"+i+"--");
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms="+i+",",exp, buffer);
		}
	}
	public void testBackwardDeleteChar2() {
		String[] data = {
				"abcde",
				"fgh\r\n",
				"ijkl"
		};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data,spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(2, 0);
		String[][] expected = {
				{"abdef", "gh\r\n", "ijkl"},
				{"abefg", "h\r\n", "ijkl"},
				{"abfgh", "\r\n", "ijkl"},
				{"abgh\r", "\n", "ijkl"},
				{"abh\r\n", "ijkl"},
				{"ab\r\n", "ijkl"},
				{"abijk","l"},
				{"abjkl"},
				{"abkl"},
				{"abl"},
				{"ab"},
				{"ab"},
		};
		for(int i=0;i<expected.length;i++){
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms="+i+",",exp, buffer);
		}
	}

	public void testBackwardDeleteChar3() {
		String[] data = {
				"abcde",
				"fgh\r\n",
				"ijkl"
		};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data,spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(5, 0);
		String[][] expected = {
				{"abcde", "gh\r\n", "ijkl"},//0
				{"abcde", "h\r\n", "ijkl"},
				{"abcde", "\r\n", "ijkl"},
				{"abcde", "ijkl"},
				{"abcde", "jkl"},//4
				{"abcde", "kl"},
				{"abcde", "l"},
				{"abcde"},
				{"abcde"},
		};
		for(int i=0;i<expected.length;i++){
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms="+i+",",exp, buffer);
			assertEquals(0, buffer.getCol());
			assertEquals(5, buffer.getRow());
		}
	}
	public void testBackwardDeleteChar3_opt() {
		String[] data = {
			"abcde", "jkl"
		};
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data,spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
		buffer.IsCrlfMode(true);
		buffer.setCursor(5, 0);
		String[][] expected = {
				{"abcde", "kl"},
				{"abcde", "l"},
				{"abcde"},
				{"abcde"},
		};
		for(int i=0;i<expected.length;i++){
			String[] exp = expected[i];
			buffer.backwardDeleteChar();
			checkData("ms="+i+",",exp, buffer);
			assertEquals(0, buffer.getCol());
			assertEquals(5, buffer.getRow());
		}
	}

	private void setData(String[] data, EmptyLineViewBufferSpecImpl buffer) {
		for(int i=0;i<data.length;i++) {
			buffer.append(new KyoroString(data[i]));
		}
		// check this fucntion.
		checkData(data, buffer);
	}

	private void checkData(String[] data, LineViewBufferSpec buffer) {
		checkData("", data, buffer);
	}

	private void checkData(String message, String[] data, LineViewBufferSpec buffer) {
		int i=0;
		try {
		assertEquals(data.length, buffer.getNumberOfStockedElement());
		for(i=0;i<data.length;i++) {
			assertEquals(""+message+"["+i+"]",data[i], buffer.get(i).toString());
		}
		} finally {
			if(i!=data.length) {
				android.util.Log.v("test","#message="+message);
				android.util.Log.v("test","#length="+buffer.getNumberOfStockedElement());
				for(int j=0;j<buffer.getNumberOfStockedElement();j++) {
					KyoroString str = buffer.get(j);
					android.util.Log.v("test","#buf["+j+"]"+str.length()+":"+str);
				}
				for(int j=0;j<data.length;j++) {
					String str = data[j];
					android.util.Log.v("test","#exp["+j+"]"+str.getBytes().length+":"+str);
				}
			}
		}
	}

}
