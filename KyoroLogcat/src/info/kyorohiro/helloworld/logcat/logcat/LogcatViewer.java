package info.kyorohiro.helloworld.logcat.logcat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.graphics.Paint;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerEvent;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class LogcatViewer extends SimpleDisplayObjectContainer {

	private LogcatCyclingLineDataList mShowedText = null;
	private Viewer viewer = new Viewer();
	private int mPosition = 0;
	private int mTextSize = 14;
	private MyCircleControllerEvent mCircleControllerEvent = null;

	public LogcatViewer(int numOfStringList) {
		this.addChild(viewer);
		mShowedText = new LogcatCyclingLineDataList(numOfStringList, 1000, mTextSize);
		mCircleControllerEvent = new MyCircleControllerEvent();
	}

	public CircleControllerEvent getCircleControllerEvent() {
		return mCircleControllerEvent;
	}

	public LogcatCyclingLineDataList getCyclingStringList() {
		return mShowedText;
	}

	public void startFilter(String filter) {
		mShowedText.stop();
		mShowedText.setFileterText(filter);
		mShowedText.start();
	}

	private class Viewer extends SimpleDisplayObject {
		private int mWidth = 0;
		private int mHeight = 0;
		private int mNumOfLine = 100;

		@Override
		public void paint(SimpleGraphics graphics) {

			CyclingListInter<LogcatLineData> showingText = null;
			if (mShowedText.taskIsAlive()) {
				showingText = mShowedText.getCopyingList();
			}
			else {
				showingText = mShowedText;
			}

			updateStatus(graphics, showingText);
			drawBG(graphics);

			int numOfStackedString = showingText.getNumberOfStockedElement();
			int referPoint = numOfStackedString-(mPosition+mNumOfLine);
			int start = referPoint;
			int end = start + mNumOfLine;
			if(start < 0) {
				start = 0;	
			}
			if(end < 0){
				end = 0;
			}
			if(end >= numOfStackedString){
				end = numOfStackedString;
			}
			
			LogcatLineData[] list = null;
			if (start > end) {
				list =  new LogcatLineData[0];
			} else {
				list = new LogcatLineData[end - start];
				list = showingText.getElements(list, start, end);
			}

			int blank = 0;//mNumOfLine - list.length;
			boolean uppserSideBlankisViewed = (referPoint)<0;
			if(uppserSideBlankisViewed){
				blank = -1*referPoint;
			}

			for (int i = 0; i < list.length; i++) {
				if(list[i]==null){
					continue;
				}
				graphics.setColor(list[i].getColor());
				graphics.drawText(
						"[" + (start + i) + "]:  " + list[i],
						0,
						graphics.getTextSize()*(blank+i + 1));
			}
		}

		private void drawBG(SimpleGraphics graphics) {
			graphics.drawBackGround(Color.parseColor("#cc795514"));
			graphics.setColor(Color.parseColor("#ccc9f486"));
		}

		private void updateStatus(SimpleGraphics graphics, CyclingListInter<LogcatLineData> showingText) {
			mWidth = (int) graphics.getWidth();
			mHeight = (int) graphics.getHeight();
			graphics.setTextSize(LogcatViewer.this.mTextSize);
			mNumOfLine = mHeight / mTextSize;

			int blankSpace = mNumOfLine / 2;
			if (mPosition < -(mNumOfLine - blankSpace)) {
				mPosition = -(mNumOfLine - blankSpace);
			} else if (mPosition > (showingText.getNumberOfStockedElement() - blankSpace)) {
				mPosition = showingText.getNumberOfStockedElement() - blankSpace;
			}

			int margine = graphics.getTextWidth("[9999]:_[9]");
			LogcatViewer.this.mShowedText.setWidth(mWidth - margine);
		}
	}

	private class MyCircleControllerEvent implements
			SimpleCircleController.CircleControllerEvent {
		public void upButton(int action) {
			if (action == CircleControllerEvent.ACTION_RELEASED) {
				mPosition++;
			}
		}

		public void downButton(int action) {
			if (action == CircleControllerEvent.ACTION_RELEASED) {
				mPosition--;
			}
		}

		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerEvent.ACTION_MOVE) {
				mPosition += rateDegree*2;
			}

		}

	}

}
