package info.kyorohiro.helloworld.display.simple;

import info.kyorohiro.helloworld.text.KyoroString;

public class EmptySimpleFont extends SimpleFont {

	private int getTextWidths(int textLen, int start, int end,
			float[] widths, float textSize) {
		if(end>textLen) {
			end = textLen;
		}
		int len = end-start;
		for(int i=0;i<len;i++){
			widths[i]=textSize;
		}
		return len;
	}

	@Override
	public int getTextWidths(KyoroString text, int start, int end,
			float[] widths, float textSize) {
		return getTextWidths(text.length(), 
				start, end, widths, textSize);
	}

	@Override
	public int getTextWidths(char[] buffer, int start, int end, float[] widths,
			float textSize) {
		return getTextWidths(buffer.length, 
				start, end, widths, textSize);
	}

}
