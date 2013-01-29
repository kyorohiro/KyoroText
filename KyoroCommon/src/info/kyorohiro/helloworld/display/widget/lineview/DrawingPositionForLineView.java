package info.kyorohiro.helloworld.display.widget.lineview;


public class DrawingPositionForLineView {
	private int mPosition = 0;
	private int mNumOfShowingLine = 0;
	private int mStart = 0;
	private int mEnd = 0;
	private int mBlank = 0;


	public int getNumOfLine() {
		return mNumOfShowingLine;
	}

	public int getStart() {
		return mStart;
	}

	public int getEnd() {
		return mEnd;
	}

	//
	@Deprecated
	public int getBlank() {
		return mBlank;
	}

	public void updateInfo(LineView view, int position, int height, int textSize, 
			double scale,LineViewBufferSpec buffer) {
		mNumOfShowingLine = (int)(height / (textSize*1.2*scale));
		//
 
		int pos = buffer.getNumberOfStockedElement() - mNumOfShowingLine;

		// end position
		if (view.getPositionY() < -1*mNumOfShowingLine/3) {
			view.setPositionY(-1*mNumOfShowingLine/3, true);
			mPosition = -1*mNumOfShowingLine/3;
		}
		// begin position
		else if (view.getPositionY() > pos) {
			mPosition  = pos;
			view.setPositionY(pos, true);
		}

		mPosition = view.getPositionY();
		mStart = start(buffer);
		mEnd = end(buffer);
		mBlank = 0;
	}


	private int start(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfShowingLine);
		if(referPoint < 0) {
			referPoint = 0;
		}
		return referPoint;
	}

	public int end(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = start(showingText);
		int end = referPoint + mNumOfShowingLine;
		if (end < 0) {
			end = 0;
		}
		if (end >= numOfStackedString) {
			end = numOfStackedString;
		}
		return end;
	}

}
