package info.kyorohiro.helloworld.display.widget.lineview;

public class DrawingPositionForLineView {
	private boolean mfittableToView = false;
	private int mPosition = 0;
	private int mNumOfLine = 0;
	private int mStart = 0;
	private int mEnd = 0;
	private int mBlank = 0;

	public void fittableToView(boolean fit) {
		mfittableToView = fit;
	}
	public boolean fittableToView() {
		return mfittableToView;
	}

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
		
		if(!mfittableToView){
			// �p�E���X����悤�ȃA�j���[�V��������B
//			System.out.println("===============do"+position+","+height+","+textSize+","+scale);
			mPosition = position;
			if(Math.abs(view.getPositionY()-mPosition)>mNumOfLine/4) {
				if(view.getPositionY()>mPosition) {
					mPosition = view.getPositionY()-mNumOfLine/4;
				} else {
					mPosition = view.getPositionY()+mNumOfLine/4;					
				}
			}
		} else {
			// �p�E���X����悤�ȃA�j���[�V�������Ȃ��B
			mPosition = view.getPositionY();
		}
		mStart = start(showingText);
		mEnd = end(showingText);
		mBlank = blank(showingText);
	}

	private void resetInfo(LineView view) {
		int blankSpace = mNumOfLine;
		if(!mfittableToView){
			blankSpace /=2;
		}
		int pos = view.getPositionY();
		LineViewBufferSpec buffer = view.getLineViewBuffer();

		if (pos <-(mNumOfLine - blankSpace)) {
			view.setPositionY(-(mNumOfLine - blankSpace) - 1, true);
		} else if (pos > (buffer.getNumberOfStockedElement() - blankSpace)) {
			view.setPositionY(buffer.getNumberOfStockedElement()- blankSpace, true);
		}
	}

	public int start(LineViewBufferSpec showingText) {
		int referPoint = referPoint(showingText);
		int start = referPoint;
		if (start < 0) {
			start = 0;
		}
		return start;
	}

	private int referPoint(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = numOfStackedString - (mPosition + mNumOfLine);
		if(mfittableToView){
			if(referPoint < 0) {
				referPoint = 0;
			}
		}
		return referPoint;
	}

	public int end(LineViewBufferSpec showingText) {
		int numOfStackedString = showingText.getNumberOfStockedElement();
		int referPoint = referPoint(showingText);
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
		if(!mfittableToView){
			int referPoint = referPoint(showingText);
			int blank = 0;
			boolean uppserSideBlankisViewed = (referPoint) < 0;
			if (uppserSideBlankisViewed) {
				blank = -1 * referPoint;
			}
			return blank;
		} else {
			return 0;
		}
	}

	public int getLen() {
		if (mStart > mEnd) {
			return 0;
		} else {
			return mEnd-mStart;
		}
	}
}
