package info.kyorohiro.helloworld.logcat.logcat;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.util.CyclingListForAsyncDuplicate;
import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Color;
import android.graphics.Paint;

public class LogcatCyclingLineDataList extends CyclingListForAsyncDuplicate<LogcatLineData> {
	private Paint mPaint = null;
	private int mWidth = 1000;
	private Pattern mFilter = null;
	private Pattern mPatternForFontColorPerLine = 
		Pattern.compile(":[\\t\\s0-9\\-:.,]*[\\t\\s]([VDIWEFS]{1})/");

	public LogcatCyclingLineDataList(int listSize, int width, int textSize) {
		super(new CyclingList<LogcatLineData>(listSize),listSize);
		mWidth = width;
		mPaint = new Paint();
		mPaint.setTextSize(textSize);
	}

	private ArrayList<LogcatLineData> mCashForFiltering = new ArrayList<LogcatLineData>();
	@Override
	protected boolean filter(LogcatLineData t) {
		if(mFilter == null){
			return true;
		}
		if(t != null) {
			mCashForFiltering.add(t);
			if(t.getStatus() == LogcatLineData.INCLUDE_END_OF_LINE) {
				StringBuilder builder = new StringBuilder("");
				for(LogcatLineData d : mCashForFiltering){
					builder.append(d);
				}
				String i = builder.toString();
				Matcher m = mFilter.matcher(i);
				if(m.find()){
					for(LogcatLineData d: mCashForFiltering) {
						getDuplicatingList().add(d);
					}
				}
				mCashForFiltering.clear();
			}
		}

		// ever time return false;
		return false;
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

	public synchronized void addLinePerBreakText(String line) {
		setColorPerLine(line);

		if (line == null) {
			line = "";
		}
		int len = 0;
		while (true) {
			len = mPaint.breakText(line.toString(), true, mWidth, null);
			if (len == line.length()) {
				add(new LogcatLineData(line, mCurrentColor,
						LogcatLineData.INCLUDE_END_OF_LINE));
				break;
			} else {
				add(new LogcatLineData(line.substring(0, len), mCurrentColor,
						LogcatLineData.EXCLUDE_END_OF_LINE));
				line = line.substring(len, line.length());
			}
		}
	}

	public synchronized void addLine(String line) {
		LogcatLineData vLine = null;
		if (line == null) {
			vLine = new LogcatLineData("", 0,
					LogcatLineData.INCLUDE_END_OF_LINE);
		} else {
			vLine = new LogcatLineData(line, 0,
					LogcatLineData.INCLUDE_END_OF_LINE);
		}
		add(vLine);
	}

	public synchronized LogcatLineData[] getLastLines(
			int numberOfRetutnArrayElement) {
		if (numberOfRetutnArrayElement < 0) {
			return new LogcatLineData[0];
		}
		LogcatLineData[] ret = new LogcatLineData[numberOfRetutnArrayElement];
		return (LogcatLineData[]) getLast(ret, numberOfRetutnArrayElement);
	}

	public synchronized LogcatLineData[] getLines(int start, int end) {
		if (start > end) {
			return new LogcatLineData[0];
		}
		LogcatLineData[] ret = new LogcatLineData[end - start];
		return getElements(ret, start, end);
	}

	public LogcatLineData getLine(int i) {
		return (LogcatLineData) super.get(i);
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

}
