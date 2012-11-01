package info.kyorohiro.helloworld.io;

import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CharArrayBuilder;
import info.kyorohiro.helloworld.util.FloatArrayBuilder;

//todo android dependent now
public abstract class BreakText implements SimpleTextDecoderBreakText {
	public abstract int getWidth();
	public abstract void setTextSize(float textSize);
	public abstract float getTextSize();
	public abstract int breakText(CharArrayBuilder mBuffer);
	public abstract int breakText(CharSequence data, int index, int count, int width);
	public abstract int getTextWidths(KyoroString text, int start, int end, float[] widths, float textSize);

	private static FloatArrayBuilder mWidths = new FloatArrayBuilder();
	public static synchronized int breakText(BreakText breaktext, KyoroString text, int index, int count, int width) {
		int w = breaktext.getWidth();
		float s = breaktext.getTextSize();
		mWidths.setLength(count);
		breaktext.getTextWidths(text, index, index+count, mWidths.getAllBufferedMoji(), s);
		return 0;
	}
}
