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
		//android.util.Log.v("kiyo","#=ZZZ1=="+x+","+start+","+end+","+_indexFromBase+","+mIndex);
		try{
			Line l = ll.get(x);
			if (mIndex < start) {
			//	android.util.Log.v("kiyo","#=ZZZ2==");
				if(0==l.begin()-(mIndex - mPrevEnd+1)&& l instanceof DeleteLine) {
				//	android.util.Log.v("kiyo","#=ZZZ3==");
					l.setStart(mIndex - mPrevEnd);
					l.set(l.length(), "");
				} else {
					//android.util.Log.v("kiyo","#=ZZZ4==");
					ll.add(x, new DeleteLine(mIndex - mPrevEnd));
					l.setStart(l.begin()-(mIndex - mPrevEnd+1));
				}
				mIsDeleted = true;
				return false;
			}
			else if (start <= mIndex && mIndex < end) {
				//android.util.Log.v("kiyo","#=ZZZ5==");

				l.rm(mIndex - start);
				if (l.length() == 0) {
					int tmp = l.begin();
					if(x+1<ll.size()){
						ll.get(x+1).setStart(ll.get(x+1).begin()+tmp);
					//	android.util.Log.v("kiyo","#=ZZZ6=="+tmp+","+ll.get(x+1).begin());
					} else {
						//android.util.Log.v("kiyo","#=ZZZ6=="+tmp);
					}
					ll.remove(x);
				}
				mIsDeleted = true;
				return false;
			} else {
				//android.util.Log.v("kiyo","#=ZZZ7==");
				return true;
			}
		}finally{
			mPrevEnd = end;

		}
	}
}