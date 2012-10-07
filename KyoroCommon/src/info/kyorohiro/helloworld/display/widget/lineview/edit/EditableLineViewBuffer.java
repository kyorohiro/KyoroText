package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;

public class EditableLineViewBuffer implements LineViewBufferSpec, IMEClient {

	private int mCursorRow = 0;
	private int mCursorCol = 0;
	private Differ mDiffer = new Differ();

	private LineViewBufferSpec mOwner = null;

	public EditableLineViewBuffer(LineViewBufferSpec owner) {
		super();
		mOwner = owner;
	}

	@Override
	public int getNumOfAdd() {
		return mOwner.getNumOfAdd();
	}

	@Override
	public void clearNumOfAdd() {
		mOwner.clearNumOfAdd();
	}

	@Override
	public int getNumberOfStockedElement() {
		return mOwner.getNumberOfStockedElement() + mDiffer.length();
	}


	@Override
	public LineViewData get(int i) {
		return mDiffer.get(mOwner, i);
	}

	@Override
	public BreakText getBreakText() {
		return mOwner.getBreakText();
	}

	@Override
	public LineViewData[] getElements(LineViewData[] ret, int start, int end) {
		for (int i = start, j = 0; i < end; i++, j++) {
			ret[j] = get(i);
		}
		return ret;
	}

	public int getRow() {
		return mCursorRow;
	}

	public int getCol() {
		return mCursorCol;
	}

	@Override
	public void setCursor(int row, int col) {
		mCursorRow = row;
		mCursorCol = col;
	}

	@Override
	public void pushCommit(CharSequence text, int cursor) {
		android.util.Log.v("log","col="+mCursorCol + ",row="+mCursorRow);
		int index = getNumberOfStockedElement();//+1;
		if(index > mCursorCol) {
			index = mCursorCol;
		}
		mDiffer.addLine(index, text);
		moveCursor(text.length());
	}

	@Override
	public int getMaxOfStackedElement() {
		return mOwner.getMaxOfStackedElement();
	}


	private void moveCursor(int length) {
		LineViewData cd = get(mCursorCol);
		while (true) {
			int m = (length + mCursorRow) - cd.length();
			if (m > 0) {
				mCursorRow = 0;
				mCursorCol += 1;
				length = m;
			} else {
				mCursorRow += length;
				break;
			}
		}
	}


}