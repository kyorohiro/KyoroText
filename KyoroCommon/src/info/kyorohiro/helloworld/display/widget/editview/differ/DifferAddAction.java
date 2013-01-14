package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.util.LinkedList;

import info.kyorohiro.helloworld.display.widget.editview.differ.AddLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;

public class DifferAddAction extends CheckAction {
	private int mPrevEnd = 0;
	private int mIndex = 0;
	private boolean mFind = false;
	private CharSequence mLine = "";
	private LinkedList<Line> mLineList = null;
	
	public void setIndex(int index) {
		mIndex = index;
	}

	public void setLine(CharSequence line) {
		mLine = line;
	}

	@Override
	public void init() {
		mFind = false;
		mPrevEnd = 0;
	}

	@Override
	public void end(LinkedList<Line> ll) {
		if (!mFind) {
			ll.add(new AddLine(mIndex - mPrevEnd, mLine));
		}
	}

	public void add(Differ differ, int i, CharSequence line) {
		this.setIndex(i);
		this.setLine(line);
		differ.checkAllSortedLine(this);
		differ.debugPrint();
	}

	@Override
	public boolean check(Differ owner, int x, int __not_use__start, int __not_use__end, int index) {
		Line l = owner.getLine(x);
		int start  = 0;
		int end = 0;
		try{
			if (l instanceof DeleteLine) {
				start = index + l.begin();
				end = start;// + l.length();
			} else {
				start = index + l.begin();
				end = start + l.length();
			}
			 if (mIndex < start) {
//					mLineList.add(x, new AddLine(mIndex - mPrevEnd, mLine));
					l.setStart(l.begin()-(mIndex - mPrevEnd));
					owner.addLine(x, new AddLine(mIndex - mPrevEnd, mLine));
					mFind = true;
					return false;
			} else 
			// �͈͓����אڂ��Ă���ꍇ
			if (!(l instanceof DeleteLine)&&start <= mIndex && mIndex <= end) {
				mFind = true;
				l.insert(mIndex - start, mLine);
				return false;
			}
			// �O�ɑ��݂���ꍇ
			return true;
		} finally {
			mPrevEnd = end;
		}
	}

}
