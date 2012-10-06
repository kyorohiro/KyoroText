package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;

import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Color;

public class Differ {
	private LinkedList<Line> mLine = new LinkedList<Line>();
	private int mLength = 0;

	public int length() {
		return mLength;
	}

	private final GetAction mGetAction = new GetAction();
	public synchronized LineViewData get(LineViewBufferSpec spec, int _index) {
		mGetAction.setIndex(_index);
		return mGetAction.get(this, spec);
	}

	public synchronized void addLine(int i, CharSequence line) {
		android.util.Log.v("ll", "aa i=" + i + ",+" + line);
		int len = mLine.size();
		int index = 0;
		int start = 0;
		int end = 0;
		boolean find = false;
		for (int x = 0; x < len; x++) {
			Line l = mLine.get(x);
			start = index + l.begin();
			end = start + l.length();
			android.util.Log.v("ll", "aa i=[0]" + start + "," + end + ","
					+ index);
			// 範囲内か隣接している場合
			// if (start <= index && index <= end) {
			if (start <= i && i <= end) {
				android.util.Log.v("ll", "aa i=[1]" + (index - start) + ",+"
						+ line);
				find = true;
				l.insert(i - start, line);
				// l.insert(index-start, line);
				break;
			}
			// 前に存在する場合
			// else if (index < start) {
			else if (i < start) {
				android.util.Log
						.v("ll", "aa i=[2]" + (i - index) + ",+" + line);
				mLine.add(x, new AddLine(i - index, line));
				// mLine.add(index, new AddLine(i-index,line));
				find = true;
				break;
			}
			index = end;
		}
		if (!find) {
			mLine.add(new AddLine(i - index, line));
		}
		// debug
		mLength++;
		debugPrint();
	}

	private void debugPrint() {
		android.util.Log.v("ll", "" + mLine.size());
		for (Line l : mLine) {
			android.util.Log.v("ll", "" + l.length());
			for (int i = 0; i < l.length(); i++) {
				android.util.Log.v("ll", "[" + i + "]=" + l.get(i));
			}
		}
	}

	public void checkAllSortedLine(CheckAction action) {
		int len = mLine.size();
		int index = 0;
		int start = 0;
		int end = 0;
		boolean find = false;
		try {
			action.init();
			for (int x = 0; x < len; x++) {
				Line l = mLine.get(x);
				start = index + l.begin();
				end = start + l.length();
				// -ココイガイは使いまわしたい-
				if (!action.check(l, start, end)) {
					return;
				}
				// --
				index = end;
			}
		} finally {
			action.end();
		}
	}

	public static abstract class CheckAction {
		public void init(){};
		// if return false,  when check action is end. 
		public abstract boolean check(Line l, int start, int end);
		public void end(){};
	}

	public static class AddLine implements Line {
		public int mOrifinalInsertIndex = 0;
		public ArrayList<CharSequence> mLines = new ArrayList<CharSequence>();

		public AddLine(int index, CharSequence line) {
			mOrifinalInsertIndex = index;
			mLines.add(line);
		}

		@Override
		public CharSequence get(int i) {
			return mLines.get(i);
		}

		@Override
		public int begin() {
			return mOrifinalInsertIndex;
		}

		@Override
		public int length() {
			return mLines.size();
		}

		@Override
		public void set(int index, CharSequence line) {
			mLines.set(index, line);
		}

		@Override
		public void insert(int index, CharSequence line) {
			mLines.add(index, line);
		}

		@Override
		public int consume() {
			return mOrifinalInsertIndex + length();
		}
	}

	interface Line {
		public int consume();
		public int begin();
		public int length();
		public void set(int index, CharSequence line);
		public CharSequence get(int i);
		public void insert(int index, CharSequence line);
	}

	public static class GetAction extends CheckAction {
		private int mIndex = 0;
		private int mResult = 0;
		private CharSequence mRe = "";
		private boolean mIsDiffer = false;
		public void setIndex(int index) {
			mIndex = index;
		}
		public boolean isDiffer() {
			return mIsDiffer;
		}

		public LineViewData get(Differ differ, LineViewBufferSpec spec) {
			differ.checkAllSortedLine(this);
			if(isDiffer()) {
				return new LineViewData(mRe, Color.BLACK, LineViewData.INCLUDE_END_OF_LINE);
			} else {
				android.util.Log.v("text","ll="+mResult+","+mIndex);
				return spec.get(mResult);
			}
		}

		@Override
		public void init() {
			mResult = mIndex;
			mIsDiffer = false;
		}
		@Override
		public boolean check(Line l, int start, int end) {
			boolean repeat = true;
			if (start <= mIndex && mIndex < end) {
				repeat = false;
				mRe = l.get(mIndex - start);
				mIsDiffer = true;
				mResult -= (end-start);
			} else if (mIndex < start) {
				repeat = false;
				mIsDiffer = false;
			} else {
				mResult -= (end-start);
			}
			android.util.Log.v("text","la="+mResult);
			return repeat;
		}
	}

}
