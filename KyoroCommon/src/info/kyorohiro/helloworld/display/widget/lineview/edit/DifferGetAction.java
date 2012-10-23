package info.kyorohiro.helloworld.display.widget.lineview.edit;

import java.util.LinkedList;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.Line;
import info.kyorohiro.helloworld.text.KyoroString;
import android.graphics.Color;

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
				return new KyoroString(mRe, Color.BLACK);
			} else {
				// android.util.Log.i("text","lll="+mIndexFromBase+","+mPrevEnd+","+mIndex+","+mDiffLength+","+
				// mDeleteLength);
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
	public boolean check(LinkedList<Line> ll, int x, int start, int end,
			int indexFromBase) {
		Line l = ll.get(x);
		if (l instanceof DeleteLine) {
			if (mIndex < start) {
				mIndexFromBase = mIndex - mDiffLength + mDeleteLength;
				return false;
			} else {
				mDeleteLength += l.length();
				mIndexFromBase = mIndex - mDiffLength + mDeleteLength;
				// android.util.Log.v("text","la[1]="+mIndexFromBase+",s="+start+",e="+end+",i="+indexFromBase+",de="+mDeleteLength+",di="+mDiffLength);
				return true;
			}
		} else {
			int diffLength = mDiffLength + end - start;
			try {
				if (start <= mIndex && mIndex < end) {
					mRe = l.get(mIndex - start);
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
