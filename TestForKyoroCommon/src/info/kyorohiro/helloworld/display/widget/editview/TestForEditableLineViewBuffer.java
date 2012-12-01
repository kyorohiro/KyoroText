package info.kyorohiro.helloworld.display.widget.editview;

import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
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
		
	}
}
