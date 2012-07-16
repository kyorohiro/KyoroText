package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;
import info.kyorohiro.helloworld.util.CyclingListInter;
import android.graphics.Color;

public class LineView extends SimpleDisplayObjectContainer {
	// extends SimpleDisplayObject {
	private int mScaleX = 0;
	private int mScaleY = 0;
	private int mScaleTime = 0;

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
	private LineViewData[] mCashBuffer = new LineViewData[0];
	private float[] widths = new float[1024];//<---refataging
	// todo refactaring
	private int mTestTextColor = Color.parseColor("#33FFFF00");
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

	public synchronized void setScale(float scale, float sScale, int sGetX,int linePosX,int linePosY, int baseX, int baseY) {
		mScale = scale;
		_updateStatus(mInputtedText);
		int pos = (int) ((getHeight() - baseY) / (getShowingTextSize() * 1.2));//
		mScaleX = baseX;
		mScaleY = baseY;
		mScaleTime = 20;
		setPositionY(mInputtedText.getNumberOfStockedElement() - linePosY - pos-1);

		
		{
			int l = getWidth(linePosX, widths, (int)(getTextSize()*sScale));
			float ww = 0;
			for (int i = 0; i < l || i < linePosX; i++) {
				ww += widths[i]; 
			}
			ww = sGetX;
			float option = baseX * scale -baseX*sScale;
			setPositionX((int) ((ww-option) * sScale / scale));

			//			setPositionX(getWidth()-(int) ((baseX+ww) * sScale / scale));
			//android.util.Log.v("kiyo", "" + ((int) ((ww-option) * sScale / scale))
			//		+ ","+ ((int) ((ww) * sScale / scale))+
			//		","+scale+","+sScale);

		}

	}

	public float getScale() {
		return mScale;
	}

	public void setTextSize(int textSize) {
		mTextSize = textSize;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public int getShowingTextSize() {
		return (int) (mTextSize * mScale);
	}

	public synchronized void setLineViewBufferSpec(
			LineViewBufferSpec inputtedText) {
		mInputtedText = (LineViewBufferSpec) inputtedText;
	}

	public synchronized LineViewBufferSpec getLineViewBuffer() {
		return mInputtedText;
	}

	@Deprecated
	public synchronized int getShowingTextStartPosition() {
		return mDrawingPosition.getStart();
	}

	@Deprecated
	public synchronized int getShowingTextEndPosition() {
		return mDrawingPosition.getEnd();
	}

	public synchronized void setPositionY(int position) {
		mPositionY = position;
	}

	public synchronized int getPositionY() {
		return mPositionY;
	}

	public void setPositionX(int x) {
		mPositionX = x;
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

	private DrawingPosition mDrawingPosition = new DrawingPosition();

	public void setBGImage(SimpleImage image) {
		mImage = image;
	}

	public int getXForShowLine(int x, int y) {
		return (getWidth()) / 20 + mPositionX * 19 / 20;
	}

	public int getYForShowLine(int textSize, int cursurCol) {
		int yy = (int) ((int) (textSize * 1.2)) * (getBlinkY() + cursurCol + 1);
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

		LineViewData data = mInputtedText.get(cursorCol);
		if (data == null) {
			return -1;
		}
		int l = mInputtedText.getBreakText().getTextWidths(data, 0,
				data.length(), w, textSize);
		return l;
	}

	@Deprecated
	public int getXToPosX(int cursorCol, int xx, int cur) {
		float x = xx;// /getScale();
		x -= getXForShowLine(0, cursorCol);
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
		int n = (int) (y / (int)((getShowingTextSize() * 1.2)));
		int yy = n - getBlinkY() - 1;
//		android.util.Log.v("kiyo","DD="+getBlinkY()+","+n+","+getScale()+","+getShowingTextSize());
		return yy + (getShowingTextStartPosition());
	}

	public int getLineYForShowLine(int textSize, int cursurRow, int cursurCol) {
		int yy = getYForShowLine(textSize, cursurCol);
		int yyy = yy + (int) (textSize * 0.2);
		return yyy;
	}

	public LineViewData getLineViewData(int cursorCol) {
		if (mInputtedText == null
				|| cursorCol >= mInputtedText.getNumberOfStockedElement()
				|| cursorCol < 0) {
			return null;
		}
		LineViewBufferSpec showingText = mInputtedText;
		try {
			if (showingText instanceof SimpleLockInter) {
				((SimpleLockInter) showingText).beginLock();
			}
			LineViewData data = mInputtedText.get(cursorCol);
			return data;
		} catch (Exception e) {
			return null;
		} finally {
			if (showingText instanceof SimpleLockInter) {
				((SimpleLockInter) showingText).endLock();
			}
		}
	}

	private void showLineDate(SimpleGraphics graphics, LineViewData[] list,
			int len) {
		if (len > list.length) {
			len = list.length;
		}

		if (mPositionX > 0) {
			mPositionX = 0;
		}
		if (mPositionX < -1 * (getWidth() * mScale - getWidth())) {
			mPositionX = -1 * (int) (getWidth() * mScale - getWidth());
		}

		// int scaleLine = mScaleLine-getShowingTextStartPosition();
		for (int i = 0; i < len; i++) {
			if (list[i] == null) {
				continue;
			}

			graphics.setColor(list[i].getColor());
			int x = getXForShowLine(0, i);
			int y = getYForShowLine(graphics.getTextSize(), i);
			int yy = getLineYForShowLine(graphics.getTextSize(), 0, i);

			graphics.drawText(list[i], x, y);
			if (list[i].getStatus() == LineViewData.INCLUDE_END_OF_LINE) {
				int c = list[i].getColor();
				graphics.setColor(Color.argb(127, Color.red(c), Color.green(c),
						Color.blue(c)));
				graphics.setStrokeWidth(1);
				graphics.drawLine(10, yy, graphics.getWidth() - 10, yy);
			}
		}
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		LineViewBufferSpec showingText = mInputtedText;
		LineViewData[] list = null;
		int len = 0;
		
		// update status
		try {
			if (showingText instanceof SimpleLockInter) {
				((SimpleLockInter) showingText).beginLock();
			}
			// update statusr
			_updateStatus(showingText);
			// get buffer
			len = _getBuffer(showingText);
			list = mCashBuffer;
		} finally {
			if (showingText instanceof SimpleLockInter) {
				((SimpleLockInter) showingText).endLock();
			}
		}

		// show buffer
		graphics.setTextSize(getShowingTextSize());// todo mScale

		// draw extra

		{//bg
			drawBG(graphics);
		}

		{//line number
			int s = graphics.getTextSize();
			graphics.setTextSize(s * 3);
			graphics.setColor(mTestTextColor);
			graphics.drawText("" + getShowingTextStartPosition() + ":"
					+ getShowingTextEndPosition(), 30, s * 4);
			graphics.setTextSize(s);
		}

		{//scale in out animation
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
		
		// fin
		super.paint(graphics);
	}

	private int _getBuffer(LineViewBufferSpec showingText) {
		int start = mDrawingPosition.getStart();
		int end = mDrawingPosition.getEnd();
		int len = 0;
		if (start<=end) {
			len = end - start;
		}
		if (mCashBuffer.length < len) {
			int buffeSize = len;
			if (buffeSize < mDefaultCashSize) {
				buffeSize = mDefaultCashSize;
			}
			mCashBuffer = new LineViewData[buffeSize];
		}
		mCashBuffer = showingText.getElements(mCashBuffer, start, end);
		return len;
	}

	protected void _updateStatus(LineViewBufferSpec showingText) {
		mNumOfLine = (int) (getHeight() / (getShowingTextSize() * 1.2));// todo
		if (!mIsTail || mPositionY > 1) {
			mPositionY += showingText.getNumOfAdd();
		}
		showingText.clearNumOfAdd();

		int blankSpace = mNumOfLine / 2;
		if (mPositionY < -(mNumOfLine - blankSpace)) {
			setPositionY(-(mNumOfLine - blankSpace) - 1);
		} else if (mPositionY > (showingText.getNumberOfStockedElement() - blankSpace)) {
			setPositionY(showingText.getNumberOfStockedElement() - blankSpace);
		}
		mDrawingPosition.updateInfo(mPositionY, getHeight(), mTextSize, mScale,
				showingText);
	}

	private void drawBG(SimpleGraphics graphics) {
		graphics.drawBackGround(mBgColor);
		if (mImage == null) {return;}
		graphics.drawImageAsTile(mImage, 0, 0, getWidth(), getHeight());
	}
}
