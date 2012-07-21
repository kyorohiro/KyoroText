package info.kyorohiro.helloworld.display.widget.lineview;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import android.graphics.Color;
import android.view.KeyEvent;

import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.CommitText;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.MyInputConnection;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.io.MyBuilder;
import info.kyorohiro.helloworld.io.BigLineDataBuilder.W;

public class EditableLineView extends CursorableLineView {

	private EditableLineViewBuffer mTextBuffer = null;

	public EditableLineView(LineViewBufferSpec v) {
		super(new EditableLineViewBuffer(v), 16, 512);
		mTextBuffer = (EditableLineViewBuffer) getLineViewBuffer();
	}

	public EditableLineView(LineViewBufferSpec v, int textSize, int cashSize) {
		super(new EditableLineViewBuffer(v), textSize, cashSize);
		mTextBuffer = (EditableLineViewBuffer) getLineViewBuffer();
	}

	@Override
	public synchronized void setLineViewBufferSpec(
			LineViewBufferSpec inputtedText) {
		try {
			lock();
			super.setLineViewBufferSpec(mTextBuffer = new EditableLineViewBuffer(
					inputtedText));
		} finally {
			releaseLock();
		}
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (0 < x && x < this.getWidth() && 0 < y && y < this.getHeight()) {
			SimpleStage stage = getStage(this);
			stage.showInputConnection();
		}
		return super.onTouchTest(x, y, action);
	}

	@Override
	public boolean onKeyDown(int keycode) {
		if (keycode == KeyEvent.KEYCODE_BACKSLASH) {
			android.util.Log.v("kiyo", "NN:BS");
		} else {
			android.util.Log.v("kiyo", "NN:" + keycode);
		}
		return super.onKeyDown(keycode);
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {

		SimpleStage stage = getStage(this);
		MyInputConnection c = stage.getCurrentInputConnection();
		if (c == null) {
			return;
		}

		try {
			mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft()
					.getCursorCol());
			while (true) {
				CommitText text = c.popFirst();
				if (text != null) {
					mTextBuffer.pushCommit(text.getText(),
							text.getNewCursorPosition());
					getStage(this).resetTimer();
				} else {
					break;
				}
			}
			
			getLeft().setCursorRow(mTextBuffer.getRow());
			getLeft().setCursorCol(mTextBuffer.getCol());
			android.util.Log.v("kiyo", "row,cor="+getLeft().getCursorRow()+","+ getLeft()
					.getCursorCol());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		super.paint(graphics);
	}

	//
	// 親を上書きする。
	//
	public static class EditableLineViewBuffer implements LineViewBufferSpec, W {

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
			return mOwner.getNumberOfStockedElement();
		}

		@Override
		public BreakText getBreakText() {
			return mOwner.getBreakText();
		}

		@Override
		public LineViewData[] getElements(LineViewData[] ret, int start, int end) {
			// todo
			// refactaring 親クラスがまったく呼ばれない!1
			for (int i = start, j = 0; i < end; i++) {
				ret[j] = get(i);
				j++;
			}
			return ret;
		}

		@Override
		public LineViewData get(int i) {
			if (mData.containsKey(i)) {
				android.util.Log.v("kiyo", "get(" + i + ")data:"
						+ mData.get(i).toString());
				return mData.get(i);
			} else {
				android.util.Log.v("kiyo",
						"get(" + i + ")owner:" + mOwner.get(i).toString());
				return mOwner.get(i);
			}
		}

		@Override
		public int getMaxOfStackedElement() {
			return mOwner.getMaxOfStackedElement();
		}

		LineViewBufferSpec getLineViewBufferSpec() {
			return this;
		}

		@Override
		public void pushCommit(CharSequence text, int cursor) {
			pushCommit(text, mCursorRow, mCursorCol);
			moveCursor(text.length());
		}

		private void moveCursor(int length) {
			LineViewData cd = get(mCursorCol);
			while (true) {
				int m = (length + mCursorRow)-cd.length();
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

		private void pushCommit(CharSequence text, int currentRow,
				int currentCol) {

			if (currentCol < 0) {
				return;
			}
			if (!mData.containsKey(currentCol)) {
				mData.put(currentCol, mOwner.get(currentCol));
			}

			LineViewData data = mData.get(currentCol);
			if (data == null) {
				data = new LineViewData("", Color.GREEN,
						LineViewData.INCLUDE_END_OF_LINE);
			}
			CharSequence c = null;
			if (currentRow < data.length()) {
				CharSequence a = data.subSequence(0, currentRow);
				CharSequence b = data.subSequence(currentRow, data.length());
				c = a.toString() + text + b.toString();
			} else {
				c = data.toString() + text;
			}
			android.util.Log.v("kiyo", "LP=" + c.length() + "," + currentCol
					+ "," + c+":::"+getBreakText().getWidth());//*9/10);
			int len = getBreakText().breakText(c, 0, c.length(),
					getBreakText().getWidth());
			mData.put(currentCol,
					new LineViewData(c.subSequence(0, len), data.getColor(),
							data.getStatus()));
			if (c.length() > len) {
				android.util.Log.v("kiyo", "LP=" + len + "," + c.length() + ","
						+ currentCol + "," + c.subSequence(len, c.length()));
				pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);
			}
		}

		@Override
		public void setCursor(int row, int col) {
			mCursorRow = row;
			mCursorCol = col;
		}

		public int getRow(){
			return mCursorRow;
		}
		public int getCol(){
			return mCursorCol;
		}
		private int mCursorRow = 0;// line
		private int mCursorCol = 0;// point
		private LinkedHashMap<Integer, LineViewData> mData = new LinkedHashMap<Integer, LineViewData>();
	}

}
