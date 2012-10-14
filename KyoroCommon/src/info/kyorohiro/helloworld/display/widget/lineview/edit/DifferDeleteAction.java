package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.Line;

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
	//	if(ll.get(x) instanceof DeleteLine) {
	//		return true;
	//	}
		if (mIndex < start) {
			android.util.Log.v("kiyo", "_delete:1=");
			Line l = ll.get(x);
			//l.setStart(l.length()-(mIndex - mPrevEnd));
			ll.add(x, new DeleteLine(mIndex - mPrevEnd));
			l.setStart(l.begin()-(mIndex - mPrevEnd+1));

//	if(x+1<ll.size()){
//				Line l1 = ll.get(x+1);
//				l1.setStart(l1.begin()-l.begin());
//		l.setStart(l.begin()-(mIndex - mPrevEnd));
//	}
			
			mIsDeleted = true;
			return false;
		}
		else if (start <= mIndex && mIndex < end) {
			android.util.Log.v("kiyo", "_delete:2=");
			// ƒyƒ“ƒh
			Line l = ll.get(x);
//			if(l instanceof DeleteLine) {
//				android.util.Log.v("kiyo", "_delete:2-1=");
//				l.set(l.length(), null);
//				return false;				
//			} else {
				android.util.Log.v("kiyo", "_delete:2-2=");
				l.rm(mIndex - start);
				if (l.length() == 0) {
					ll.remove(x);
				}
				mIsDeleted = true;
				return false;
//			}
		} else {
			return true;
		}
		}finally{
			mPrevEnd = end;

		}
	}
}