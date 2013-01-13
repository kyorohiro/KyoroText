package info.kyorohiro.helloworld.display.widget.editview.differ;

import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;
import junit.framework.TestCase;

public class TestForDiffer extends TestCase {

	public void testHello() {
		System.out.println("hello");
	}

	public void testAddLine() {
		Differ differ = new Differ();
		differ.addLine(0, "[1]");
		differ.get(null, 0);
	}

	public static class MyBuffer implements LineViewBufferSpec {
		@Override
		public int getNumOfAdd() {
			return 0;
		}

		@Override
		public void clearNumOfAdd() {
		}

		@Override
		public void isSync(boolean isSync) {
		}

		@Override
		public boolean isSync() {
			return false;
		}

		@Override
		public KyoroString get(int i) {
			return null;
		}

		@Override
		public int getNumberOfStockedElement() {
			return 0;
		}

		@Override
		public int getMaxOfStackedElement() {
			return 0;
		}

		@Override
		public BreakText getBreakText() {
			return null;
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLoading() {
			return false;
		}		
	}
}
