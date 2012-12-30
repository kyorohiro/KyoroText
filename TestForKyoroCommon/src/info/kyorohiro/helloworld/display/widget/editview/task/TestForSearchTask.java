package info.kyorohiro.helloworld.display.widget.editview.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.AsyncronousTask;
import junit.framework.TestCase;

public class TestForSearchTask extends TestCase {

	public void testHello() {
		
	}

	public void testFirst() {
		{
			String[] data = { "abcde", "fgh\r\n", "ijkla" };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data, spec);
			EditableLineView view = new EditableLineView(spec, 12, 100);

			SearchTask task = new SearchTask(view, "f");
			AsyncronousTask atask = new AsyncronousTask(task);
			Thread t = new Thread(atask);
			view.getLeft().setCursorCol(0);
			view.getLeft().setCursorRow(0);
			t.start();

			atask.waitForTask();
			assertEquals(1, view.getLeft().getCursorRow());
			assertEquals(1, view.getLeft().getCursorCol());
		}
	}

	private void setData(String[] data, EmptyLineViewBufferSpecImpl buffer) {
		for (int i = 0; i < data.length; i++) {
			buffer.append(new KyoroString(data[i]));
		}
	}
}
