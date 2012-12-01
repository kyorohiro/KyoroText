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
			buffer.setCursor(0, 0);
			buffer.killLine();
			String[] expected = {
					"fgh\r\n",
					"ijkl"
			};
			checkData(expected, buffer);
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
		for(int i=0;i<data.length;i++) {
			assertEquals(data[i], buffer.get(i).toString());
		}
	}

}
