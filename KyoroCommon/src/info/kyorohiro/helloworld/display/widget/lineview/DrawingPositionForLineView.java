package info.kyorohiro.helloworld.display.widget.lineview;


public class DrawingPositionForLineView {
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

	public void updateInfo(LineView view, int position, int height, int textSize, 
			double scale,LineViewBufferSpec showingText) {
		mNumOfLine = (int)(height / (textSize*1.2*scale));
		resetInfo(view);
		mPosition = view.getPositionY();
		mStart = start(showingText);
		mEnd = end(showingText);
		mBlank = 0;
	}

	private void resetInfo(LineView view) {
		int blankSpace = mNumOfLine;
		int pos = view.getPositionY();
		LineViewBufferSpec buffer = view.getLineViewBuffer();

		if (pos <-(mNumOfLine - blankSpace)) {
			view.setPositionY(-(mNumOfLine - blankSpace) - 1, true);
		} else if (pos > (buffer.getNumberOfStockedElement() - blankSpace)) {
			view.setPositionY(buffer.getNumberOfStockedElement()- blankSpace, true);
		}
	}

	private int start(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		if(referPoint < 0) {
			referPoint = 0;
		}
		return referPoint;
	}

	public int end(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = start(showingText);
		int end = referPoint + mNumOfLine;
		if (end < 0) {
			end = 0;
		}
		if (end >= numOfStackedString) {
			end = numOfStackedString;
		}
		return end;
	}

}
