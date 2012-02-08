package info.kyorohiro.helloworld.display.widget;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.display.widget.FlowingLineDatam;
import info.kyorohiro.helloworld.util.CyclingListForAsyncDuplicate;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;
import android.graphics.Paint;

public class FlowingLineData extends CyclingListForAsyncDuplicate<FlowingLineDatam> {
	private Paint mPaint = null;
	private int mWidth = 1000;
	private Pattern mFilter = null;
	private Pattern mPatternForFontColorPerLine = 
		Pattern.compile(":[\\t\\s0-9\\-:.,]*[\\t\\s]([VDIWEFS]{1})/");

	public FlowingLineData(int listSize, int width, int textSize) {
		super(new CyclingList<FlowingLineDatam>(listSize),listSize);
		mWidth = width;
		mPaint = new Paint();
		mPaint.setTextSize(textSize);
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

	public synchronized void addLineToHead(String line) {
		this.head(new FlowingLineDatam(line, mCurrentColor,
						FlowingLineDatam.INCLUDE_END_OF_LINE));
	}

	public synchronized void addLinePerBreakText(String line) {
		setColorPerLine(line);

		if (line == null) {
			line = "";
		}
		int len = 0;
		while (true) {
			len = mPaint.breakText(line.toString(), true, mWidth, null);
			if (len == line.length()) {
				add(new FlowingLineDatam(line, mCurrentColor,
						FlowingLineDatam.INCLUDE_END_OF_LINE));
				break;
			} else {
				add(new FlowingLineDatam(line.substring(0, len), mCurrentColor,
						FlowingLineDatam.EXCLUDE_END_OF_LINE));
				line = line.substring(len, line.length());
			}
		}
	}

	public synchronized FlowingLineDatam[] getLastLines(
			int numberOfRetutnArrayElement) {
		if (numberOfRetutnArrayElement < 0) {
			return new FlowingLineDatam[0];
		}
		FlowingLineDatam[] ret = new FlowingLineDatam[numberOfRetutnArrayElement];
		return (FlowingLineDatam[]) getLast(ret, numberOfRetutnArrayElement);
	}

	public synchronized FlowingLineDatam[] getLines(int start, int end) {
		if (start > end) {
			return new FlowingLineDatam[0];
		}
		FlowingLineDatam[] ret = new FlowingLineDatam[end - start];
		return getElements(ret, start, end);
	}

	public FlowingLineDatam getLine(int i) {
		return (FlowingLineDatam) super.get(i);
	}

	private int mCurrentColor = Color.parseColor("#ccc9f486");
	private void setColorPerLine(String line) {
		try {
			Matcher m = mPatternForFontColorPerLine.matcher(line);
			if (m == null) {
				return;
			}
			if (m.find()) {
				if ("D".equals(m.group(1))) {
					mCurrentColor = Color.parseColor("#cc86c9f4");
				} else if ("I".equals(m.group(1))) {
					mCurrentColor = Color.parseColor("#cc86f4c9");
				} else if ("V".equals(m.group(1))) {
					mCurrentColor = Color.parseColor("#ccc9f486");
				} else if ("W".equals(m.group(1))) {
					mCurrentColor = Color.parseColor("#ccffff00");
				} else if ("E".equals(m.group(1))) {
					mCurrentColor = Color.parseColor("#ccff2222");
				} else if ("F".equals(m.group(1))) {
					mCurrentColor = Color.parseColor("#ccff2222");
				} else if ("S".equals(m.group(1))) {
					mCurrentColor = Color.parseColor("#ccff2222");
				}
			}
		} catch (Throwable e) {

		}
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
