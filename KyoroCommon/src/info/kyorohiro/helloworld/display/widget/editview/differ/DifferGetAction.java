package info.kyorohiro.helloworld.display.widget.editview.differ;

import java.util.LinkedList;

import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ.Line;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;

public class DifferGetAction extends CheckAction {
	private int mTargetPatchedPosition = 0;
	private int mTargetUnpatchedPosition = 0;
	private int mPrevEnd = 0;
	private CharSequence mRe = "";
	private boolean mIsDiffer = false;

	public boolean isDiffer() {
		return mIsDiffer;
	}



	public KyoroString get(Differ differ, LineViewBufferSpec spec, int index) {
		mTargetPatchedPosition = index;
		differ.checkAllSortedLine(this);
		try {
			if (isDiffer()) {
				//android.util.Log.v("kiyo","---A="+mRe);
				if(mRe instanceof KyoroString) {
//					android.util.Log.v("kiyo","---A="+mRe);
//					android.util.Log.v("kiyo","---A="+((KyoroString)mRe).getType());
					return (KyoroString)mRe;	
				} else {
//					android.util.Log.v("kiyo","---B="+mRe);
					return new KyoroString(mRe, SimpleGraphicUtil.BLACK);
				}
			} else {
				// android.util.Log.i("text","lll="+mIndexFromBase+","+mPrevEnd+","+mIndex+","+mDiffLength+","+
				// mDeleteLength);
				//android.util.Log.v("kiyo","---B=");
				return spec.get(mTargetUnpatchedPosition);
			}
		} finally {
			//differ.debugPrint();
		}
	}

	@Override
	public void init() {
		mTargetUnpatchedPosition = mTargetPatchedPosition;
		//mTargetUnpatchedPosition = 0;
		mPrevPatchedPosition = 0;
		mPrevUnpatchedPosition = 0;
		mIsDiffer = false;
		mDiffLength = 0;
		mDeleteLength = 0;
	}

	private int mDiffLength = 0;
	private int mDeleteLength = 0;

	private int mPrevPatchedPosition = 0;
	private int mPrevUnpatchedPosition = 0;

	@Override
	public boolean check(Differ owner, int lineLocation, int patchedPosition, int unpatchedPosition, int index) {
		Line targetLine = owner.getLine(lineLocation);
		
		//android.util.Log.v("kiyo","#A#"+lineLocation+","+ patchedPosition+","+ unpatchedPosition);
		//android.util.Log.v("kiyo","#B#"+mTargetPatchedPosition+","+ mTargetUnpatchedPosition);
		//android.util.Log.v("kiyo","#C#"+mPrevPatchedPosition+","+ mPrevUnpatchedPosition);
		boolean ret = true;
		try {
			//
			// in 
			// 
			if(mPrevPatchedPosition <= mTargetPatchedPosition&& mTargetPatchedPosition < patchedPosition) {
				//mTargetPatchedPosition = mPrevPatchedPosition+mTargetPatchedPosition-mPrevPatchedPosition; 
				if((mPrevPatchedPosition+targetLine.begin())<=mTargetPatchedPosition){
					android.util.Log.v("kiyo","#00#"+"("+mTargetPatchedPosition+"-"+mPrevPatchedPosition+")"+"-"+targetLine.begin());
					mRe = targetLine.get((mTargetPatchedPosition-mPrevPatchedPosition)-targetLine.begin());
					mIsDiffer =true;
					//android.util.Log.v("kiyo","#01#"+mRe);
				} else {
					mTargetUnpatchedPosition = mPrevUnpatchedPosition+mTargetPatchedPosition-mPrevPatchedPosition;					
					//android.util.Log.v("kiyo","#1#"+mTargetUnpatchedPosition);
				}
				ret = false;
			} 
			//
			// out before
			//
			else if(mTargetPatchedPosition < mPrevPatchedPosition) {
				mTargetUnpatchedPosition = mPrevUnpatchedPosition +  mPrevPatchedPosition- mTargetPatchedPosition;
//				android.util.Log.v("kiyo","#2#"+mTargetUnpatchedPosition);
				ret = false;;
			}
			//
			// out after
			//
			else {
				mTargetUnpatchedPosition = unpatchedPosition + mTargetPatchedPosition - patchedPosition;
//				android.util.Log.v("kiyo","#3#"+mTargetUnpatchedPosition);
				ret = true;
			}
		} finally {
			mPrevPatchedPosition = patchedPosition;
			mPrevUnpatchedPosition = unpatchedPosition;
		}
		return ret;

	}
}
