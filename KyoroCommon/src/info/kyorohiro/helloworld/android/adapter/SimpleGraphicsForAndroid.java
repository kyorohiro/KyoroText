package info.kyorohiro.helloworld.android.adapter;

import java.io.InputStream;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;

public class SimpleGraphicsForAndroid extends SimpleGraphics {
	private Canvas mCanvas = null;
	private Paint mPaint = null;
	private int mGlobalX = 0;
	private int mGlobalY = 0;
	public final static int STYLE_STROKE = 1;
	public final static int STYLE_FILL = 2;

	public SimpleGraphicsForAndroid(Canvas canvas, int globalX, int globalY) {
		mCanvas = canvas;
		mGlobalX = globalX;
		mGlobalY = globalY;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setAlpha(0);
		// mPaint.setTypeface(Typeface.SANS_SERIF);
	}

	@Override
	public SimpleGraphics getChildGraphics(SimpleGraphics graphics,
			int globalX, int globalY) {
		mCanvas = ((SimpleGraphicsForAndroid) graphics).getCanvas();
		return new SimpleGraphicsForAndroid(mCanvas, globalX, globalY);
	}

	public void setGlobalPoint(int x, int y) {
		mGlobalX = x;
		mGlobalY = y;
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
		mCanvas.drawCircle(x + mGlobalX, y + mGlobalY, radius, mPaint);
	}

	public void drawLine(int startX, int startY, int stopX, int stopY) {
		mCanvas.drawLine(startX + mGlobalX, startY + mGlobalY,
				stopX + mGlobalX, stopY + mGlobalY, mPaint);
	}

	public void drawBackGround(int color) {
	//	mCanvas.drawColor(color, PorterDuff.Mode.CLEAR);
		mCanvas.drawColor(color);
	}

	public void drawText(CharSequence text, int x, int y) {
		mCanvas.drawText(text, 0, text.length(), x + mGlobalX, y + mGlobalY,
				mPaint);
	}

	Shader shader = null;
	@Override
	public void drawImageAsTile(SimpleImage image, int x, int y, int w, int h) {
		if (image.getImage().isRecycled()) {
			return;
		}
		/*
		// todo: examine following code.
		if(shader == null){
		 shader = new BitmapShader(image.getImage(),
				Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		}
		Paint paint = mPaint; //new Paint();
		paint.setShader(shader);
		Rect prev = mCanvas.getClipBounds();
		mCanvas.clipRect(x, y, x+w, y+h);
		mCanvas.drawPaint(paint);
		mCanvas.clipRect(prev);
		paint.setShader(null);
        //*/
		///*
		Bitmap bitmap = image.getImage();
		int imgW = bitmap.getWidth();
		int imgH = bitmap.getHeight();
		int lenW = w / imgW;
		int lenH = h / imgH;
		for (int _w = 0; _w <= lenW; _w++) {
			for (int _h = 0; _h <= lenH; _h++) {
				int srcW = imgW;
				int srcH = imgH;
				if (srcW > w - (imgW * _w)) {
					srcW = w - (imgW * _w);
				}
				if (srcH > h - (imgH * _h)) {
					srcH = h - (imgH * _h);
				}
				Rect src = new Rect(0, 0, srcW, srcH);
				Rect dst = new Rect(x + imgW * _w, y + imgH * _h, x + imgW * _w
						+ srcW, y + imgH * _h + srcH);
				mCanvas.drawBitmap(bitmap, src, dst, mPaint);
			}
		}
		//*/
	}

	public int getTextSize() {
		return (int) mPaint.getTextSize();
	}

	private int mMoveToX = 0;
	private int mMoveToY = 0;

	// rewrite : now draw line only , must support fill
	public void moveTo(int x, int y) {
		// mCanvas.
//		mMoveToX = x;
//		mMoveToY = y;
		mPath.moveTo(mGlobalX+x, mGlobalY+y);
	}

	public void lineTo(int x, int y) {
//		drawLine(mMoveToX, mMoveToY, x, y);
//		moveTo(x, y);
		mPath.lineTo(mGlobalX+x, mGlobalY+y);
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

	public void setStyle(int style) {
		Paint.Style paintStyle = Paint.Style.FILL;
		if (style == (STYLE_STROKE | STYLE_FILL)) {
			paintStyle = Paint.Style.FILL_AND_STROKE;
		} else if (style == (STYLE_STROKE)) {
			paintStyle = Paint.Style.STROKE;
		} else if (style == (STYLE_FILL)) {
			paintStyle = Paint.Style.FILL;
		}
		mPaint.setStyle(paintStyle);
	}

	public void setStrokeWidth(int w) {
		mPaint.setStrokeWidth(w);
	}

	public int getTextWidth(String line) {
		float[] w = new float[line.length()];
		mPaint.getTextWidths(line, w);
		float margine = 0;
		int len = line.length();
		for (int i = 0; i < len; i++) {
			margine += w[i];
		}
		return (int) margine;
	}

	@Override
	public SimpleDisplayObject createImage(byte[] data, int offset, int length) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length);

		return null;
	}

	private Path mPath = null;

	@Override
	public void startPath() {
		mPath = new Path();
	}

	@Override
	public void endPath() {
		if(mPath != null) {
			mCanvas.drawPath(mPath, mPaint);
			mPath.close();
			mPath = null;
		}
	}

}
