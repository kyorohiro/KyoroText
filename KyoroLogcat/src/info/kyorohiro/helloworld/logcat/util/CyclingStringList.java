package info.kyorohiro.helloworld.logcat.util;

import android.graphics.Paint;

public class CyclingStringList {
	private Paint mPaint = null;

	private final int mLength;
	private final String[] mList;
	private int mWidth = 1000;
	private int mNextAddedPoint = 0;
	private boolean mStartPointIsZero = true;

	public CyclingStringList(int listSize,int width, int textSize) {
		mLength = listSize;
		mList = new String[mLength];
		mWidth = width;
		mPaint = new Paint();
		mPaint.setTextSize(textSize);
	}

	public void setWidth(int w) {
		mWidth = w;
	}

	public int getLength() {
		return mLength;
	}

	public Paint getPaint() {
		return mPaint;
	}

	public synchronized void clear(){
		mNextAddedPoint = 0;
		mStartPointIsZero = true;
	}

	public synchronized void addLinePerBreakText(String line) {
		if (line == null) {
			line = "";
		}
		//
		// todo ‚±‚±‚É‰üs‹@”\‚ð’Ç‰Á‚·‚é—\’è
		//
		int len = 0;
		while(true) {
			len = mPaint.breakText(line, true, mWidth, null);
			if(len == line.length()) {
				addLine(line);
				break;
			}
			else {
				addLine(line.substring(0, len));
				line =line.substring(len, line.length());
			}
		}
	}

	public synchronized void addLine(String line) {
		if (line == null) {
			line = "";
		}

		mList[mNextAddedPoint] = line;
		mNextAddedPoint = mNextAddedPoint + 1;

		if (mNextAddedPoint >= mList.length) {
			mStartPointIsZero = false;
		}

		mNextAddedPoint = mNextAddedPoint % mList.length;
	}

	public synchronized String[] getLast(int count) {
		int lengthOfList = count;
		int max = getMax();
		if (max <= lengthOfList) {
			lengthOfList = max;
		}

		String[] ret = new String[lengthOfList];
		for (int i = 0; i < lengthOfList; i++) {
			ret[i] = getLine(max - lengthOfList + i);
		}
		return ret;
	}

	public synchronized String[] getlines(int start, int end) {
		int max = getMax();
		if (max < end) {
			end = max;
		}
		if (start < 0) {
			start = 0;
		}
		if (start > end) {
			int t = start;
			start = end;
			end = t;
		}
		int lengthOfList = end - start;

		String[] ret = new String[lengthOfList];
		for (int i = 0; i < end - start; i++) {
			ret[i] = getLine(i + start);
		}
		return ret;
	}

	public String getLine(int i) {
		int num = i % mList.length;
		if (mStartPointIsZero) {
			return mList[num];
		} else {
			num = (mNextAddedPoint + num) % mList.length;
			return mList[num];
		}
	}

	public int getMax() {
		if (mStartPointIsZero) {
			return mNextAddedPoint;
		} else {
			return mList.length;
		}
	}

}
