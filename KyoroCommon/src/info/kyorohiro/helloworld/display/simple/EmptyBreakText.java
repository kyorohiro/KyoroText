package info.kyorohiro.helloworld.display.simple;

import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CharArrayBuilder;

public class EmptyBreakText extends BreakText {

	public EmptyBreakText(SimpleFont font, int width) {
		super(font, width);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int breakText(CharArrayBuilder mBuffer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTextWidths(KyoroString text, int start, int end,
			float[] widths, float textSize) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTextWidths(char[] buffer, int start, int end, float[] widths,
			float textSize) {
		// TODO Auto-generated method stub
		return 0;
	}

}
