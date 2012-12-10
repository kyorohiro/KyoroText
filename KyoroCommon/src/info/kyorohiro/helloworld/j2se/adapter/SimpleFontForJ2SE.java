package info.kyorohiro.helloworld.j2se.adapter;

import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.text.StyledEditorKit.FontFamilyAction;

import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.text.KyoroString;

public class SimpleFontForJ2SE extends SimpleFont {

	private Font mFont = null;
	private FontMetrics mMetrics = null;
	public SimpleFontForJ2SE(Font font, FontMetrics metrics) {
		mFont = font;
		mMetrics = metrics;
	}

	//public Font getFont() {
	//	return mFont;
	//}

	@Override
	public int getTextWidths(KyoroString text, int start, int end,
			float[] widths, float textSize){
//		System.out.println("Bw=");
		int ret = mMetrics.charsWidth(text.getChars(), start, end-start);
		normalizeWidth(text.getChars(), start, end, widths, textSize);
		return ret;
	}

	@Override
	public int getTextWidths(char[] buffer, int start, int end, float[] widths,
			float textSize) {
		int ret = mMetrics.charsWidth(buffer, start, end-start);
		normalizeWidth(buffer, start, end, widths, textSize);
		return ret;
	}

}
