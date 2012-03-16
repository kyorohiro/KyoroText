package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.CyclingListInter;
import android.graphics.Color;

public class LineView extends SimpleDisplayObject {
	private int mNumOfLine = 100;
	private CyclingListInter<FlowingLineDatam> mInputtedText = null;
	private int mPosition = 0;
	private int mTextSize = 14;
	private int mShowingTextStartPosition = 0;
	private int mShowingTextEndPosition = 0;

	public LineView(CyclingListInter<FlowingLineDatam> inputtedText) {
		mInputtedText = inputtedText;
	}

	public void setCyclingList(CyclingListInter<FlowingLineDatam> inputtedText) {
		mInputtedText = inputtedText;
	}

	public CyclingListInter<FlowingLineDatam> getCyclingList() {
		return mInputtedText;
	}

	public int getShowingTextStartPosition() {
		return mShowingTextStartPosition;
	}

	public int getShowingTextEndPosition() {
		return mShowingTextEndPosition;
	}


	@Override
	public void paint(SimpleGraphics graphics) {
		CyclingListInter<FlowingLineDatam> showingText = mInputtedText;
		updateStatus(graphics, showingText);
		drawBG(graphics);
		int start = start(showingText);
		int end = end(showingText);
		int blank = blank(showingText);

		FlowingLineDatam[] list = null;
		if (start > end) {
			list = new FlowingLineDatam[0];
		} else {
			list = new FlowingLineDatam[end - start];
			list = showingText.getElements(list, start, end);
		}

		showLineDate(graphics, list, blank);
		mShowingTextStartPosition = start;
		mShowingTextEndPosition = end;
	}


	public int start(CyclingListInter<FlowingLineDatam> showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		int start = referPoint;
		if (start < 0) {
			start = 0;
		}
		return start;
	}


	public int end(CyclingListInter<FlowingLineDatam> showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		int end = referPoint + mNumOfLine;
		if (end < 0) {
			end = 0;
		}
		if (end >= numOfStackedString) {
			end = numOfStackedString;
		}
		return end;
	}

	public int blank(CyclingListInter<FlowingLineDatam> showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		int blank = 0;
		boolean uppserSideBlankisViewed = (referPoint) < 0;
		if (uppserSideBlankisViewed) {
			blank = -1 * referPoint;
		}
		return blank;
	}

	private void showLineDate(SimpleGraphics graphics, FlowingLineDatam[] list,
			int blank) {
		for (int i = 0; i < list.length; i++) {
			if (list[i] == null) {
				continue;
			}

			// drawLine
			graphics.setColor(list[i].getColor());
			int startStopY = (int)(graphics.getTextSize()*1.2) * (blank + i + 1);
			if (list[i].getStatus() == FlowingLineDatam.INCLUDE_END_OF_LINE) {
				graphics.drawLine(10, startStopY, graphics.getWidth() - 10,
						startStopY);
			}
			int x = getWidth() / 20;
			int y = (int)(graphics.getTextSize()*1.2) * (blank + i + 1);
			graphics.drawText("" + list[i], x, y);
		}
	}

	private void drawBG(SimpleGraphics graphics) {
		graphics.drawBackGround(Color.parseColor("#FF777788"));
		//graphics.drawBackGround(Color.parseColor("#cc795514"));
		//graphics.setColor(Color.parseColor("#ccc9f486"));
	}

	private void updateStatus(SimpleGraphics graphics,
			CyclingListInter<FlowingLineDatam> showingText) {
		mNumOfLine = (int)(getHeight() / (mTextSize*1.2));
		int blankSpace = mNumOfLine / 2;
		if (mPosition < -(mNumOfLine - blankSpace)) {
			setPosition(-(mNumOfLine - blankSpace) - 1);
		} else if (mPosition > (showingText.getNumberOfStockedElement() - blankSpace)) {
			setPosition(showingText.getNumberOfStockedElement() - blankSpace);
		}
		graphics.setTextSize(mTextSize);
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public int getPosition() {
		return mPosition;
	}
}
