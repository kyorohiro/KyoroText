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
	public boolean check(Differ owner, int lineLocation, int patchedPosition, int unpatchedPosition, int index) {
		//android.util.Log.v("kiyo","#=ZZZ1=="+x+","+start+","+end+","+_indexFromBase+","+mIndex);
		Line l = owner.getLine(lineLocation);
		int start  = 0;
		int end = 0;
		try{
			if (l instanceof DeleteLine) {
				//indexFromBase += l.length();
				start = index + l.begin();
				end = start;// + l.length();
			} else {
				start = index + l.begin();
				end = start + l.length();
			}
		//	start = index + l.begin();
		//	end = start;// + l.length();

			if (mIndex < start) {
			//	android.util.Log.v("kiyo","#=ZZZ2==");
				if(0==l.begin()-(mIndex - mPrevEnd+1)&& l instanceof DeleteLine) {
				//	android.util.Log.v("kiyo","#=ZZZ3==");
					l.setStart(mIndex - mPrevEnd);
					l.set(l.length(), "");
				} else {
					//android.util.Log.v("kiyo","#=ZZZ4==");
					owner.addLine(lineLocation, new DeleteLine(mIndex - mPrevEnd));
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
					if(lineLocation+1<owner.numOfLine()){
						owner.getLine(lineLocation+1).setStart(owner.getLine(lineLocation+1).begin()+tmp);
					//	android.util.Log.v("kiyo","#=ZZZ6=="+tmp+","+ll.get(x+1).begin());
					} else {
						//android.util.Log.v("kiyo","#=ZZZ6=="+tmp);
					}
					owner.removeLine(lineLocation);
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