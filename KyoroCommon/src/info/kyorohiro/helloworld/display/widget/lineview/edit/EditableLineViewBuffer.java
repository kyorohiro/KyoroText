package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.BigLineDataBuilder.W;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import android.graphics.Color;

public class EditableLineViewBuffer implements LineViewBufferSpec, W {

	private int mCursorRow = 0;
	private int mCursorCol = 0;

	private LineViewBufferSpec mOwner = null;
	private HashMap<Integer, LineViewData> mDiff = new HashMap<Integer, LineViewData>();
	private HashMap<Integer, Integer> mIndex = new HashMap<Integer, Integer>();
// HashMap<Integer, Integer> mDelete = new HashMap<Integer, Integer>();
	private ArrayList<Integer> mDelete =  new ArrayList<Integer>();

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
		return mOwner.getNumberOfStockedElement() + numOfAddFromDiff();
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

	@Override
	public void setCursor(int row, int col) {
		mCursorRow = row;
		mCursorCol = col;
	}

	public int getRow() {
		return mCursorRow;
	}

	public int getCol() {
		return mCursorCol;
	}

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

	@Override
	public LineViewData get(int i) {
		if (includeDiff(i)) {
			return mDiff.get(i);
		} else {
			try{
				return mOwner.get(toOwnerY(i));
			}catch(IndexOutOfBoundsException e){
				return end_of_line;
			}
		}
	}
	private LineViewData end_of_line = new LineViewData("++END OF LINE++", Color.RED, LineViewData.INCLUDE_END_OF_LINE);


	private boolean includeDiff(int i) {
		if (mDiff.containsKey(i)) {
			return true;
		} else {
			return false;
		}
	}

	private int toOwnerY(int i) {
		int plus = 0;
		//
		Set<Integer> indexs = mIndex.keySet();
		for (Integer ii : indexs) {
			if (ii < i) {
				plus += mIndex.get(ii);
			}
		}

		//
		int del = 0;
		int next = i-plus;
		//Set<Integer> deletes = mDelete.keySet();
		int[] ret = sort(mDelete);
//		android.util.Log.v("kiyo","-------------");
//		for(int r:ret){
//			android.util.Log.v("kiyo",""+r);
//		}
//		android.util.Log.v("kiyo","=============");
		for(int dd: ret) {
			if(dd<=next){
				next++;
			}
		}
		return next;
	}

	//
	// いけてないソート
	private int[] sort(ArrayList<Integer> key){
		int[] ret = new int[key.size()];
		int i=0;
		for(int ii : key){
			ret[i++] = ii;
		}

		int s =0;
		for(i=0;i<ret.length;i++){
			s=i;
			for(int j=i+1;j<ret.length;j++){
				if(ret[s]>ret[j]){
					s=j;
				}
			}
			if(s!=i){
				int t = ret[i];
				ret[i] = ret[s];
				ret[s] = t;
			}
		}
		return ret;
	}

	private int numOfAddFromDiff() {
		int plus = 0;
		Set<Integer> indexs = mIndex.keySet();
		for (Integer ii : indexs) {
			plus += mIndex.get(ii);
		}
		return plus;
	}

	public void crlfOne() {
		int currentCol = mCursorCol;
		int currentRow =  mCursorRow;
		LineViewData d = get(currentCol);
		if(currentRow > d.length()){
			currentRow = d.length();
		}

		CharSequence c = d.subSequence(currentRow, d.length());
		mDiff.put(currentCol,
				new LineViewData(d.subSequence(0, currentRow),Color.CYAN,
				LineViewData.INCLUDE_END_OF_LINE));

		// currentCol +=1;
		if (mIndex.containsKey(toOwnerY(currentCol))) {
			mIndex.put(toOwnerY(currentCol),
					mIndex.get(toOwnerY(currentCol)) + 1);
		} else {
			mIndex.put(toOwnerY(currentCol), 1);
		}
		insertDiff(currentCol + 1, new LineViewData(c, Color.YELLOW,
				LineViewData.INCLUDE_END_OF_LINE));
		mCursorCol +=1;
		mCursorRow = 0;
	}

	public void deleteOne() {
		LineViewData data = get(mCursorCol);
		if (data == null) {
			data = new LineViewData("", Color.GREEN,
					LineViewData.INCLUDE_END_OF_LINE);
		}
		if (mCursorRow >= data.length()) {
			mCursorRow = data.length();
		}
		if (mCursorRow <= 0) {
			if (mCursorCol >=0) {
				//
				mCursorCol--;
				LineViewData next = get(mCursorCol);
				mCursorRow = next.length();
				//
				int col = mCursorCol + 1;
				if (data.length() <= 0) {
					printDiff();
					printIndex();
					if (mIndex.containsKey(toOwnerY(col-1))) {
						mIndex.put(toOwnerY(col-1),mIndex.get(toOwnerY(col-1)) - 1);
					} else {
						int _c = toOwnerY(col);
						if(!mDelete.contains(_c)){
							mDelete.add(_c);
						}
					}
					mDiff.remove(col);
					printDiff();
					printIndex();
				}

			}
			return;
		}

		CharSequence a = data.subSequence(0, mCursorRow - 1);
		CharSequence b;
		if (mCursorRow < data.length()) {
			b = data.subSequence(mCursorRow, data.length());
		} else {
			b = "";
		}
		mDiff.put(mCursorCol, new LineViewData(a.toString() + b.toString(),
				data.getColor(), data.getStatus()));
		mCursorRow--;
	}

	private void printIndex() {
		Set<Integer> keys = mIndex.keySet();
		android.util.Log.v("kiyokiyo",
				"pIndex:start-----------------------------");
		for (Integer k : keys) {
			android.util.Log
					.v("kiyokiyo", "pindex:[" + k + "]:" + mIndex.get(k));
		}
		android.util.Log.v("kiyokiyo",
				"pIndex:stop==============================");
	}

	private void printDiff() {
		Set<Integer> keys = mDiff.keySet();
		android.util.Log.v("kiyokiyo",
				"pdiff:start-----------------------------");
		for (Integer k : keys) {
			android.util.Log.v("kiyokiyo", "pdiff:[" + k + "]:" + mDiff.get(k));
		}
		android.util.Log.v("kiyokiyo",
				"pdiff:stop==============================");
	}

	//
	// よさげでないデータ構造なので、直す。
	private void insertDiff(int currentCol, LineViewData data) {
		HashMap<Integer, LineViewData> ret = new HashMap<Integer, LineViewData>();
		Set<Integer> keys = mDiff.keySet();
		for (Integer k : keys) {
			if ((currentCol) <= k) {
				if (mDiff.containsKey(k)) {
					ret.put(k + 1, mDiff.get(k));
				}
			} else {
				if (mDiff.containsKey(k)) {
					ret.put(k, mDiff.get(k));
				}
			}
		}
		ret.put(currentCol, data);
		mDiff.clear();
		mDiff = ret;
	}





//==============================================================================================
//
//==============================================================================================


	private CharSequence extract(CharSequence commit, int currentRow,
			int currentCol) {
		LineViewData data = get(currentCol);
		if (data == null) {
			data = new LineViewData("", Color.GREEN,
					LineViewData.INCLUDE_END_OF_LINE);
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
		if (currentCol < 0) {
			return;
		}

		LineViewData data = get(currentCol);
		if (data == null) {
			data = new LineViewData("", Color.GREEN,
					LineViewData.INCLUDE_END_OF_LINE);
		}

		CharSequence c = extract(text, currentRow, currentCol);

		// 再配置する。
		int len = getBreakText().breakText(c, 0, c.length(),
				getBreakText().getWidth());
		if (c.length() <= len) {
			// 1行におさまるから何もしない
			mDiff.put(currentCol, new LineViewData(c.subSequence(0, len),
					Color.BLUE, data.getStatus()));
		} else {
			mDiff.put(currentCol, new LineViewData(c.subSequence(0, len),
					Color.BLUE, LineViewData.EXCLUDE_END_OF_LINE));

			if ('\n' == c.charAt(c.length() - 1)
					|| data.getStatus() == LineViewData.INCLUDE_END_OF_LINE) {
				// currentCol +=1;
				if (mIndex.containsKey(toOwnerY(currentCol))) {
					mIndex.put(toOwnerY(currentCol),
							mIndex.get(toOwnerY(currentCol)) + 1);
				} else {
					mIndex.put(toOwnerY(currentCol), 1);
				}
				insertDiff(currentCol + 1, new LineViewData("", Color.YELLOW,
						LineViewData.INCLUDE_END_OF_LINE));
				pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);
			} else {
				pushCommit(c.subSequence(len, c.length()), 0, currentCol + 1);
			}
		}
		// printIndex();
		// printDiff();
	}

}