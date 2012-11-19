package info.kyorohiro.helloworld.display.simple;


import info.kyorohiro.helloworld.android.adapter.SimpleGraphicsForAndroid;
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
		graphics.drawLine(x+w, h/2+y, x+w, y);
		graphics.drawLine(x+w, y, x, y);
		/*
		graphics.setStrokeWidth(1);
		graphics.setStyle(style);
		graphics.startPath();
		graphics.moveTo(x+w, h/2+y);
		graphics.lineTo(x+w, y);
		graphics.lineTo(x, y);
		graphics.endPath();
		graphics.setStyle(s);*/
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
		//float[] widths = cash.getAllBufferedMoji(); 
		int start = 0;
		int end = 0;
		float xPlus = 0;
		long sTime = 0;
		//long mTime1 = 0;
		//long mTime2 = 0;
		//long mTime3 = 0;
		//long mTime4 = 0;
		//long mTime5 = 0;
		long eTime = 0;
		try{
			sTime = System.currentTimeMillis();
			if(!text.use(textSize)){
				//android.util.Log.v("time","time[--cash--]="+(eTime-sTime));
		//		text.setCash(font, textSize);
			}
			end = text.length();
			while(true) {
				//
				// あらかじめ計算しておくようにした。
				SimpleGraphicsForAndroid andrographics = (SimpleGraphicsForAndroid)graphics;
				float zoom = text.getCashZoomSize(textSize);
				andrographics.drawPosText(buffer, text.getCash(), zoom, start, end,x, y);

				//mTime2 = System.currentTimeMillis();
				if(len<=end){
					return;
				}

				for(int i=start;i<end;i++){
					xPlus +=text.getCash()[i];
				}
				//mTime4 = System.currentTimeMillis();
				xPlus += drawControlCode(graphics, buffer[end], x+(int)(xPlus*text.getCashZoomSize(textSize)), y, textSize, (int)text.getCash()[end]);
				//
				//mTime5 = System.currentTimeMillis();
				//android.util.Log.v("time","time[--21--]="+(mTime2-mTime1));
				//android.util.Log.v("time","time[--22--]="+(mTime3-mTime2));
				//android.util.Log.v("time","time[--23--]="+(mTime4-mTime3));
				//android.util.Log.v("time","time[--24--]="+(mTime5-mTime4));
				start = end+1;
				break;
			}
		}finally{
			eTime = System.currentTimeMillis();
			//android.util.Log.v("time","time[--2--]="+(eTime-sTime));
		}
	}

	private static final int sControlCodeColoe = SimpleGraphicUtil.parseColor("#99FF9911");
	private static final int sControlCodeColoe20 = SimpleGraphicUtil.parseColor("#22FF9911");
	public static final int BLACK = parseColor("#FF000000");
	public static final int GREEN = parseColor("#FF00FF00");
	public static final int YELLOW = parseColor("#FFFFFF00");;
	
	private static int drawControlCode(SimpleGraphics graphics, char code, int x, int y, int textSize, int baseWidth) {
//		long sTime = 0;
//		long mTime = 0;
//		long eTime = 0;
//		sTime = System.currentTimeMillis();
		int size = graphics.getSimpleFont().lengthOfControlCode(code, textSize);
//		mTime = System.currentTimeMillis();
		//if(true){
		//return size;
		//}
		///*
		if(size != 0){//&&(code == '\r'||code == '\n')) {
			int ts = graphics.getTextSize();
			int c = graphics.getColor();
			if(code == '\r'||code == '\n') {
				graphics.setColor(sControlCodeColoe);
				graphics.setStrokeWidth(1);
				SimpleGraphicUtil.drawControlCodeRect(graphics, SimpleGraphics.STYLE_STROKE, x, y, size, -textSize);
				graphics.setTextSize(ts/3);
				graphics.drawText(""+Integer.toHexString((int)code), x, y);
				graphics.setTextSize(ts);
			} else {
				graphics.setColor(sControlCodeColoe20);
				graphics.setStrokeWidth(1);
				SimpleGraphicUtil.drawControlCodeRect(graphics, SimpleGraphics.STYLE_STROKE, x, y, size, -textSize);
			}
			graphics.setColor(c);
		}//*/
//		eTime = System.currentTimeMillis();
//		android.util.Log.v("time","time[--1--]="+(eTime-sTime));
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
