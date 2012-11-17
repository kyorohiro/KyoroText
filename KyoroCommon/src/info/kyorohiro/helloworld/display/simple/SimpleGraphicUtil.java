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
		graphics.setStrokeWidth(1);
		graphics.setStyle(style);
		graphics.startPath();
		graphics.moveTo(x, y);
		graphics.lineTo(x, h+y);
		graphics.lineTo(x+w, h+y);
		graphics.lineTo(x+w, y);
		graphics.lineTo(x, y);
		graphics.endPath();
		graphics.setStyle(s);
	}

	public static void drawControlCodeRect(SimpleGraphics graphics,int style, int x, int y, int w, int h) {
		int s = graphics.getStyle();
		graphics.setStrokeWidth(1);
		graphics.setStyle(style);
		graphics.startPath();
//		graphics.moveTo(x, h/2+y);
		graphics.moveTo(x+w, h/2+y);
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

	private static final int sControlCodeColoe = SimpleGraphicUtil.parseColor("#99FF9911");
	private static final int sControlCodeColoe20 = SimpleGraphicUtil.parseColor("#22FF9911");
	public static final int BLACK = parseColor("#FF000000");
	public static final int GREEN = parseColor("#FF00FF00");
	public static final int YELLOW = parseColor("#FFFFFF00");;
	private static int drawControlCode(SimpleGraphics graphics, char code, int x, int y, int textSize) {
		int size = graphics.getSimpleFont().lengthOfControlCode(code, textSize);
		if(size != 0) {
			int ts = graphics.getTextSize();
			int c = graphics.getColor();
			if(code == 0x20||code == 0x09) {
				graphics.setColor(sControlCodeColoe20);
			} else {
				graphics.setColor(sControlCodeColoe);
			}
			graphics.setStrokeWidth(1);
			SimpleGraphicUtil.drawControlCodeRect(graphics, SimpleGraphics.STYLE_STROKE, x, y, size, -textSize);
			graphics.setTextSize(ts/3);
			graphics.drawText(""+Integer.toHexString((int)code), x, y);
			graphics.setTextSize(ts);
			graphics.setColor(c);
		}
		return size;
	}

	public static int parseColor(String colorSource) {
		String c = colorSource.replaceAll(" ","").replaceAll("#","");
		return (int)Long.parseLong(c, 16);
	}

	public static int argb(int a, int r, int g, int b) {
		int ret = ((a&0xff)<<(3*8))|((r&0xff)<<(2*8))|((g&0xff)<<(1*8))|((b&0xff)<<(0*8));
		return ret;
	}
	public static int colorA(int c) {
		int ret = 0xff&(c>>(3*8));
		return ret;		
	}
	public static int colorR(int c) {
		int ret = 0xff&(c>>(2*8));
		return ret;		
	}
	public static int colorG(int c) {
		int ret = 0xff&(c>>(1*8));
		return ret;		
	}
	public static int colorB(int c) {
		int ret = 0xff&(c>>(0*8));
		return ret;		
	}

}
