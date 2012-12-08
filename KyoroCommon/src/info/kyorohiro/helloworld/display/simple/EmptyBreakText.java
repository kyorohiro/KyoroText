package info.kyorohiro.helloworld.display.simple;

import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CharArrayBuilder;

public class EmptyBreakText extends BreakText {

	public EmptyBreakText(SimpleFont font, int width) {
		super(font, width);
		//android.util.Log.v("test","#width="+width);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int breakText(CharArrayBuilder mBuffer) {
		return breakText(this, mBuffer.getAllBufferedMoji(), 0, mBuffer.getCurrentBufferedMojiSize(), getWidth());
	}

	@Override
	public int getTextWidths(KyoroString text, int start, int end,
			float[] widths, float textSize) {
		return getSimpleFont().getTextWidths(text, start, end, widths, textSize);
	}

	@Override
	public int getTextWidths(char[] buffer, int start, int end, float[] widths,
			float textSize) {
		return getSimpleFont().getTextWidths(buffer, start, end, widths, textSize);
	}

}
