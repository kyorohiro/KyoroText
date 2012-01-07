package info.kyorohiro.helloworld.logcat.display.simple;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SimpleGraphics {
	private Canvas mCanvas = null;
	private Paint mPaint = null;
	private int mGlobalX = 0;
	private int mGlobalY = 0;
	public final static int STYLE_STROKE = 1;
	public final static int STYLE_FILL = 2;

	public SimpleGraphics(Canvas canvas, int globalX, int globalY) {
		mCanvas = canvas;
		mGlobalX = globalX;
		mGlobalY = globalY;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	public int getGlobalX() {
		return mGlobalX;
	}
	
	public int getGlobalY() {
		return mGlobalY;
	}

	public Canvas getCanvas() {
		return mCanvas;
	}

	public void drawCircle(int x, int y, int radius) {
		mCanvas.drawCircle(x+mGlobalX, y+mGlobalY, radius, mPaint);
	}
	
	public void drawLine(int startX, int startY, int stopX, int stopY) {
		mCanvas.drawLine(startX+mGlobalX, startY+mGlobalY, stopX+mGlobalX, stopY+mGlobalY, mPaint);
	}
	
	public void drawBackGround(int color) {
		mCanvas.drawColor(color);
	}

	public void drawText(String text, int x, int y) {
		mCanvas.drawText(text, x, y, mPaint);
	}

	public int getTextSize(){
		return (int)mPaint.getTextSize();
	}
	private int mMoveToX = 0;
	private int mMoveToY = 0;

	// rewrite : now draw line only , must support fill 
	public void moveTo(int x, int y){
		mMoveToX = x;
		mMoveToY = y;
	}

	public void lineTo(int x, int y){
		drawLine(mMoveToX, mMoveToY, x, y);
		moveTo(x,y);
	}

	public int getWidth() {
		return mCanvas.getWidth();
	}
	
	public int getHeight() {
		return mCanvas.getHeight();
	}

	public void setColor(int color) {
		mPaint.setColor(color);
	}

	public void setTextSize(int size) {
		mPaint.setTextSize(size);
	}

	public void setStyle(int style){
		Paint.Style paintStyle = Paint.Style.FILL;
		if (style == (STYLE_STROKE|STYLE_FILL)){
			paintStyle = Paint.Style.FILL_AND_STROKE;			
		} else if (style == (STYLE_STROKE)){
			paintStyle = Paint.Style.STROKE;
		} else if (style == (STYLE_FILL) ) {
			paintStyle = Paint.Style.FILL;
		}
		mPaint.setStyle(paintStyle);
	}
	
	public void setStrokeWidth(int w){
		mPaint.setStrokeWidth(w);
	}

	public int getTextWidth(String line) {
		float[] w = new float[line.length()];
		mPaint.getTextWidths(line,w);
		float margine = 0;
		int len = line.length();
		for(int i=0;i<len;i++){
			margine += w[i];
		}
		return (int)margine;
	}
}
