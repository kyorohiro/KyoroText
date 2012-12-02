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
					"abfgh\r\n",
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
					"abijkl"
			};
			checkData(expected3, buffer);
			
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
		int i=0;
		try {
		for(i=0;i<data.length;i++) {
			assertEquals("["+i+"]",data[i], buffer.get(i).toString());
		}
		} finally {
			if(i!=data.length) {
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
