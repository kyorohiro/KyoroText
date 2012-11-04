package info.kyorohiro.helloworld.display.simple;


import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.FloatArrayBuilder;

public class SimpleGraphicUtil {
	public static void fillRect(SimpleGraphics graphics, int x, int y, int w, int h) {
		drawRect(graphics, SimpleGraphics.STYLE_FILL, x, y, w, h);
	}

	public static void drawRect(SimpleGraphics graphics, int x, int y, int w, int h) {
		drawRect(graphics, SimpleGraphics.STYLE_LINE, x, y, w, h);
	}
	
	public static void drawRect(SimpleGraphics graphics,int style, int x, int y, int w, int h) {
		int s = graphics.getStyle();
		graphics.startPath();
		graphics.setStyle(style);
		graphics.moveTo(x, y);
		graphics.lineTo(x, h+y);
		graphics.lineTo(x+w, h+y);
		graphics.lineTo(x+w, y);
		graphics.lineTo(x, y);
		graphics.endPath();
		graphics.setStyle(s);
	}

	private static FloatArrayBuilder mCash = new FloatArrayBuilder();
	public static void drawString(SimpleGraphics graphics, KyoroString text, int x, int y, FloatArrayBuilder cash) {
		SimpleFont font = graphics.getSimpleFont();
		int textSize = graphics.getTextSize();
		char[] buffer = text.getChars();
		int len = text.length();
		if(cash == null) {
			cash = mCash;
		}
		cash.setLength(len);
		float[] widths = cash.getAllBufferedMoji(); 
		int start = 0;
		int end = 0;
		int xPlus = 0;
		while(true) {
			end = getControlCode(buffer, len, start);
			graphics.drawText(buffer, start, end, x+xPlus, y);
			if(len<=end){
				return;
			}

			xPlus += font.getTextWidths(text, start, end, widths, textSize);
			xPlus += drawControlCode(graphics, buffer[end], x+xPlus, y, textSize);
			//
			start = end+1;
		}
	}
	private static int drawControlCode(SimpleGraphics graphics, char code, int x, int y, int textSize) {
		if(code == 9) {//tab
			//graphics.drawLine(x, y, x+textSize*4, y);
			SimpleGraphicUtil.drawRect(graphics, x, y, textSize*4, -textSize);
			return textSize*4;
		} else {
			SimpleGraphicUtil.drawRect(graphics, x, y, textSize, -textSize);
			//graphics.drawLine(x, y, x+textSize, y);
			return textSize;
		}
	}
	private static int getControlCode(char[] buffer, int len, int start ) {
		for(int i=start;i<len;i++) {
			if(buffer[i]<=31||buffer[i]==127){
				return i;
			}
		}
		return len;
	}
}
