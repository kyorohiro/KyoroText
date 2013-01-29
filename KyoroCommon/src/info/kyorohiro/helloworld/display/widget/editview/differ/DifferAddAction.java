package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.util.LinkedList;


import info.kyorohiro.helloworld.display.widget.editview.differ.AddLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;

public class DifferAddAction implements CheckAction {
	private int mTargetPatchedPosition = 0;
	private CharSequence mLine = "";
	
	public void setIndex(int index) {
		mTargetPatchedPosition = index;
	}

	public void setLine(CharSequence line) {
		mLine = line;
	}

	@Override
	public void init() {
		mIsAdded = false;
		mPrevPatchedPosition = 0;
		mPrevUnpatchedPosition = 0;
	}

	@Override
	public void end(LinkedList<Line> ll) {
		if (!mIsAdded) {
			ll.add(new AddLine(mTargetPatchedPosition - mPrevPatchedPosition, mLine));
		}
	}

	public void add(Differ differ, int i, CharSequence line) {
		this.setIndex(i);
		this.setLine(line);
		differ.checkAllSortedLine(this);
		differ.debugPrint();
	}

	private int mPrevPatchedPosition = 0;
	private int mPrevUnpatchedPosition = 0;
	private boolean mIsAdded = false;

	@Override
	public boolean check(Differ owner, int lineLocation, int patchedPosition, int unpatchedPosition, int index) {
		//
		//
//		/*
		Line targetLine = owner.getLine(lineLocation);
		try {
			// in
//			if(mPrevPatchedPosition <= mTargetPatchedPosition && mTargetPatchedPosition < patchedPosition) {
			if(mPrevPatchedPosition <= mTargetPatchedPosition && mTargetPatchedPosition <= patchedPosition) {
				int start = mPrevPatchedPosition+targetLine.begin(); 
				if(mTargetPatchedPosition < start) {
					// new AddLine
					int n = mTargetPatchedPosition - mPrevPatchedPosition;
					owner.addLine(lineLocation, new AddLine(n, mLine));	
					targetLine.setStart(targetLine.begin()-n);
					// loop end
					mIsAdded = true;
					return false;
				} else {
					// add test in AddLine
					if(targetLine instanceof AddLine) {
						((AddLine)targetLine).insert(mTargetPatchedPosition-mPrevPatchedPosition-targetLine.begin(), mLine);
						mIsAdded = true;
						return false;
					} else {
						// 
						// dont thrugh this state 
						System.out.print("#Warnning# unexpected state.");
					}
				}
			}
			// out 
			// done in endLine
		} finally {
			mPrevPatchedPosition = patchedPosition;
			mPrevUnpatchedPosition = unpatchedPosition;
		}
		// expect next roop
		return true;

	}

}
