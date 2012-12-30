package info.kyorohiro.helloworld.display.widget.editview.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.MyCursor;
import info.kyorohiro.helloworld.text.KyoroString;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchTask implements Runnable {
	private EditableLineView mTargetView = null;
	private String mRegex = "";
	public SearchTask(EditableLineView targetView, String regex) {
		mRegex = regex;
		mTargetView = targetView;
	}

	@Override
	public void run() {
		MyCursor cursor = mTargetView.getLeft();
		int index = cursor.getCursorCol();
		int row = cursor.getCursorRow();
		EditableLineViewBuffer buffer = (EditableLineViewBuffer)mTargetView.getLineViewBuffer();
		Line l = new Line(mRegex);
		boolean f = true;
		while(index<buffer.getNumberOfStockedElement()) {
			KyoroString str = buffer.get(index);
			if(f) {
				l.add(str,row, str.length());
				f = false;
			} else {
				l.add(str, 0, str.length());
			}
			if(str.includeLF()) {
				if(l.find()) {
					mTargetView.getLeft().setCursorCol(l.getY()+index);
					mTargetView.getLeft().setCursorRow(l.getX()+(l.getY()==0?row:0));
					mTargetView.recenter();
					break;
				}
			}
			index++;
		}
	}

	//
	// KyoroText's Managed Text data dont't divide per CRLF
	// This class create group per CRLF. and find
	public static class Line {
		private StringBuffer mBuffer = new StringBuffer();
		private ArrayList<Integer> mLength = new ArrayList<Integer>(10);
		private Pattern mPattern = null;
		private int mX = 0;
		private int mY = 0;
		
		
		public Line(String regex) {
			mPattern = Pattern.compile(regex);
		}

		public void add(CharSequence str, int begin, int end) {
			mBuffer.append(str.subSequence(begin, end));
		}

		public void clear() {
			mLength.clear();
			mBuffer = new StringBuffer();
			mX = 0;
			mY = 0;
		}

		public boolean find() {
			Matcher mMatcher = mPattern.matcher(mBuffer.toString());
			boolean ret = mMatcher.find();
			if(ret) {
				int start = mMatcher.start();
				int end = mMatcher.end();
				int len = 0;
				int prev = 0;
				for(int i=0;i<mLength.size();i++) {
					len +=mLength.get(i);
					if(end>=len) {
						mY = i;
						mX = end-prev;
					}
					prev = len;
				}
			}
			return ret;
		}

		public int getX() {
			return mX;
		}

		public int getY() {
			return mY;
		}
	}
}
