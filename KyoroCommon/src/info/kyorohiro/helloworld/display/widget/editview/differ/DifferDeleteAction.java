package info.kyorohiro.helloworld.display.widget.editview.differ;

import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;

import java.util.LinkedList;

public class DifferDeleteAction extends CheckAction {

	public void delete(Differ differ, int index) {
//		android.util.Log.v("kiyo","#D#0#----000----");
		mTargetPatchedPosition = index;
		differ.checkAllSortedLine(this);
		differ.debugPrint();
	}

	@Override
	public void init() {
//		android.util.Log.v("kiyo","#D#0#----001----");
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

		try {
			// in
			if(mPrevPatchedPosition <= mTargetPatchedPosition && mTargetPatchedPosition < patchedPosition) {
				if(mTargetPatchedPosition < mPrevPatchedPosition+targetLine.begin()) {
					owner.addLine(lineLocation, new DeleteLine(mTargetPatchedPosition-mPrevPatchedPosition));
					targetLine.setStart(targetLine.begin()-(mTargetPatchedPosition-mPrevPatchedPosition+1));
				} else {
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
//			android.util.Log.v("kiyo","#D#0#----5----");
			ll.add(new DeleteLine(mTargetPatchedPosition-mPrevPatchedPosition));
		} else {
//			android.util.Log.v("kiyo","#D#0#----6----");
		}
	}

}