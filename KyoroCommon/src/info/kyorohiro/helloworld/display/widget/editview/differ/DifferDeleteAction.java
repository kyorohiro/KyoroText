package info.kyorohiro.helloworld.display.widget.editview.differ;

import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;

import java.util.LinkedList;

public class DifferDeleteAction extends CheckAction {
	//private int mPrevEnd = 0;
	//private boolean mIsDeleted = false;

	public void delete(Differ differ, int index) {
		android.util.Log.v("kiyo","#D#0#----000----");
		mTargetPatchedPosition = index;
		differ.checkAllSortedLine(this);
		differ.debugPrint();
	}

	@Override
	public void init() {
		android.util.Log.v("kiyo","#D#0#----001----");
		//mPrevEnd = 0;
		mIsDelete = true;//if non exist line object
		mTargetUnpatchedPosition = mTargetPatchedPosition;
		mPrevPatchedPosition = 0;
		mPrevUnpatchedPosition = 0;
	}

	private int mTargetPatchedPosition = 0;
	private int mTargetUnpatchedPosition = 0;
	private int mPrevPatchedPosition = 0;
	private int mPrevUnpatchedPosition = 0;
	private boolean mIsDelete =false;

	@Override
	public boolean check(Differ owner, int lineLocation, int patchedPosition, int unpatchedPosition, int index) {
		boolean ret = true;
		Line targetLine = owner.getLine(lineLocation);
		android.util.Log.v("kiyo","#D#A#"+lineLocation+","+ patchedPosition+","+ unpatchedPosition);
		android.util.Log.v("kiyo","#D#B#"+mTargetPatchedPosition+","+ mTargetUnpatchedPosition);
		android.util.Log.v("kiyo","#D#C#"+mPrevPatchedPosition+","+ mPrevUnpatchedPosition);

		try {
			// in
			if(mPrevPatchedPosition <= mTargetPatchedPosition && mTargetPatchedPosition < patchedPosition) {
				android.util.Log.v("kiyo","#D#0#"+mTargetPatchedPosition+"-"+mPrevPatchedPosition);
				if(mTargetPatchedPosition < mPrevPatchedPosition+targetLine.begin()) {
					android.util.Log.v("kiyo","#D#0#----1----"+mTargetPatchedPosition +"<"+ mPrevPatchedPosition+"+"+targetLine.begin());
					owner.addLine(lineLocation, new DeleteLine(mTargetPatchedPosition-mPrevPatchedPosition));
					targetLine.setStart(targetLine.begin()-(mTargetPatchedPosition-mPrevPatchedPosition+1));
					//
					//
				} else {
					//todo not test
					android.util.Log.v("kiyo","#D#0#----2----");
					targetLine.rm(mTargetPatchedPosition-mPrevPatchedPosition-targetLine.begin());
					if(targetLine.length()==0){
						owner.removeLine(lineLocation);
					}
				}
				ret = false;
			}
		} finally {
			mPrevPatchedPosition = patchedPosition;
			mPrevUnpatchedPosition = unpatchedPosition;
		}
		mIsDelete = ret;
		return ret;
	}


	@Override
	public void end(LinkedList<Line> ll) {
		//todo not test
		if(mIsDelete){
			android.util.Log.v("kiyo","#D#0#----5----");
			ll.add(new DeleteLine(mTargetPatchedPosition-mPrevPatchedPosition));
		} else {
			android.util.Log.v("kiyo","#D#0#----6----");
		}
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