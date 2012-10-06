package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;

import java.util.ArrayList;
import java.util.LinkedList;

import android.graphics.Color;

public class Differ {
	private final DifferGetAction mGetAction = new DifferGetAction();
	private final DifferAddAction mAddAction = new DifferAddAction();
	private final DifferSetAction mSetAction = new DifferSetAction();

	private LinkedList<Line> mLine = new LinkedList<Line>();
	private int mLength = 0;

	public int length() {
		return mLength;
	}

	public synchronized LineViewData get(LineViewBufferSpec spec, int _index) {
		return mGetAction.get(this, spec, _index);
	}

	public synchronized void addLine(int i, CharSequence line) {
		mAddAction.add(this, i, line);
		mLength++;
	}

	public synchronized void setLine(int i, CharSequence line) {
		mSetAction.set(this, i, line);
	}
	public void checkAllSortedLine(CheckAction action) {
		int len = mLine.size();
		int index = 0;
		int start = 0;
		int end = 0;
		try {
			action.init();
			for (int x = 0; x < len; x++) {
				Line l = mLine.get(x);
				start = index + l.begin();
				end = start + l.length();
				if (!action.check(mLine, x, start, end)) {
					return;
				}
				index = end;
			}
		} finally {
			action.end(mLine);
		}
	}

	public static abstract class CheckAction {
		public void init(){};
		// if return false,  when check action is end. 
		public abstract boolean check(LinkedList<Line> l, int x, int start, int end);
		public void end(LinkedList<Line> ll){};
	}

	interface Line {
		public int consume();
		public int begin();
		public int length();
		public void set(int index, CharSequence line);
		public CharSequence get(int i);
		public void insert(int index, CharSequence line);
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

}
