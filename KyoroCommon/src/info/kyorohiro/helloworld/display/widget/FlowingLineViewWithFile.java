package info.kyorohiro.helloworld.display.widget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.BigLineData;


public class FlowingLineViewWithFile extends SimpleDisplayObjectContainer {
	private FlowingLineData mInputtedText = null;
	private FlowingLineView mViewer = null;
	private int mTextSize = 14;
	private BigLineData mLineData = null;

	public FlowingLineViewWithFile(File path) throws FileNotFoundException {
		mLineData = new BigLineData(path);
		mInputtedText = new FlowingLineData(3000, 1000, mTextSize);
		mViewer = new FlowingLineView(mInputtedText);
		addChild(mViewer);
		addChild(new Layout());
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			FlowingLineViewWithFile.this.mInputtedText.setWidth(graphics.getWidth()*9/10);
		}
	}
	public FlowingLineData getCyclingStringList() {
		return mInputtedText;
	}

	public FlowingLineData getCyclingFlowingLineData() {
		return mInputtedText;
	}

	public void setPosition(int position) {
		mViewer.setPosition(position);
	}

	public int getPosition() {
		return mViewer.getPosition();
	}


	public class NextTask implements Runnable {
		
		private int mPoint = 0;
		public NextTask(int point) {
			mPoint = point;
		}

		@Override
		public void run() {
			try {
				String line = mLineData.readLine();
				mInputtedText.addLinePerBreakText(line);
				Thread.sleep(0);
			} catch(InterruptedException e) {
				
			} catch(IOException e) {
				
			}
		}
	}
}
