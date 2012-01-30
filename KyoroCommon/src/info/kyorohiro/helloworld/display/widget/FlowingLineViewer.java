package info.kyorohiro.helloworld.display.widget;

import java.util.regex.Pattern;
import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class FlowingLineViewer extends SimpleDisplayObjectContainer {

	private CyclingFlowingLineData mInputtedText = null;
	private Viewer mViewer = new Viewer();
	private int mPosition = 0;
	private int mTextSize = 14;

	public FlowingLineViewer(int numOfStringList) {
		addChild(mViewer);
		mInputtedText = new CyclingFlowingLineData(numOfStringList, 1000, mTextSize);
	}

	public CyclingFlowingLineData getCyclingStringList() {
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

			CyclingListInter<FlowingLineData> showingText = null;
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
			
			FlowingLineData[] list = null;
			if (start > end) {
				list =  new FlowingLineData[0];
			} else {
				list = new FlowingLineData[end - start];
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
				if(list[i].getStatus() == FlowingLineData.INCLUDE_END_OF_LINE){
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

		private void updateStatus(SimpleGraphics graphics, CyclingListInter<FlowingLineData> showingText) {
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

			graphics.setTextSize(FlowingLineViewer.this.mTextSize);
			FlowingLineViewer.this.mInputtedText.setWidth(mWidth - margine);
		}
	}

}
