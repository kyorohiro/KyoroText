package info.kyorohiro.helloworld.display.widget;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.CyclingListInter;
import android.graphics.Color;

public class LineViewer extends SimpleDisplayObject {
	private int mWidth = 0;
	private int mHeight = 0;
	private int mNumOfLine = 100;
	private CyclingListInter<FlowingLineData> mInputtedText = null;
	private int mPosition = 0;
	private int mTextSize = 14;

	public LineViewer(CyclingListInter<FlowingLineData> inputtedText) {
		mInputtedText = inputtedText;
	}

	public void setCyclingList(CyclingListInter<FlowingLineData> inputtedText) {
		mInputtedText = inputtedText;
	}

	public CyclingListInter<FlowingLineData> setCyclingList() {
		return mInputtedText;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		CyclingListInter<FlowingLineData> showingText = mInputtedText;
		updateStatus(graphics, showingText);
		drawBG(graphics);
		int start = start(showingText);
		int end = end(showingText);
		int blank = blank(showingText);

		FlowingLineData[] list = null;
		if (start > end) {
			list = new FlowingLineData[0];
		} else {
			list = new FlowingLineData[end - start];
			list = showingText.getElements(list, start, end);
		}

		showLineDate(graphics, list, blank);
		showScrollBar(graphics, start, end,
				showingText.getNumberOfStockedElement());
	}

	public int start(CyclingListInter<FlowingLineData> showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		int start = referPoint;
		if (start < 0) {
			start = 0;
		}
		return start;
	}

	public int end(CyclingListInter<FlowingLineData> showingText) {
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

	public int blank(CyclingListInter<FlowingLineData> showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - ( mPosition+ mNumOfLine);
		int blank = 0;
		boolean uppserSideBlankisViewed = (referPoint) < 0;
		if (uppserSideBlankisViewed) {
			blank = -1 * referPoint;
		}
		return blank;
	}

	private void showScrollBar(SimpleGraphics graphics, int start, int end,
			int size) {
		int w = mWidth;
		int h = mHeight;
		int sp = start;
		int ep = end;
		int s = size;
		if (s == 0) {
			s = 1;
		}
		int barWidth = mWidth / 20;
		double barHeigh = mHeight / (double) s;
		graphics.drawLine(w - barWidth, (int) (barHeigh * sp), w,
				(int) (barHeigh * sp));
		graphics.drawLine(w - barWidth, (int) (barHeigh * ep), w,
				(int) (barHeigh * ep));
		graphics.drawLine(w - barWidth, (int) (barHeigh * sp),
				w - barWidth, (int) (barHeigh * ep));
		graphics.drawLine(w, (int) (barHeigh * ep), w,
				(int) (barHeigh * sp));
		graphics.drawLine(w - barWidth, (int) (barHeigh * sp), w,
				(int) (barHeigh * ep));
		graphics.drawLine(w - barWidth, (int) (barHeigh * ep), w,
				(int) (barHeigh * sp));
	}

	private void showLineDate(SimpleGraphics graphics,
			FlowingLineData[] list, int blank) {
		for (int i = 0; i < list.length; i++) {
			if (list[i] == null) {
				continue;
			}
			graphics.setColor(list[i].getColor());
			int startStopY = graphics.getTextSize() * (blank + i + 1);
			if (list[i].getStatus() == FlowingLineData.INCLUDE_END_OF_LINE) {
				graphics.drawLine(10, startStopY, graphics.getWidth() - 10,
						startStopY);
			}
			graphics.drawText("" + list[i], mWidth / 20,
					graphics.getTextSize() * (blank + i + 1));
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
		int margine = mWidth / 10;

		if (mPosition < -(mNumOfLine - blankSpace)) {
			setPosition( -(mNumOfLine - blankSpace)-1);
		} else if (mPosition > (showingText.getNumberOfStockedElement() - blankSpace)) {
			setPosition( showingText.getNumberOfStockedElement()- blankSpace);
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

