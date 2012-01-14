package info.kyorohiro.helloworld.logcat.util;

import info.kyorohiro.helloworld.util.CyclingList;
import android.graphics.Paint;

public class CyclingStringList extends CyclingList<String>{
	private Paint mPaint = null;
	private int mWidth = 1000;

	public CyclingStringList(int listSize,int width, int textSize) {
		super(listSize);
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

	public synchronized void addLinePerBreakText(String line) {
		if (line == null) {
			line = "";
		}
		//
		// todo Ç±Ç±Ç…â¸çsã@î\Çí«â¡Ç∑ÇÈó\íË
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
		add(line);
	}

	public synchronized String[] getLastLines(int numberOfRetutnArrayElement) {
		if(numberOfRetutnArrayElement < 0){
			return new String[0];
		}
		String[] ret = new String[numberOfRetutnArrayElement];
		return (String[])getLast(ret, numberOfRetutnArrayElement);
	}

	public synchronized String[] getLines(int start, int end) {
		if(start > end){
			return new String[0];
		}
		String[] ret = new String[end-start];
		return getElements(ret, start, end);
	}

	public String getLine(int i) {
		return (String)super.get(i);
	}

}
