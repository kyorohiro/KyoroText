package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.BigLineDataBuilder.W;

import java.util.LinkedHashMap;
import java.util.Set;

import android.graphics.Color;

public class EditableLineViewBuffer implements LineViewBufferSpec, W {

	private int mCursorRow = 0;
	private int mCursorCol = 0;

	private LineViewBufferSpec mOwner = null;
	private LinkedHashMap<Integer, LineViewData> mDiff = new LinkedHashMap<Integer, LineViewData>();
	private LinkedHashMap<Integer, Integer> mIndex = new LinkedHashMap<Integer, Integer>();

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
		for (int i = start, j = 0; i < end; i++,j++) {ret[j] = get(i);}
		return ret;
	}
	@Override
	public void setCursor(int row, int col) {
		mCursorRow = row;
		mCursorCol = col;
	}
	public int getRow(){return mCursorRow;}
	public int getCol(){return mCursorCol;}
	@Override
	public int getMaxOfStackedElement() {
		return mOwner.getMaxOfStackedElement();
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

	@Override
	public LineViewData get(int i) {
		if (includeDiff(i)) {
			return mDiff.get(i);
		} else {
			return mOwner.get(toOwnerY(i));
		}
	}

	private boolean includeDiff(int i) {
		if (mDiff.containsKey(i)) {
			return true;
		} else {
			return false;
		}
	}

	private int toOwnerY(int i){
		int plus = 0;
		Set<Integer> indexs = mIndex.keySet();
		for(Integer ii : indexs){
			if(ii<i){
				plus += mIndex.get(ii);
			}
		}
		return i-plus;
	}

	private CharSequence extract(CharSequence commit, int currentRow, int currentCol) {
		LineViewData data = get(currentCol);
		if (data == null) {
			data = new LineViewData("", Color.GREEN, LineViewData.INCLUDE_END_OF_LINE);
		}

		CharSequence c = null;
		if (currentRow < data.length()) {
			CharSequence a = data.subSequence(0, currentRow);
			CharSequence b = data.subSequence(currentRow, data.length());
			c = a.toString() + commit + b.toString();
		} else {
			c = data.toString() + commit;
		}
		return c;
	}

	private void pushCommit(CharSequence text, int currentRow, int currentCol) {
		if (currentCol < 0) {return;}

		LineViewData data = get(currentCol);
		if (data == null) {
			data = new LineViewData("", Color.GREEN, LineViewData.INCLUDE_END_OF_LINE);
		}

		CharSequence c = extract(text, currentRow, currentCol);

		// 再配置する。
		int len = getBreakText().breakText(c, 0, c.length(),getBreakText().getWidth());
		mDiff.put(currentCol,new LineViewData(c.subSequence(0, len), Color.BLUE, data.getStatus()));
		if(c.length() <= len) {
			// 1行におさまるから何もしない
		} else {
			if('\n'==c.charAt(c.length()-1)||data.getStatus() == LineViewData.INCLUDE_END_OF_LINE){			
				pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);						
			} else {
				pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);			
			}
		}
/*
		android.util.Log.v("kiyo", "MOMO--0--");
		if (c.length() > len) {
			android.util.Log.v("kiyo", "MOMO--1--");
			if('\n'==c.charAt(c.length()-1)
					||data.getStatus() == LineViewData.INCLUDE_END_OF_LINE){
				android.util.Log.v("kiyo", "MOMO--1_1--");
				if(!mIndex.containsKey(toOwnerY(currentCol))){
					mIndex.put(toOwnerY(currentCol), 0);
				}
				android.util.Log.v("kiyo", "MOMO--1_2--");
				mIndex.put(toOwnerY(currentCol),
						mIndex.get(toOwnerY(currentCol))+1);

				android.util.Log.v("kiyo", "MOMO--1_3--");
				if (!mDiff.containsKey(currentCol+1)) {
					mDiff.put(currentCol+1, get(currentCol+1));
				}
				android.util.Log.v("kiyo", "MOMO--1_4--");
				pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);
//				mData.put(currentCol+1,
//						new LineViewData(c.subSequence(len, c.length()), 
//								Color.RED,
//								data.getStatus()));
				android.util.Log.v("kiyo", "MOMO"+(currentCol+1)+","+c.subSequence(len, c.length()));

			} else {
				android.util.Log.v("kiyo", "MOMO--2--");
				pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);
			}
		}*/
	}
}
