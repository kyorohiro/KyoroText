package info.kyorohiro.helloworld.display.simple;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class SimpleGraphics {
	private Canvas mCanvas = null;
	private Paint mPaint = null;
	private int mGlobalX = 0;
	private int mGlobalY = 0;
	public final static int STYLE_STROKE = 1;
	public final static int STYLE_FILL = 2;


	public int getGlobalX() {
		return mGlobalX;
	}
	
	public int getGlobalY() {
		return mGlobalY;
	}

	public abstract SimpleGraphics getChildGraphics(SimpleGraphics graphics, int globalX, int globalY);
	public abstract void drawCircle(int x, int y, int radius);	
	public abstract void drawLine(int startX, int startY, int stopX, int stopY);	
	public abstract void drawBackGround(int color);
	public abstract void drawText(String text, int x, int y);
	public abstract int getTextSize();
	public abstract void moveTo(int x, int y);
	public abstract void lineTo(int x, int y);
	public abstract int getWidth();	
	public abstract int getHeight();
	public abstract void setColor(int color);
	public abstract void setTextSize(int size);
	public abstract void setStyle(int style);
	public abstract void setStrokeWidth(int w);
	public abstract int getTextWidth(String line);
}