package info.kyorohiro.helloworld.logcat.logcat;

import java.util.regex.Pattern;
import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class LogcatViewer extends SimpleDisplayObjectContainer {

	private LogcatCyclingLineDataList mInputtedText = null;
	private Viewer mViewer = new Viewer();
	private int mPosition = 0;
	private int mTextSize = 14;
	private MyCircleControllerEvent mCircleControllerAction = null;

	public LogcatViewer(int numOfStringList) {
		addChild(mViewer);
		mInputtedText = new LogcatCyclingLineDataList(numOfStringList, 1000, mTextSize);
		mCircleControllerAction = new MyCircleControllerEvent();
	}

	public CircleControllerAction getCircleControllerAction() {
		return mCircleControllerAction;
	}

	public LogcatCyclingLineDataList getCyclingStringList() {
		return mInputtedText;
	}

	public void startFilter(Pattern nextFilter) {
		if(nextFilter == null || mInputtedText == null) {
			return;
		}
		Pattern currentFilter = mInputtedText.getFilterText();

		if(!equalFilter(currentFilter, nextFilter)){
			mInputtedText.stop();
			mInputtedText.setFileterText(nextFilter);
			mInputtedText.start();
		}
	}

	private boolean equalFilter(Pattern currentFilter, Pattern nextFilter){
		if(nextFilter == null || nextFilter == null) {
			return true;
		}

		String currentPattern = "";
		String nextPattern = ""+nextFilter.pattern();
		if(currentFilter != null) {
			currentPattern = ""+currentFilter.pattern();
		}

		if (currentPattern.equals(nextPattern)) {
			return true;
		} else {
			return false;
		}
	}

	private class Viewer extends SimpleDisplayObject {
		private int mWidth = 0;
		private int mHeight = 0;
		private int mNumOfLine = 100;

		@Override
		public void paint(SimpleGraphics graphics) {

			CyclingListInter<LogcatLineData> showingText = null;
			if (mInputtedText.taskIsAlive()) {
				showingText = mInputtedText.getDuplicatingList();
			}
			else {
				showingText = mInputtedText;
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

			int blank = 0;
			boolean uppserSideBlankisViewed = (referPoint)<0;
			if(uppserSideBlankisViewed){
				blank = -1*referPoint;
			}

			for (int i = 0; i < list.length; i++) {
				if(list[i]==null){
					continue;
				}
				graphics.setColor(list[i].getColor());
				int startStopY = graphics.getTextSize()*(blank+i + 1);
				if(list[i].getStatus() == LogcatLineData.INCLUDE_END_OF_LINE){
				graphics.drawLine(10, startStopY, graphics.getWidth()-10, startStopY);
				}
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
			mNumOfLine = mHeight / mTextSize;

			int blankSpace = mNumOfLine / 2;
			int margine = graphics.getTextWidth("[9999]:_[9]");

			if (mPosition < -(mNumOfLine - blankSpace)) {
				mPosition = -(mNumOfLine - blankSpace);
			} else if (mPosition > (showingText.getNumberOfStockedElement() - blankSpace)) {
				mPosition = showingText.getNumberOfStockedElement() - blankSpace;
			}

			graphics.setTextSize(LogcatViewer.this.mTextSize);
			LogcatViewer.this.mInputtedText.setWidth(mWidth - margine);
		}
	}

	private class MyCircleControllerEvent implements SimpleCircleController.CircleControllerAction {
		public void upButton(int action) {
			if (action == CircleControllerAction.ACTION_RELEASED) {
				mPosition++;
			}
		}

		public void downButton(int action) {
			if (action == CircleControllerAction.ACTION_RELEASED) {
				mPosition--;
			}
		}

		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				mPosition += rateDegree*2;
			}
		}
	}

}
