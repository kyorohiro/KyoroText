package info.kyorohiro.helloworld.display.widget.editview.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;
import junit.framework.TestCase;

public class TestForSearchTask extends TestCase {

	public void testHello() {
		
	}

	public void testFirst() {
		String[] data = { "abcde", "fgh\r\n", "ijkl" };
		EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
		setData(data, spec);

		EditableLineView view = new EditableLineView(spec, 12, 100);
		SearchTask task = new SearchTask(view, "abcde");
	}

	private void setData(String[] data, EmptyLineViewBufferSpecImpl buffer) {
		for (int i = 0; i < data.length; i++) {
			buffer.append(new KyoroString(data[i]));
		}
	}
}
