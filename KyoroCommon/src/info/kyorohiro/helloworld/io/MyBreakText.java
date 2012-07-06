package info.kyorohiro.helloworld.io;

import android.graphics.Paint;

public class MyBreakText implements BreakText {
	private Paint mPaint = new Paint();
	private int mWidth = 400;

	@Override
	public void setTextSize(float textSize) {			
		mPaint.setTextSize(textSize);
	}
	public void setBufferWidth(int w){
		mWidth = w;
	}
	@Override
	public int breakText(MyBuilder b, int width) {
		int len = mPaint.breakText(b.getAllBufferedMoji(), 0,
				b.getCurrentBufferedMojiSize(), width, null);
		return len;
	}

	@Override
	public int breakText(MyBuilder mBuffer) {
		return breakText(mBuffer, mWidth);
	}

	@Override
	public int getTextWidths(char[] text, int index, int count,
			float[] widths) {
		return mPaint.getTextWidths(text, index, count, widths);
	}

	@Override
	public int getTextWidths(CharSequence text, int start, int end,
			float[] widths) {
		return mPaint.getTextWidths(text, start, end, widths);
	}
	@Override
	public int breakText(CharSequence data, int index, int count, int width) {
//		int len = mPaint.breakText(data, 0, count, width, null);
		int len = mPaint.breakText(data, 0, count, false, width, null);
		return len;
	}

}
