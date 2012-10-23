package info.kyorohiro.helloworld.display.widget.lineview;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.SimpleLockInter;
import android.graphics.Color;

public class LineView extends SimpleDisplayObjectContainer {
	public static float[] widths = new float[256];
	private final static int sTestTextColor = Color.parseColor("#AAFFFF00");
	private boolean mIsClearBG = false;
	private int mBiasAboutMoveLine = 0;
	private int mMergine = -1;
	private int mPointID = 0;
	private int mScaleX = 0;
	private int mScaleY = 0;
	private int mScaleTime = 0;

	private WeakHashMap<Integer, Point> mPoint = new WeakHashMap<Integer, Point>();

	private SimpleImage mImage = null;
	private int mNumOfLine = 300;
	private LineViewBufferSpec mInputtedText = null;
	private int mPositionY = 0;
	private int mPositionX = 0;
	private int mTextSize = 16;
	private float mScale = 1.0f;
	private int mBgColor = Color.parseColor("#FF000022");
	private boolean mIsTail = true;
	private int mDefaultCashSize = 100;
	private KyoroString[] mCashBuffer = new KyoroString[0];

	// setScaleとsetTextSize()で拡大率を設定している。
	// 後でどちらかに統一する？
	protected float getSclaeFromTextSize() {
		float scale = (getTextSize() / getBreakText().getTextSize());
		return scale;
	}

	public BreakText getBreakText() {
		if (getLineViewBuffer() == null) {
			return null;
		} else {
			return getLineViewBuffer().getBreakText();
		}
	}

	public synchronized Point getPoint(int num) {
		Point point = new Point(num, this);
		mPoint.put(mPointID++, point);
		return point;
	}

	public void isClearBG(boolean on) {
		mIsClearBG = on;
	}

	public boolean isOver() {
		if (this.isTail()
				&& mInputtedText.getMaxOfStackedElement() <= mInputtedText
						.getNumberOfStockedElement()) {
			return true;
		} else {
			return false;
		}
	}

	private synchronized void addPoint(int num) {
		if (isOver()) {
			for (Point p : mPoint.values()) {
				p.mPoint -= num;
				if (p.mPoint < 0) {
					p.mPoint = 0;
				}
			}
		}
	}

	public void fittableToView(boolean fit) {
		mDrawingPosition.fittableToView(fit);
	}

	public LineView(LineViewBufferSpec inputtedText, int textSize) {
		mInputtedText = inputtedText;
		mTextSize = textSize;
	}

	public LineView(LineViewBufferSpec inputtedText, int textSize, int cashSize) {
		mInputtedText = inputtedText;
		mTextSize = textSize;
		mDefaultCashSize = cashSize;
	}

	public void setBgColor(int color) {
		mBgColor = color;
	}

	public int getBgColor() {
		return mBgColor;
	}

	public void setScale(float scale) {
		mScale = scale;
	}

	public synchronized void setScale(float scale, float sScale, int sGetX,
			int linePosX, Point linePosY, int baseX, int baseY) {
		// mInputtedText.clearNumOfAdd();
		_updateStatus(mInputtedText);
		if (mBiasAboutMoveLine < 6) {
			mBiasAboutMoveLine += 2;
		}
		mScale = scale;
		int pos = (int) ((getHeight() - baseY) / (getShowingTextSize() * 1.2));//
		mScaleX = baseX;
		mScaleY = baseY;
		mScaleTime = 20;
		setPositionY(
				mInputtedText.getNumberOfStockedElement() - linePosY.getPoint()
						- pos - 1, true);
		setPositionX((int) (baseX - (baseX - sGetX) * scale / sScale), true);

	}

	public float getScale() {
		return mScale;
	}

	public void setTextSize(int textSize) {
		mTextSize = textSize;
	}

	public int getTextSize() {
		if (mTextSize <= 0) {
			return 1;
		}
		return mTextSize;
	}

	public int getShowingTextSize() {
		int ret = (int) (mTextSize * mScale);
		if (ret < 0) {
			return 1;
		}
		return ret;
	}

	public synchronized void setLineViewBufferSpec(
			LineViewBufferSpec inputtedText) {
		mInputtedText = (LineViewBufferSpec) inputtedText;
	}

	public synchronized LineViewBufferSpec getLineViewBuffer() {
		return mInputtedText;
	}

	// @Deprecated
	public synchronized int getShowingTextStartPosition() {
		return mDrawingPosition.getStart();
	}

	// @Deprecated
	public synchronized int getShowingTextEndPosition() {
		return mDrawingPosition.getEnd();
	}

	public synchronized void setPositionY(int position) {
		setPositionY(position, false);
	}

	public synchronized void setPositionY(int position, boolean ignoreBias) {
		if (mBiasAboutMoveLine <= 0 || ignoreBias) {
			mPositionY = position;
		}
	}

	public synchronized int getPositionY() {
		return mPositionY;
	}

	public void setPositionX(int x) {
		setPositionX(x, false);
	}

	public void setPositionX(int x, boolean ignoreBias) {
		if (mBiasAboutMoveLine <= 0 || ignoreBias) {
			mPositionX = x;
		}
	}

	public int getPositionX() {
		return mPositionX;
	}

	public int getBlinkY() {
		return mDrawingPosition.getBlank();
	}

	public boolean isTail() {
		return mIsTail;
	}

	public void isTail(boolean on) {
		mIsTail = on;
	}

	private DrawingPositionForLineView mDrawingPosition = new DrawingPositionForLineView();

	public void setBGImage(SimpleImage image) {
		mImage = image;
	}

	public void setMergine(int mergine) {
		mMergine = mergine;
	}

	public int getMergine() {
		if (mMergine == -1) {
			return mMergine = (getWidth()) / 20;
		} else {
			return mMergine;
		}
	}

	public int getLeftForStartDrawLine() {
		return getMergine() + mPositionX;
	}

	public int getYForStartDrawLine(int cursurCol) {
		int yy = (int) ((int) (getShowingTextSize() * 1.2))
				* (getBlinkY() + cursurCol + 1);
		return yy;
	}

	public int getWidth(int cursorCol, float[] w) {
		return getWidth(cursorCol, w, getShowingTextSize());
	}

	public int getWidth(int cursorCol, float[] w, int textSize) {
		LineViewBufferSpec mInputtedText = getLineViewBuffer();
		if (mInputtedText == null || null == mInputtedText.getBreakText()) {
			return -1;
		} else if (cursorCol >= mInputtedText.getNumberOfStockedElement()
				|| cursorCol < 0) {
			return -1;
		}

		KyoroString data = mInputtedText.get(cursorCol);
		if (data == null) {
			return -1;
		}
		int l = mInputtedText.getBreakText().getTextWidths(data, 0,
				data.length(), w, textSize);
		return l;
	}

	// @Deprecated
	public int getXToPosX(int cursorCol, int xx, int cur) {
		float x = xx;// /getScale();
		x -= getLeftForStartDrawLine();
		int l = getWidth(cursorCol, widths);

		float ww = 0;
		for (int i = 0; i < l; i++) {
			ww += widths[i];// * getSclaeFromTextSize();
			if (ww > x) {
				return i;
			}
		}
		return l;
	}

	public int getYToPosY(int y) {
		int n = (int) (y / (int) ((getShowingTextSize() * 1.2)));
		int yy = n - getBlinkY() - 1;
		// android.util.Log.v("kiyo","DD="+getBlinkY()+","+n+","+getScale()+","+getShowingTextSize());
		return yy + (getShowingTextStartPosition());
	}

	public int getLineYForShowLine(int cursurRow, int cursurCol) {
		int yy = getYForStartDrawLine(cursurCol);
		int yyy = yy + (int) (getShowingTextSize() * 0.2);
		return yyy;
	}

	public KyoroString getLineViewData(int cursorCol) {
		if (mInputtedText == null
				|| cursorCol >= mInputtedText.getNumberOfStockedElement()
				|| cursorCol < 0) {
			return null;
		}
		try {
			lock();
			KyoroString data = mInputtedText.get(cursorCol);
			return data;
		} catch (Exception e) {
			return null;
		} finally {
			releaseLock();
		}
	}

	private void showLineDate(SimpleGraphics graphics, KyoroString[] list,
			int len) {
		if (len > list.length) {
			len = list.length;
		}

		if (mPositionX > 0) {
			mPositionX = 0;
		}

		float positionMax = -1
				 * getBreakText().getWidth() * getShowingTextSize() / 
				 (float) getBreakText().getTextSize()
				//* (getBreakText().getWidth() * mScale)
				//* (getWidth()-getMergine()*2)/ (float) (getBreakText().getWidth())
				 ;

//		android.util.Log.v("kiyo", "positionMax _=" + positionMax+","+(getWidth() - getMergine() * 2));
	//	positionMax *= getShowingTextSize() / (float)getBreakText().getTextSize();
		positionMax += getWidth() - getMergine() * 2.2;
//		positionMax += getWidth()*getShowingTextSize() / (float)getBreakText().getTextSize()- getMergine() * 2;
//		android.util.Log.v("kiyo", "positionMax=" + positionMax);
//		android.util.Log.v("kiyo",",w="+ getBreakText().getWidth() + "," + getWidth()+ ",m="+ getMergine());
//		android.util.Log.v("kiyo",",scale=" + mScale);
//		android.util.Log.v("kiyo","tsize=" + getShowingTextSize() + ","+ getBreakText().getTextSize()+
//				","+getTextSize() );
//		android.util.Log.v("kiyo",",logical=" + (getWidth()-getMergine()*2)
///				/ (float) (getBreakText().getWidth()));
		
		if (mPositionX < positionMax) {
			mPositionX = (int) positionMax;
		}

		// int scaleLine = mScaleLine-getShowingTextStartPosition();
		for (int i = 0; i < len; i++) {
			if (list[i] == null) {
				continue;
			}

			graphics.setColor(list[i].getColor());
			int x = getLeftForStartDrawLine();
			int y = getYForStartDrawLine(i);
			int yy = getLineYForShowLine(0, i);

			graphics.drawText(list[i], x, y);
			if (list[i].includeLF()) {
				int c = list[i].getColor();
				graphics.setColor(Color.argb(127, Color.red(c), Color.green(c),
						Color.blue(c)));
				graphics.setStrokeWidth(1);
				graphics.drawLine(10, yy, graphics.getWidth() - 10, yy);
			}
		}
	}

	private Thread currentThread = null;
	int num = 0;

	public synchronized void lock() {
		try {
			while (currentThread != null
					&& currentThread != Thread.currentThread()) {
				wait();
			}
			if (mInputtedText instanceof SimpleLockInter) {
				((SimpleLockInter) mInputtedText).beginLock();
			}
		} catch (InterruptedException e) {
		} finally {
			currentThread = Thread.currentThread();
			num++;
		}
		// android.util.Log.v("kiyo","lock="+num);
	}

	public synchronized void releaseLock() {
		if (mInputtedText instanceof SimpleLockInter) {
			((SimpleLockInter) mInputtedText).endLock();
		}
		num--;
		if (num == 0 && currentThread == Thread.currentThread()) {
			notifyAll();
			currentThread = null;
		}
		// android.util.Log.v("kiyo","unlock="+num);
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		graphics.clipRect(0, 0, getWidth(), getHeight());
		if (mBiasAboutMoveLine > 0) {
			mBiasAboutMoveLine--;
		}
		LineViewBufferSpec showingText = mInputtedText;
		KyoroString[] list = null;
		int len = 0;

		// update status
		try {
			lock();
			// update statusr
			_updateStatus(showingText);
			// get buffer
			len = _getBuffer(showingText);
			list = mCashBuffer;
		} finally {
			releaseLock();
		}

		// show buffer
		graphics.setTextSize(getShowingTextSize());// todo mScale

		// draw extra

		{// bg
			drawBG(graphics);
		}
		{// line number
			int s = graphics.getTextSize();
			graphics.setTextSize(s * 3);
			graphics.setColor(sTestTextColor);
			graphics.drawText("" + getShowingTextStartPosition() + ":"
					+ getShowingTextEndPosition(), 30, s * 4);
			graphics.setTextSize(s);
		}
		{// scale in out animation
			if (mScaleTime > 0) {
				graphics.setColor(Color.argb(mScaleTime, 0xff, 0xff, 0x00));
				graphics.drawCircle(mScaleX, mScaleY, 30);
				mScaleTime -= 3;
			}
		}
		// draw content
		if (list != null) {// bug fix
			showLineDate(graphics, list, len);
		}
		// */
		// fin
		super.paint(graphics);
		graphics.clipRect(-1, -1, -1, -1);
	}

	private int _getBuffer(LineViewBufferSpec showingText) {
		int start = mDrawingPosition.getStart();
		int end = mDrawingPosition.getEnd();
		int len = 0;
		if (start <= end) {
			len = end - start;
		}
		if (mCashBuffer.length < len) {
			int buffeSize = len;
			if (buffeSize < mDefaultCashSize) {
				buffeSize = mDefaultCashSize;
			}
			mCashBuffer = new KyoroString[buffeSize];
		}
		mCashBuffer = showingText.getElements(mCashBuffer, start, end);
		return len;
	}

	protected void _updateStatus(LineViewBufferSpec showingText) {
		mNumOfLine = (int) (getHeight() / (getShowingTextSize() * 1.2));// todo
		try {
			lock();
			if (!mIsTail || mPositionY > 1) {
				// mPositionY += showingText.getNumOfAdd();
				setPositionY(mPositionY + showingText.getNumOfAdd(), true);

				addPoint(showingText.getNumOfAdd());
			}
			showingText.clearNumOfAdd();
			mDrawingPosition.updateInfo(this, mPositionY, getHeight(), mTextSize, mScale, showingText);
		} finally {
			releaseLock();
		}
	}

	private void drawBG(SimpleGraphics graphics) {
		if (mIsClearBG) {
			int w = getWidth();
			int h = getHeight();

			graphics.startPath();
			graphics.setStyle(SimpleGraphics.STYLE_FILL);
			graphics.moveTo(0, 0);
			graphics.lineTo(0, h);
			graphics.lineTo(w, h);
			graphics.lineTo(w, 0);
			graphics.lineTo(0, 0);
			graphics.endPath();
			if (mImage != null) {
				graphics.drawImageAsTile(mImage, 0, 0, getWidth(), getHeight());
			}
		}
	}

	public static class Point {
		WeakReference<LineView> mR;

		private Point(int point, LineView v) {
			mPoint = point;
			mR = new WeakReference<LineView>(v);
		}

		public int getPoint() {
			return mPoint;
		}

		public void setPoint(int point) {
			mPoint = point;
		}

		private int mPoint = 0;
	}

}
