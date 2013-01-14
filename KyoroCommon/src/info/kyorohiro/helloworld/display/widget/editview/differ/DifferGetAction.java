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
	private int mIndex = 0;
	private int mIndexFromBase = 0;
	private int mPrevEnd = 0;
	private CharSequence mRe = "";
	private boolean mIsDiffer = false;

	public boolean isDiffer() {
		return mIsDiffer;
	}



	public KyoroString get(Differ differ, LineViewBufferSpec spec, int index) {
		mIndex = index;
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
				return spec.get(mIndexFromBase);
			}
		} finally {
			//differ.debugPrint();
		}
	}

	@Override
	public void init() {
		mIndexFromBase = mIndex;
		mIsDiffer = false;
		mDiffLength = 0;
		mDeleteLength = 0;
	}

	private int mDiffLength = 0;
	private int mDeleteLength = 0;

	@Override
	public boolean check(Differ owner, int lineLocation, int __not_use__start, int __not_use__end, int index) {
		Line targetLine = owner.getLine(lineLocation);
		if (targetLine instanceof DeleteLine) {
			int start = index + targetLine.begin();
			int end = start;// + l.length();
			if (mIndex < start) {
				mIndexFromBase = mIndex - mDiffLength + mDeleteLength;
				return false;
			} else {
				mDeleteLength += targetLine.length();
				mIndexFromBase = mIndex - mDiffLength + mDeleteLength;
				// android.util.Log.v("text","la[1]="+mIndexFromBase+",s="+start+",e="+end+",i="+indexFromBase+",de="+mDeleteLength+",di="+mDiffLength);
				return true;
			}
		} else {
			int start = index + targetLine.begin();
			int end = start + targetLine.length();
			int diffLength = mDiffLength + end - start;
			try {
				if (start <= mIndex && mIndex < end) {
					mRe = targetLine.get(mIndex - start);
					if(!(mRe instanceof KyoroString)) {
						mRe = new KyoroString(mRe);
						targetLine.set(mIndex - start, mRe);
						((KyoroString)mRe).setColor(SimpleGraphicUtil.BLACK);
					}
					
					mIsDiffer = true;
					// android.util.Log.v("text","la[1]="+mIndexFromBase+",s="+start+",e="+end+",i="+indexFromBase+",de="+mDeleteLength+",di="+mDiffLength);
					return false;
				} else if (mIndex < start) {
					mIsDiffer = false;
					mIndexFromBase = mIndex - mDiffLength + mDeleteLength;
					// android.util.Log.v("text","la[2]="+mIndexFromBase+",s="+start+",e="+end+",i="+indexFromBase+",de="+mDeleteLength+",di="+mDiffLength);
					return false;
				} else {
					mIndexFromBase = mIndex - diffLength + mDeleteLength;
					// android.util.Log.v("text","la[3]="+mIndexFromBase+",s="+start+",e="+end+",i="+indexFromBase+",de="+mDeleteLength+",di="+mDiffLength);
					return true;
				}
			} finally {
				mDiffLength = diffLength;
			}

		}
	}
}
