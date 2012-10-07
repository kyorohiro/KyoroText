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
	private final DifferDeleteAction mDeleteAction = new DifferDeleteAction();

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
		deleteLine(i);
		addLine(i, line);
	}

	public synchronized void deleteLine(int i) {
		mDeleteAction.delete(this, i);
		mLength -= 1;
	}

	public static class DifferDeleteAction extends CheckAction {
		private int mIndex = 0;
		private int mPrevEnd = 0;
		private boolean mIsDeleted = false;

		public void delete(Differ differ, int index) {
			mIndex = index;
			differ.checkAllSortedLine(this);
		}

		@Override
		public void init() {
			mPrevEnd = 0;
			mIsDeleted = false;
		}

		@Override
		public void end(LinkedList<Line> ll) {
			if(!mIsDeleted) {
				ll.add( new DeleteLine(mIndex - mPrevEnd));
			}
		}

		@Override
		public boolean check(LinkedList<Line> ll, int x, int start, int end,
				int indexFromBase) {
			mPrevEnd = end;
			if (mIndex < start) {
				android.util.Log.v("kiyo", "_delete:1");
				Line l = ll.get(x);
				l.setStart(l.length()-(mIndex - mPrevEnd));
				ll.add(x, new DeleteLine(mIndex - mPrevEnd));
				mIsDeleted = true;
				return false;
			} else if (start <= mIndex && mIndex < end) {
				android.util.Log.v("kiyo", "_delete:2="+(mIndex - start));
				// ƒyƒ“ƒh
				Line l = ll.get(x);
				l.rm(mIndex - start);
				if (l.length() == 0) {
					ll.remove(x);
				}
				mIsDeleted = true;
				return false;
			} else {
				return true;
			}
		}
	}

	public void checkAllSortedLine(CheckAction action) {
		int len = mLine.size();
		int index = 0;
		int start = 0;
		int end = 0;
		int indexFromBase = 0;
		try {
			action.init();
			for (int x = 0; x < len; x++) {
				Line l = mLine.get(x);
				if (l instanceof DeleteLine) {
					//indexFromBase += l.length();
					start = index + l.begin();
					end = start;// + l.length();
					index += l.begin();// + l.length();
				} else {
					start = index + l.begin();
					end = start + l.length();
					index += l.begin() + l.length();
					//indexFromBase += end - start;
				}
				if (!action.check(mLine, x, start, end, indexFromBase)) {
					return;
				}
			}
		} finally {
			action.end(mLine);
		}
	}

	public static abstract class CheckAction {
		public void init() {
		};

		// if return false, when check action is end.
		public abstract boolean check(LinkedList<Line> l, int x, int start,
				int end, int indexFromBase);

		public void end(LinkedList<Line> ll) {
		};
	}

	interface Line {
		// public int consume();
		public int begin();

		public int length();

		public void setStart(int start);

		public void set(int index, CharSequence line);

		public void rm(int index);

		public CharSequence get(int i);

		public void insert(int index, CharSequence line);
	}

	public void debugPrint() {
		android.util.Log.v("ll", "" + mLine.size());
		int j = 0;
		for (Line l : mLine) {
			android.util.Log.v("ll", "" + l.length());
			for (int i = 0; i < l.length(); i++) {
				android.util.Log.v("ll", "[" + j + "][" + i + "]=" + l.get(i)+","+l.begin());
			}
			j++;
		}
	}

}
