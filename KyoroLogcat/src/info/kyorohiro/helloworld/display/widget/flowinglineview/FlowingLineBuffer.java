package info.kyorohiro.helloworld.display.widget.flowinglineview;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.CyclingListForAsyncDuplicate;
import info.kyorohiro.helloworld.util.SimpleLockInter;
import android.graphics.Color;
import android.graphics.Paint;

public class FlowingLineBuffer extends CyclingListForAsyncDuplicate<KyoroString> 
implements SimpleLockInter {
	private Paint mPaint = null;
	private int mWidth = 1000;
	private int mNumOfLineAdded = 0;
	private Pattern mFilter = null;
	private boolean locked = false;
	private Thread cThread = null;

	public FlowingLineBuffer(int listSize, int width, int textSize) {
		super(new CyclingList<KyoroString>(listSize),listSize);
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
		this.head(KyoroString.newKyoroStringWithLF(line, mCurrentColor));
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
			if (len == line.length()) {
				mNumOfLineAdded++;
				add(new KyoroString(line+"\n", mCurrentColor));
				break;
			} else {
				mNumOfLineAdded++;
				add(new KyoroString(line.subSequence(0, len), mCurrentColor));
				line = line.subSequence(len, line.length());
				// kiyo
			}
		}
	}

	public synchronized KyoroString[] getLastLines(int numberOfRetutnArrayElement) {
		lock();
		if (numberOfRetutnArrayElement < 0) {
			return new KyoroString[0];
		}
		KyoroString[] ret = new KyoroString[numberOfRetutnArrayElement];
		return (KyoroString[]) getLast(ret, numberOfRetutnArrayElement);
	}

	public synchronized KyoroString[] getLines(int start, int end) {
		lock();
		if (start > end) {
			return new KyoroString[0];
		}
		KyoroString[] ret = new KyoroString[end - start];
		return getElements(ret, start, end);
	}

	public synchronized KyoroString getLine(int i) {
		lock();
		return (KyoroString) super.get(i);
	}

	private int mCurrentColor = Color.parseColor("#ccc9f486");
	public void setCurrentLineColor(int color) {
		mCurrentColor = color;
	}

	protected void setColorPerLine(CharSequence line) {
	}

	private ArrayList<KyoroString> mCashForFiltering = new ArrayList<KyoroString>();
	@Override
	protected boolean filter(KyoroString t) {
		if(mFilter == null){
			return true;
		}
		if(t != null) {
			mCashForFiltering.add(t);
			if(t.includeLF()) {
				StringBuilder builder = new StringBuilder("");
				for(KyoroString d : mCashForFiltering){
					builder.append(d);
				}
				String i = builder.toString();
				Matcher m = mFilter.matcher(i);
				if(m.find()){
					for(KyoroString d: mCashForFiltering) {
						getDuplicatingList().add(d);
					}
				}
				mCashForFiltering.clear();
			}
		}

		// ever time return false;
		return false;
	}
	private int num = 0;
	@Override
	public synchronized void beginLock() {
		if(!locked){
			locked = true;
			cThread = Thread.currentThread();
			num++;
		}
	}

	@Override
	public synchronized void endLock() {
		if(locked&& cThread == Thread.currentThread()){
			num--;
			if(num <=0){
				locked = false;
				num =0;
				notifyAll();
			}
		}
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
