package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.File;

import android.os.Environment;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.sample.EmptySimpleFont;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import junit.framework.TestCase;

public class TestForLineViewManager extends TestCase {

	public void testHello() {

	}

	public void testOtherWindow() {
		SimpleApplication application = new MyApplication();
		AppDependentAction builder = new MyBuilder();
		int baseTextSize = 12;
		int textSize = 12;
		int width = 200;
		int height = 200;
		int mergine = 12;
		int menuWidth = 12;
		{
			//
			// initial state
			//
			BufferManager manager = new BufferManager(application, builder,
					baseTextSize, textSize, width, height, mergine, menuWidth);
			TextViewer v = manager.getFocusingTextViewer();
			manager.otherWindow();
			assertEquals(v, manager.getFocusingTextViewer());
		}
		{
			//
			// modeline and lineviewmanager
			//
			BufferManager manager = new BufferManager(application, builder,
					baseTextSize, textSize, width, height, mergine, menuWidth);
			TextViewer v = manager.getFocusingTextViewer();

			// set task
			manager.getMiniBuffer().startMiniBufferJob(new MyTask());
			manager.otherWindow();
			assertEquals(manager.getMiniBuffer(), manager.getFocusingTextViewer());

			//
			manager.otherWindow();
			assertEquals(v, manager.getFocusingTextViewer());

			//
			manager.getMiniBuffer().endTask();
			manager.otherWindow();
			assertEquals(v, manager.getFocusingTextViewer());
			//
			manager.otherWindow();
			assertEquals(v, manager.getFocusingTextViewer());
		}
		{
			//
			// dividewindow
			//
			BufferManager manager = new BufferManager(application, builder,
					baseTextSize, textSize, width, height, mergine, menuWidth);
			TextViewer v = manager.getFocusingTextViewer();
			manager.otherWindow();
			assertEquals(v, manager.getFocusingTextViewer());
		}

	}

	public class MyTask implements MiniBufferJob {
		@Override
		public void enter(String line) {
		}
		@Override
		public void begin() {
		}
		@Override
		public void end() {
		}
		@Override
		public void tab(String line) {
		}		
	}
	public static class MyApplication implements SimpleApplication {
		@Override
		public File getApplicationDirectory() {
			return Environment.getExternalStorageDirectory();
		}

		@Override
		public void showMessage(CharSequence message) {
			System.out.println(""+message);
		}
	}

	public static class MyBuilder extends AppDependentAction {
		@Override
		public SimpleFont newSimpleFont() {
			return new EmptySimpleFont();
		}

		@Override
		public void copyStart() {
		}

		@Override
		public void pastStart() {
		}

		@Override
		public File getFilesDir() {
			return Environment.getExternalStorageDirectory();
		}

		@Override
		public boolean currentBrIsLF() {
			return false;
		}

		@Override
		public String getCurrentCharset() {
			return "utf8";
		}

		@Override
		public void setCurrentFile(String path) {
		}
	}
}
