package info.kyorohiro.helloworld.display.widget.editview.differ;

import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;

import java.util.LinkedList;

public class DifferDeleteAction extends CheckAction {
	private int mIndex = 0;
	private int mPrevEnd = 0;
	private boolean mIsDeleted = false;

	public void delete(Differ differ, int index) {
		mIndex = index;
		differ.checkAllSortedLine(this);
		differ.debugPrint();
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
			int _indexFromBase) {
		try{
			Line l = ll.get(x);
			if (mIndex < start) {
				if(0==l.begin()-(mIndex - mPrevEnd+1)&& l instanceof DeleteLine) {
					l.setStart(mIndex - mPrevEnd);
					l.set(l.length(), "");
				} else {
					ll.add(x, new DeleteLine(mIndex - mPrevEnd));
					l.setStart(l.begin()-(mIndex - mPrevEnd+1));
				}
				mIsDeleted = true;
				return false;
			}
			else if (start <= mIndex && mIndex < end) {
				l.rm(mIndex - start);
				if (l.length() == 0) {
					ll.remove(x);
				}
				mIsDeleted = true;
				return false;
			} else {
				return true;
			}
		}finally{
			mPrevEnd = end;

		}
	}
}