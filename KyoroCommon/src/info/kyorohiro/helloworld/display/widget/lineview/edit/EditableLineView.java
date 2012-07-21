package info.kyorohiro.helloworld.display.widget.lineview.edit;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import android.graphics.Color;
import android.view.KeyEvent;

import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.CommitText;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.MyInputConnection;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.io.MyBuilder;
import info.kyorohiro.helloworld.io.BigLineDataBuilder.W;

/**
 * This Class は CursorableLineViewにIMEからの編集機能を追加したものです。
 * 
 * [方針]
 *   LineViewへ渡しているテキストデータのDIFFデータをキャッシュする。
 *   表示するときは、毎回DIFFデータをマージして、画面に表示する。
 *
 */
public class EditableLineView extends CursorableLineView {

	private EditableLineViewBuffer mTextBuffer = null;

	public EditableLineView(LineViewBufferSpec v, int textSize, int cashSize) {
		super(new EditableLineViewBuffer(v), textSize, cashSize);
		mTextBuffer = (EditableLineViewBuffer) getLineViewBuffer();
	}

	@Override
	public synchronized void setLineViewBufferSpec(LineViewBufferSpec inputtedText) {
		try {
			lock();
			super.setLineViewBufferSpec(mTextBuffer = new EditableLineViewBuffer(inputtedText));
		} finally {
			releaseLock();
		}
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (inside(x, y)) {
			showIME();
		}
		return super.onTouchTest(x, y, action);
	}

	private boolean inside(int x, int y) {
		if (0 < x && x < this.getWidth() && 0 < y && y < this.getHeight()) {
			return true;
		} else {
			return false;
		}
	}

	private void showIME() {
		SimpleStage stage = getStage(this);
		stage.showInputConnection();		
	}

	private boolean editable() {
		if (getMode() == CursorableLineView.MODE_EDIT||getMode().equals(CursorableLineView.MODE_EDIT)) {
			return true;
		} else {
			return false;
		}
	}

	private MyInputConnection getMyInputConnection() {
		SimpleStage stage = getStage(this);
		MyInputConnection c = stage.getCurrentInputConnection();
		return c;
	}

	private void updateOnIMEOutput() {
		MyInputConnection c = getMyInputConnection();
		if (c == null) {return;} // <-- ここをとおることはない

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
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		if(editable()) {
			try {
				mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
				updateOnIMEOutput();
				getLeft().setCursorRow(mTextBuffer.getRow());
				getLeft().setCursorCol(mTextBuffer.getCol());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		super.paint(graphics);
	}



	

	//
	// 親を上書きする。
	//
	public static class EditableLineViewBuffer implements LineViewBufferSpec, W {

		private LineViewBufferSpec mOwner = null;
		private int mCursorRow = 0;// line
		private int mCursorCol = 0;// point

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
		
		
		
		
//
// following code is now creating
//f
		
		


		@Override
		public LineViewData get(int i) {
			if (__mData__.containsKey(i)) {
				return __mData__.get(i);
			} else {
				return mOwner.get(getOwnerY(i));
			}
		}

		private int getOwnerY(int i){
			int plus = 0;
			Set<Integer> indexs = mIndex.keySet();
			for(Integer ii : indexs){
				if(i<ii){
					plus += mIndex.get(ii);
				}
			}
			return i-plus;
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


		private void currentDataToCash(int currentCol) {
			if (!__mData__.containsKey(currentCol)) {
				__mData__.put(currentCol, get(currentCol));
			}
			
		}

		private void pushCommit(CharSequence text, int currentRow, int currentCol) {
			if (currentCol < 0) {return;}
			if (!__mData__.containsKey(currentCol)) {
				__mData__.put(currentCol, get(currentCol));
			}

			LineViewData data = get(currentCol);
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
			__mData__.put(currentCol,
					new LineViewData(c.subSequence(0, len), Color.BLUE,
							data.getStatus()));

			android.util.Log.v("kiyo", "MOMO--0--");
			if (c.length() > len) {
				android.util.Log.v("kiyo", "MOMO--1--");
				if('\n'==c.charAt(c.length()-1)
						||data.getStatus() == LineViewData.INCLUDE_END_OF_LINE){
					android.util.Log.v("kiyo", "MOMO--1_1--");
					if(!mIndex.containsKey(getOwnerY(currentCol))){
						mIndex.put(getOwnerY(currentCol), 0);
					}
					android.util.Log.v("kiyo", "MOMO--1_2--");
					mIndex.put(getOwnerY(currentCol),
							mIndex.get(getOwnerY(currentCol))+1);

					android.util.Log.v("kiyo", "MOMO--1_3--");
					if (!__mData__.containsKey(currentCol+1)) {
						__mData__.put(currentCol+1, get(currentCol+1));
					}
					android.util.Log.v("kiyo", "MOMO--1_4--");
					pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);
//					mData.put(currentCol+1,
//							new LineViewData(c.subSequence(len, c.length()), 
//									Color.RED,
//									data.getStatus()));
					android.util.Log.v("kiyo", "MOMO"+(currentCol+1)+","+c.subSequence(len, c.length()));

				} else {
					android.util.Log.v("kiyo", "MOMO--2--");
					pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);
				}
			}
		}

		private LinkedHashMap<Integer, LineViewData> __mData__ = new LinkedHashMap<Integer, LineViewData>();
		private LinkedHashMap<Integer, Integer> mIndex = new LinkedHashMap<Integer, Integer>();
	}

}
