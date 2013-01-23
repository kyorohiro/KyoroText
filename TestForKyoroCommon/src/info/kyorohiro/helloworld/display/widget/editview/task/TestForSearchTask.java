package info.kyorohiro.helloworld.display.widget.editview.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.sample.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.ext.textviewer.manager.task.SearchTask;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.AsyncronousTask;
import junit.framework.TestCase;

public class TestForSearchTask extends TestCase {

	public void testHello() {
		
	}

	//
	// following bc search pattern
	// { "abcbb", "cgh\r\n", "ijkla" }
	//
	public void testSecound() {
		
	}


	public void testFirst() {
		{
			String[] data = { "abcbe", "fgh\r\n", "ijkla" };
			EmptyLineViewBufferSpecImpl spec = new EmptyLineViewBufferSpecImpl(5);
			setData(data, spec);
			EditableLineView view = new EditableLineView(spec, 12, 100);

			{
				{
					android.util.Log.v("kiyo","--4--");
					SearchTask task = new SearchTask(view, "b");
					AsyncronousTask atask = new AsyncronousTask(task);
					Thread t = new Thread(atask);
					view.getLeft().setCursorCol(0);
					view.getLeft().setCursorRow(0);
					t.start();
					atask.syncTask();
					assertEquals(2, view.getLeft().getCursorRow());
					assertEquals(0, view.getLeft().getCursorCol());
				}
				{
					android.util.Log.v("kiyo","--5--");
					SearchTask task = new SearchTask(view, "b");
					AsyncronousTask atask = new AsyncronousTask(task);
					Thread t = new Thread(atask);
					t.start();
					atask.syncTask();
					assertEquals(4, view.getLeft().getCursorRow());
					assertEquals(0, view.getLeft().getCursorCol());
				}
				{
					android.util.Log.v("kiyo","--6--");
					SearchTask task = new SearchTask(view, "b");
					AsyncronousTask atask = new AsyncronousTask(task);
					Thread t = new Thread(atask);
					t.start();
					atask.syncTask();
					assertEquals(2, view.getLeft().getCursorRow());
					assertEquals(0, view.getLeft().getCursorCol());
				}
			}
			{
				SearchTask task = new SearchTask(view, "f");
				AsyncronousTask atask = new AsyncronousTask(task);
				Thread t = new Thread(atask);
				view.getLeft().setCursorCol(0);
				view.getLeft().setCursorRow(0);
				t.start();
				atask.syncTask();
				assertEquals(1, view.getLeft().getCursorRow());
				assertEquals(1, view.getLeft().getCursorCol());
			}
			{
				android.util.Log.v("kiyo","--3--");
				SearchTask task = new SearchTask(view, "f");
				AsyncronousTask atask = new AsyncronousTask(task);
				Thread t = new Thread(atask);
				t.start();
				atask.syncTask();
				assertEquals(1, view.getLeft().getCursorRow());
				assertEquals(1, view.getLeft().getCursorCol());
			}
			{
				android.util.Log.v("kiyo","--4--");
				SearchTask task = new SearchTask(view, "a");
				AsyncronousTask atask = new AsyncronousTask(task);
				Thread t = new Thread(atask);
				view.getLeft().setCursorCol(0);
				view.getLeft().setCursorRow(0);
				t.start();
				atask.syncTask();
				assertEquals(1, view.getLeft().getCursorRow());
				assertEquals(0, view.getLeft().getCursorCol());
			}
			{
				android.util.Log.v("kiyo","--5--");
				SearchTask task = new SearchTask(view, "a");
				AsyncronousTask atask = new AsyncronousTask(task);
				Thread t = new Thread(atask);
				t.start();
				atask.syncTask();
				assertEquals(5, view.getLeft().getCursorRow());
				assertEquals(2, view.getLeft().getCursorCol());
			}
			{
				android.util.Log.v("kiyo","--6--");
				SearchTask task = new SearchTask(view, "a");
				AsyncronousTask atask = new AsyncronousTask(task);
				Thread t = new Thread(atask);
				t.start();
				atask.syncTask();
				assertEquals(1, view.getLeft().getCursorRow());
				assertEquals(0, view.getLeft().getCursorCol());
			}
			{
				android.util.Log.v("kiyo","--7--");
				SearchTask task = new SearchTask(view, "efg");
				AsyncronousTask atask = new AsyncronousTask(task);
				Thread t = new Thread(atask);
				t.start();
				atask.syncTask();
				assertEquals(2, view.getLeft().getCursorRow());
				assertEquals(1, view.getLeft().getCursorCol());
			}

			{
				android.util.Log.v("kiyo","--8--");
				SearchTask task = new SearchTask(view, "aaaaa");
				AsyncronousTask atask = new AsyncronousTask(task);
				Thread t = new Thread(atask);
				t.start();
				atask.syncTask();
				assertEquals(2, view.getLeft().getCursorRow());
				assertEquals(1, view.getLeft().getCursorCol());
			}

		}
	}

	private void setData(String[] data, EmptyLineViewBufferSpecImpl buffer) {
		for (int i = 0; i < data.length; i++) {
			buffer.append(new KyoroString(data[i]));
		}
	}
}
