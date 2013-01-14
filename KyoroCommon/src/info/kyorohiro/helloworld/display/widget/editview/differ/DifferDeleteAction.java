package info.kyorohiro.helloworld.display.widget.editview.differ;

import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;

import java.util.LinkedList;

public class DifferDeleteAction extends CheckAction {
	private int mPrevEnd = 0;
	private boolean mIsDeleted = false;

	public void delete(Differ differ, int index) {
		mTargetPatchedPosition = index;
		differ.checkAllSortedLine(this);
		differ.debugPrint();
	}

	@Override
	public void init() {
		mPrevEnd = 0;
		mIsDeleted = false;
		mTargetUnpatchedPosition = mTargetPatchedPosition;
		mPrevPatchedPosition = 0;
		mPrevUnpatchedPosition = 0;
	}

	private int mTargetPatchedPosition = 0;
	private int mTargetUnpatchedPosition = 0;
	private int mPrevPatchedPosition = 0;
	private int mPrevUnpatchedPosition = 0;

	@Override
	public boolean check(Differ owner, int lineLocation, int patchedPosition, int unpatchedPosition, int index) {
		boolean ret = true;
		try {
			// in
			if(mPrevPatchedPosition <= mTargetPatchedPosition && mTargetPatchedPosition < patchedPosition) {
				owner.addLine(lineLocation, new DeleteLine(mTargetPatchedPosition-mPrevPatchedPosition));
				ret = false;
			}
			// out before
			else if(mTargetPatchedPosition<mPrevPatchedPosition){
				ret = false;
			}
		} finally {
			mPrevPatchedPosition = patchedPosition;
			mPrevUnpatchedPosition = unpatchedPosition;
		}
		return ret;
	}



	/*	
	@Override
	public void end(LinkedList<Line> ll) {
		if(!mIsDeleted) {
			ll.add( new DeleteLine(mIndex - mPrevEnd));
		}
	}

	@Override
	public boolean check(Differ owner, int lineLocation, int patchedPosition, int unpatchedPosition, int index) {
		Line targetLine = owner.getLine(lineLocation);
		int start  = 0;
		int end = 0;
		try{
			if (targetLine instanceof DeleteLine) {
				//indexFromBase += l.length();
				start = index + targetLine.begin();
				end = start;// + l.length();
			} else {
				start = index + targetLine.begin();
				end = start + targetLine.length();
			}

			// 
			// out before
			//
			if (mIndex < start) {
			//	android.util.Log.v("kiyo","#=ZZZ2==");
				if(0==targetLine.begin()-(mIndex - mPrevEnd+1)&& targetLine instanceof DeleteLine) {
				//	android.util.Log.v("kiyo","#=ZZZ3==");
					targetLine.setStart(mIndex - mPrevEnd);
					targetLine.set(targetLine.length(), "");
				} else {
					//android.util.Log.v("kiyo","#=ZZZ4==");
					owner.addLine(lineLocation, new DeleteLine(mIndex - mPrevEnd));
					targetLine.setStart(targetLine.begin()-(mIndex - mPrevEnd+1));
				}
				mIsDeleted = true;
				return false;
			}
			//
			// in
			//
			else if (start <= mIndex && mIndex < end) {
				//android.util.Log.v("kiyo","#=ZZZ5==");

				targetLine.rm(mIndex - start);
				if (targetLine.length() == 0) {
					int tmp = targetLine.begin();
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
			}
			//
			// out
			//
			else {
				//android.util.Log.v("kiyo","#=ZZZ7==");
				return true;
			}
		}finally{
			mPrevEnd = end;
		}
	}
//*/
}