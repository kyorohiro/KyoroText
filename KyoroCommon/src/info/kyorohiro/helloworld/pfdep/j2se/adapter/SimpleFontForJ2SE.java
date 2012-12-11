package info.kyorohiro.helloworld.pfdep.j2se.adapter;

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
		return getTextWidths(text.getChars(), start, end, widths, textSize);
	}

	@Override
	public int getTextWidths(char[] buffer, int start, int end, float[] widths,
			float textSize) {
		float zoom = textSize/getFontSize();
//		System.out.println("Bw="+start+","+end);
		//mMetrics.
		for(int i=start;i<end;i++){
			float ret = 0;
			//if(i+1<end-start){
			//	ret = zoom*mMetrics.charsWidth(text.getChars(), i, 2)-
			//			mMetrics.charsWidth(text.getChars(), i+1, 1);	
			//} else {
			
				ret = zoom*mMetrics.charsWidth(buffer, i, 1);
			//}
			widths[i-start] = (int)ret;
		}
		normalizeWidth(buffer, start, end, widths, textSize);
//		for(int i=start;i<end;i++){
//				System.out.println("#w["+i+"]="+widths[i]+","+buffer[i]+","+zoom+","+getFontSize());
//		}
		return 100;
	}

}
