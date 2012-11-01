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
//	public abstract int breakText(CharSequence data, int index, int count, int width);
	public abstract int getTextWidths(KyoroString text, int start, int end, float[] widths, float textSize);

	private static FloatArrayBuilder mWidths = new FloatArrayBuilder();
	//
	// todo
	public static synchronized int breakText(BreakText breaktext, KyoroString text, int index, int count) {
		int width = breaktext.getWidth();
		float s = breaktext.getTextSize();
		mWidths.setLength(count);
		float[] ws = mWidths.getAllBufferedMoji();
		breaktext.getTextWidths(text, index, index+count, ws, s);
		float l=0;
		for(int i=0;i<ws.length;i++) {
			l+=ws[i];
			if(l>=width){
				return  (i<=0?0:i-1);
			}
		}
		return ws.length;
	}
}
