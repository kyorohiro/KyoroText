package info.kyorohiro.helloworld.io;

import android.graphics.Paint;

//todo android dependent now
public interface BreakText {
	public void setTextSize(float textSize);
	public int breakText(MyBuilder mBuffer);
	public int breakText(MyBuilder mBuffer, int width);
	public int breakText(CharSequence data, int index, int count, int width);
	public int getTextWidths(char[] text, int index, int count, float[] widths);
	public int getTextWidths(CharSequence text, int start, int end, float[] widths);
}
