package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.File;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.sample.EmptySimpleFont;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import junit.framework.TestCase;

public class TestForLineViewManager extends TestCase {

	public void testHello() {

	}

	public void otherWindow() {
		SimpleApplication application = new MyApplication();
		TextViewBuilder builder = new MyBuilder();
		int baseTextSize = 12;
		int textSize = 12;
		int width = 200;
		int height = 200;
		int mergine = 12;
		int menuWidth = 12;
		{
			LineViewManager manager = new LineViewManager(application, builder,
					baseTextSize, textSize, width, height, mergine, menuWidth);
			TextViewer v = manager.getFocusingTextViewer();
			manager.otherWindow();
			assertEquals(v, manager.getFocusingTextViewer());
		}
		{

		}
	}

	public static class MyApplication implements SimpleApplication {
		@Override
		public File getApplicationDirectory() {
			return null;
		}
	}

	public static class MyBuilder extends TextViewBuilder {
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
			return null;
		}

		@Override
		public boolean currentBrIsLF() {
			return false;
		}

		@Override
		public String getCurrentCharset() {
			return null;
		}

		@Override
		public void setCurrentFile(String path) {
		}
	}
}
