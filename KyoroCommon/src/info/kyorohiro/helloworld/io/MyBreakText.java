package info.kyorohiro.helloworld.io;

import android.graphics.Paint;

//
//　このコードから、いらない機能を削除する必要がある。
//　--> LineViewのカーソル関連のコードを修正するタイミングが
//     よさげ
public class MyBreakText implements BreakText {
	private Paint mPaint = new Paint();
	private int mWidth = 400;

	public MyBreakText() {
		mPaint.setAntiAlias(true);
	}
	public int getWidth(){
		return mWidth;
	}

	@Override
	public void setTextSize(float textSize) {			
		mPaint.setTextSize(textSize);
	}

	@Override
	public float getTextSize() {			
		return mPaint.getTextSize();
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

	private Paint specialPaint = new Paint();
	@Override
	public int getTextWidths(CharSequence text, int start, int end,
			float[] widths, float textSize) {
		try {
			specialPaint.setAntiAlias(true);
			specialPaint.setTextSize(textSize);
			return specialPaint.getTextWidths(text, start, end, widths);
		}catch(Throwable t){
			//todo refactaring
			return 0;
		}
	}

}
