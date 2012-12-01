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
		LineViewBufferSpec spec = new EmptyLineViewBufferSpecImpl();
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
	}

	public void testDeleteText() {
		assertTrue(true);
	}
	
	public void testKillLine() {
		String[] data = {
				"abc",
				"abc\r\n",
				"abc"
		};

		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl();
		setData(data,spec);
		EditableLineViewBuffer buffer = new EditableLineViewBuffer(spec);
	}
	
	private void setData(String[] data, EmptyLineViewBufferSpecImpl buffer) {
		for(int i=0;i<data.length;i++) {
			buffer.append(new KyoroString(data[i]));
		}
		for(int i=0;i<data.length;i++) {
			assertEquals(data[i], buffer.get(i).toString());
		}
	}
}
