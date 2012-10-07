package info.kyorohiro.helloworld.display.widget.lineview.edit;

import java.util.LinkedList;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.CheckAction;
import info.kyorohiro.helloworld.display.widget.lineview.edit.Differ.Line;
import android.graphics.Color;

public class DifferGetAction extends CheckAction {
	private int mIndex = 0;
	private int mResult = 0;
	private CharSequence mRe = "";
	private boolean mIsDiffer = false;
	public void setIndex(int index) {
		mIndex = index;
	}
	public boolean isDiffer() {
		return mIsDiffer;
	}

	public LineViewData get(Differ differ, LineViewBufferSpec spec, int index) {
		setIndex(index);
		differ.checkAllSortedLine(this);
		try {
		if(isDiffer()) {
			return new LineViewData(mRe, Color.BLACK, LineViewData.INCLUDE_END_OF_LINE);
		} else {
			android.util.Log.v("text","ll="+mResult+","+mIndex);
			return spec.get(mResult);
		}
		}finally {
			differ.debugPrint();
		}
	}

	@Override
	public void init() {
		mResult = mIndex;
		mIsDiffer = false;
	}

	@Override
	public boolean check(LinkedList<Line> ll, int x, int start, int end, int indexFromBase) {
		boolean repeat = true;
		Line l = ll.get(x);
		if (!(l instanceof DeleteLine)&&start <= mIndex && mIndex < end) {
			repeat = false;
			mRe = l.get(mIndex - start);
			mIsDiffer = true;
			mResult = mIndex-indexFromBase;
			android.util.Log.v("text","la[1]="+mResult+",s="+start+",e="+end+",i="+indexFromBase);
		} else if (mIndex < start) {
			repeat = false;
			mIsDiffer = false;
			android.util.Log.v("text","la[2]="+mResult+",s="+start+",e="+end+",i="+indexFromBase);
		} else {
			mResult = mIndex-indexFromBase;
			android.util.Log.v("text","la[3]="+mResult+",s="+start+",e="+end+",i="+indexFromBase);
		}
		return repeat;
	}
}
