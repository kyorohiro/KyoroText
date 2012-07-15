package info.kyorohiro.helloworld.display.widget.flowinglineview;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.util.CyclingListForAsyncDuplicate;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;
import android.graphics.Paint;

public class FlowingLineBuffer extends CyclingListForAsyncDuplicate<LineViewData> 
implements SimpleLockInter {
	private Paint mPaint = null;
	private int mWidth = 1000;
	private int mNumOfLineAdded = 0;
	private Pattern mFilter = null;
	private boolean locked = false;
	private Thread cThread = null;

	public FlowingLineBuffer(int listSize, int width, int textSize) {
		super(new CyclingList<LineViewData>(listSize),listSize);
		mWidth = width;
		mPaint = new Paint();
		mPaint.setTextSize(textSize);
	}

	public int getTextSize() {
		return (int)mPaint.getTextSize();
	}

	public void setTextSize(int height) {
		mPaint.setTextSize(height);
	}

	public synchronized int getNumOfLineAdded() {
		lock();
		int t = mNumOfLineAdded;
		t = 0;
		return t;
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

	public synchronized void addLineToHead(CharSequence line) {
		lock();
		this.head(new LineViewData(line, mCurrentColor,
				LineViewData.INCLUDE_END_OF_LINE));
	}

	public synchronized void addLinePerBreakText(CharSequence line) {
		lock();
		setColorPerLine(line);

		if (line == null) {
			line = "";
		}
		int len = 0;
		while (true) {
			len = mPaint.breakText(line.toString(), true, mWidth, null);
			if (len <= line.length()) {
				mNumOfLineAdded++;
				add(new LineViewData(line, mCurrentColor,
						LineViewData.INCLUDE_END_OF_LINE));
				break;
			} else {
				mNumOfLineAdded++;
				add(new LineViewData(line.subSequence(0, len), mCurrentColor,
						LineViewData.EXCLUDE_END_OF_LINE));
				line = line.subSequence(len, line.length()-len);
				// kiyo
			}
		}
	}

	public synchronized LineViewData[] getLastLines(int numberOfRetutnArrayElement) {
		lock();
		if (numberOfRetutnArrayElement < 0) {
			return new LineViewData[0];
		}
		LineViewData[] ret = new LineViewData[numberOfRetutnArrayElement];
		return (LineViewData[]) getLast(ret, numberOfRetutnArrayElement);
	}

	public synchronized LineViewData[] getLines(int start, int end) {
		lock();
		if (start > end) {
			return new LineViewData[0];
		}
		LineViewData[] ret = new LineViewData[end - start];
		return getElements(ret, start, end);
	}

	public synchronized LineViewData getLine(int i) {
		lock();
		return (LineViewData) super.get(i);
	}

	private int mCurrentColor = Color.parseColor("#ccc9f486");
	public void setCurrentLineColor(int color) {
		mCurrentColor = color;
	}

	protected void setColorPerLine(CharSequence line) {
	}

	private ArrayList<LineViewData> mCashForFiltering = new ArrayList<LineViewData>();
	@Override
	protected boolean filter(LineViewData t) {
		if(mFilter == null){
			return true;
		}
		if(t != null) {
			mCashForFiltering.add(t);
			if(t.getStatus() == LineViewData.INCLUDE_END_OF_LINE) {
				StringBuilder builder = new StringBuilder("");
				for(LineViewData d : mCashForFiltering){
					builder.append(d);
				}
				String i = builder.toString();
				Matcher m = mFilter.matcher(i);
				if(m.find()){
					for(LineViewData d: mCashForFiltering) {
						getDuplicatingList().add(d);
					}
				}
				mCashForFiltering.clear();
			}
		}

		// ever time return false;
		return false;
	}
	@Override
	public synchronized void beginLock() {
		locked = true;
		cThread = Thread.currentThread();
	}

	@Override
	public synchronized void endLock() {
		locked = false;
		notifyAll();
	}

	private synchronized void lock() {
		if(locked&&cThread !=null&& cThread != Thread.currentThread()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
