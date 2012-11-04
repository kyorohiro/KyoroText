package info.kyorohiro.helloworld.display.simple;


import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.FloatArrayBuilder;

public class SimpleGraphicUtil {
	public static void fillRect(SimpleGraphics graphics, int x, int y, int w, int h) {
		drawRect(graphics, SimpleGraphics.STYLE_FILL, x, y, w, h);
	}

	public static void drawRect(SimpleGraphics graphics, int x, int y, int w, int h) {
		drawRect(graphics, SimpleGraphics.STYLE_STROKE, x, y, w, h);
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
			end = graphics.getSimpleFont().getControlCode(buffer, len, start);
			graphics.drawText(buffer, start, end, x+xPlus, y);
			if(len<=end){
				return;
			}

			int t = font.getTextWidths(text, start, end, widths, textSize);
			for(int i=0;i<t;i++){
				xPlus +=widths[i];
			}
			xPlus +=// widths[end];
			drawControlCode(graphics, buffer[end], x+xPlus, y, textSize);
			//
			start = end+1;
		}
	}
	private static int drawControlCode(SimpleGraphics graphics, char code, int x, int y, int textSize) {
		int size = graphics.getSimpleFont().lengthOfControlCode(code, textSize);
		if(size != 0) {
			SimpleGraphicUtil.drawRect(graphics, x, y, size, -textSize);
		}
		return size;
	}
}
