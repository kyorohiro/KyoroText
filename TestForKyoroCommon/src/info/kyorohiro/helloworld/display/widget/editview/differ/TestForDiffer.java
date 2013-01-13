package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.TreeMap;

import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;
import junit.framework.TestCase;

public class TestForDiffer extends TestCase {

	public void testHello() {
		System.out.println("hello");
	}

	public void testAddLine() {
		String[] message = {
				"abcdefg",
				"ABCDEFG",
				"NONONON",
				"0123456",
		};
		{
			Differ differ = new Differ();
			MyBuffer buffer = new MyBuffer(message);
			KyoroString ret = differ.get(buffer, 0);
			assertEquals(message[0], ret.toString());
		}
	}

	public static class MyBuffer implements LineViewBufferSpec {
		private LinkedList<KyoroString> mBuffer = new LinkedList<KyoroString>();

		public MyBuffer(String[] buffer) {
			long size = 0;
			for(int i=0;i<buffer.length;i++){
				String text = buffer[i];
				KyoroString tmp = new KyoroString(text);
				tmp.setLinePosition(i);
				tmp.setBeginPointer(size);
				try {
					size = text.getBytes("utf8").length;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				tmp.setEndPointer(size);
				mBuffer.add(tmp);
			}
		}
		@Override
		public KyoroString get(int location) {
			return mBuffer.get(location);
		}

		@Override
		public int getNumberOfStockedElement() {
			return mBuffer.size();
		}

		@Override
		public int getMaxOfStackedElement() {
			return mBuffer.size();
		}

		//
		// following method is unuse
		//
		
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
