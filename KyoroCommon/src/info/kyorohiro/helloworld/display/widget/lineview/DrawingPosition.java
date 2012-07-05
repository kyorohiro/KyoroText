package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.util.CyclingListInter;
import info.kyorohiro.helloworld.util.LineViewBufferSpec;

public class DrawingPosition {
	private int mPosition = 0;
	private int mNumOfLine = 0;
	private int mStart = 0;
	private int mEnd = 0;
	private int mBlank = 0;

	public int getNumOfLine() {
		return mNumOfLine;
	}

	public int getStart() {
		return mStart;
	}

	public int getEnd() {
		return mEnd;
	}

	public int getBlank() {
		return mBlank;
	}

	public void updateInfo(int position, int height, int textSize, 
			double scale,LineViewBufferSpec showingText) {
		mPosition = position;
		mNumOfLine = (int)(height / (textSize*1.2*scale));
		mStart = start(showingText);
		mEnd = end(showingText);
		mBlank = blank(showingText);
	}

	public int start(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		int start = referPoint;
		if (start < 0) {
			start = 0;
		}
		return start;
	}


	public int end(LineViewBufferSpec showingText) {
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

	public int blank(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		int blank = 0;
		boolean uppserSideBlankisViewed = (referPoint) < 0;
		if (uppserSideBlankisViewed) {
			blank = -1 * referPoint;
		}
		return blank;
	}

	public int getLen() {
		if (mStart > mEnd) {
			return 0;
		} else {
			return mEnd-mStart;
		}
	}
}
