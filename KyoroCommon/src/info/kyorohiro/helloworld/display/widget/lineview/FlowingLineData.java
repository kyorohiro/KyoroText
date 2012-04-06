package info.kyorohiro.helloworld.display.widget.lineview;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.android.util.SimpleLock;
import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.util.CyclingListForAsyncDuplicate;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;
import android.graphics.Paint;

public class FlowingLineData extends CyclingListForAsyncDuplicate<FlowingLineDatam> 
implements SimpleLockInter {
	private Paint mPaint = null;
	private int mWidth = 1000;
	private int mNumOfLineAdded = 0;
	private Pattern mFilter = null;
	private SimpleLock mLock = new SimpleLock();

	public FlowingLineData(int listSize, int width, int textSize) {
		super(new CyclingList<FlowingLineDatam>(listSize),listSize);
		mWidth = width;
		mPaint = new Paint();
		mPaint.setTextSize(textSize);
	}

	@Override
	public void beginLock() {
		mLock.begin();
	}


	@Override
	public void endLock() {
		mLock.end();
	}

	public int getTextSize() {
		return (int)mPaint.getTextSize();
	}

	public void setTextSize(int height) {
		mPaint.setTextSize(height);
	}

	public int getNumOfLineAdded() {
		try {
			mLock.begin();
			int t = mNumOfLineAdded;
			t = 0;
			return t;
		} finally {
			mLock.end();
		}
	}

	public void setWidth(int w) {
		mWidth = w;
	}

	public Paint getPaint() {
		return mPaint;
	}

	public void setFileterText(Pattern filter) {
		mFilter = filter;
	}
	
	public Pattern getFilterText() {
		return mFilter;
	}

	public void addLineToHead(CharSequence line) {
		try {
			mLock.begin();
			this.head(new FlowingLineDatam(line, mCurrentColor,
					FlowingLineDatam.INCLUDE_END_OF_LINE));
		} finally {
			mLock.end();
		}
	}

	public void addLinePerBreakText(CharSequence line) {
		try {
			mLock.begin();

			setColorPerLine(line);

			if (line == null) {
				line = "";
			}
			int len = 0;
			while (true) {
				len = mPaint.breakText(line.toString(), true, mWidth, null);
				if (len == line.length()) {
					mNumOfLineAdded++;
					add(new FlowingLineDatam(line, mCurrentColor,
							FlowingLineDatam.INCLUDE_END_OF_LINE));
					break;
				} else {
					mNumOfLineAdded++;
					add(new FlowingLineDatam(line.subSequence(0, len), mCurrentColor,
							FlowingLineDatam.EXCLUDE_END_OF_LINE));
					line = line.subSequence(len, len+line.length()-len);
					// kiyo
				}
			}
		} finally {
			mLock.end();
		}
	}

	public FlowingLineDatam[] getLastLines(int numberOfRetutnArrayElement) {
		try {
			mLock.begin();
			if (numberOfRetutnArrayElement < 0) {
				return new FlowingLineDatam[0];
			}
			FlowingLineDatam[] ret = new FlowingLineDatam[numberOfRetutnArrayElement];
			return (FlowingLineDatam[]) getLast(ret, numberOfRetutnArrayElement);
		} finally {
			mLock.end();
		}
	}

	public FlowingLineDatam[] getLines(int start, int end) {
		try {
			mLock.begin();

			if (start > end) {
				return new FlowingLineDatam[0];
			}
			FlowingLineDatam[] ret = new FlowingLineDatam[end - start];
			return getElements(ret, start, end);
		} finally {
			mLock.end();
		}
	}

	public FlowingLineDatam getLine(int i) {
		try {
			mLock.begin();
			return (FlowingLineDatam) super.get(i);
		} finally {
			mLock.end();
		}
	}

	private int mCurrentColor = Color.parseColor("#ccc9f486");
	public void setCurrentLineColor(int color) {
		mCurrentColor = color;
	}

	protected void setColorPerLine(CharSequence line) {
	}

	private ArrayList<FlowingLineDatam> mCashForFiltering = new ArrayList<FlowingLineDatam>();
	@Override
	protected boolean filter(FlowingLineDatam t) {
		if(mFilter == null){
			return true;
		}
		if(t != null) {
			mCashForFiltering.add(t);
			if(t.getStatus() == FlowingLineDatam.INCLUDE_END_OF_LINE) {
				StringBuilder builder = new StringBuilder("");
				for(FlowingLineDatam d : mCashForFiltering){
					builder.append(d);
				}
				String i = builder.toString();
				Matcher m = mFilter.matcher(i);
				if(m.find()){
					for(FlowingLineDatam d: mCashForFiltering) {
						getDuplicatingList().add(d);
					}
				}
				mCashForFiltering.clear();
			}
		}

		// ever time return false;
		return false;
	}

}
