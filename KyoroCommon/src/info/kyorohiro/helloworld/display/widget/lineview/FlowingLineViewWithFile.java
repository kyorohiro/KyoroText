package info.kyorohiro.helloworld.display.widget.lineview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.BigLineData;


public class FlowingLineViewWithFile extends SimpleDisplayObjectContainer {
	private FlowingLineData mInputtedText = null;
	private FlowingLineView mViewer = null;
	private int mTextSize = 14;
	private BigLineData mLineData = null;
	private Thread mTaskManager = null;
	private int mPosition = 0;

	public FlowingLineViewWithFile() {
		mInputtedText = new FlowingLineData(3000, 1000, mTextSize);
		mViewer = new FlowingLineView(mInputtedText);
		addChild(new Layout());
		addChild(mViewer);
	}

	public void start(File path) throws FileNotFoundException {
		mInputtedText.clear();
		mLineData = new BigLineData(path);	
		mTaskManager = new Thread(new NextTask(0));
		mTaskManager.start();
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			FlowingLineViewWithFile.this.mInputtedText.setWidth(graphics.getWidth()*9/10);
			int numOfStackedString = mViewer.getCyclingList().getNumberOfStockedElement();
			int referPoint = numOfStackedString - mPosition;
			mViewer.setPosition(referPoint);
		}
	}
	public FlowingLineData getCyclingStringList() {
		return mInputtedText;
	}


	public FlowingLineData getCyclingFlowingLineData() {
		return mInputtedText;
	}


	public void setPosition(int position) {
		mPosition  = position;
		if(position < 0){
			mPosition = 0;
		}
		
		int numOfStackedString = mViewer.getCyclingList().getNumberOfStockedElement();
		if(mPosition > numOfStackedString) {
			if(mTaskManager == null) {
				mTaskManager = new Thread(new NextTask(0));
				mTaskManager.start();
			}
		}
	}


	public int getPosition() {
		return mPosition;
	}


	public class PrevTask implements Runnable {
		private int mPoint = 0;
		public PrevTask(int point) {
			mPoint = point;
		}

		@Override
		public void run() {
			try {
				mLineData.moveLinePer1000(mPoint/1000);
				CharSequence[] tmp = new CharSequence[1000];
				for(int i=0;i<1000;i++) {
					tmp[i] = mLineData.readLine();
					Thread.sleep(0);
				}

				for(int i=0;i<1000;i++) {
					mInputtedText.addLineToHead(tmp[999-i]);
				}

			} catch(InterruptedException e) {
				
			} catch(IOException e) {
				
			} finally {
				mTaskManager = null;
			}
		}
	}

	public class NextTask implements Runnable {
		private int mPoint = 0;
		public NextTask(int point) {
			mPoint = point;
		}

		@Override
		public void run() {
			try {
				mLineData.moveLinePer1000(mPoint/1000);
				for(int i=0;i<1000;i++) {
					CharSequence line =  mLineData.readLine();
					///mInputtedText.addLinePerBreakText(line);
					mInputtedText.add(new FlowingLineDatam(line,
							Color.parseColor("#FFFFFF"),
							FlowingLineDatam.EXCLUDE_END_OF_LINE));
					if(mLineData.isEOF()) {
						break;
					}
					Thread.sleep(0);
					Thread.yield();
				}
			} catch(InterruptedException e) {
				
			} catch(IOException e) {
				
			} finally {
				mTaskManager = null;
			}
		}
	}
	

}
