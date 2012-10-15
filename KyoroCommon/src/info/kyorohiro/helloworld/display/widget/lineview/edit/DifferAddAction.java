package info.kyorohiro.helloworld.display.widget.lineview.edit;

import java.util.LinkedList;
import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.Line;

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
	public boolean check(LinkedList<Line> ll, int x, int start, int end, int indexFromBase) {
		Line l = ll.get(x);
		try {
			 if (mIndex < start) {
//					mLineList.add(x, new AddLine(mIndex - mPrevEnd, mLine));
					l.setStart(l.begin()-(mIndex - mPrevEnd));
					ll.add(x, new AddLine(mIndex - mPrevEnd, mLine));
					mFind = true;
					return false;
			} else 
			// 範囲内か隣接している場合
			if (!(l instanceof DeleteLine)&&start <= mIndex && mIndex <= end) {
				mFind = true;
				l.insert(mIndex - start, mLine);
				return false;
			}
			// 前に存在する場合
			return true;
		} finally {
			mPrevEnd = end;
		}
	}

}
