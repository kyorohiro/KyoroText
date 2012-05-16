package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;
import info.kyorohiro.helloworld.util.CyclingListInter;
import android.graphics.Color;

public class LineView extends SimpleDisplayObject {
	private SimpleImage mImage = null;
	private int mNumOfLine = 300;
	private CyclingListInter<LineViewData> mInputtedText = null;
	private int mPosition = 0;
	private int mPosX = 0;
	private int mTextSize = 16;
	private int mShowingTextStartPosition = 0;
	private int mShowingTextEndPosition = 0;
	private float mScale = 1.0f;
	private int mAddedPoint = 0;
	private int mBgColor = Color.parseColor("#FF000022");
	private boolean mIsTail = true;
	private int mDefaultCashSize = 100;
	private LineViewData[] mCashBuffer = new LineViewData[0];

	//==========================================================
	// todo refactaring
	private int mTodoExtraY = 0;
	private int mCash  = 0;
	private int mTestTextColor = Color.parseColor("#33FFFF00");
	//
	//==========================================================

	public LineView(CyclingListInter<LineViewData> inputtedText, int textSize) {
		mInputtedText = inputtedText;
		mTextSize = textSize;
	}

	public LineView(CyclingListInter<LineViewData> inputtedText, int textSize, int cashSize) {
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

	public float getScale() {
		return mScale;
	}

	public void setTextSize(int textSize) {
		mTextSize = textSize;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public synchronized void setCyclingList(CyclingListInter<LineViewData> inputtedText) {
		mInputtedText = inputtedText;
	}

	public synchronized CyclingListInter<LineViewData> getCyclingList() {
		return mInputtedText;
	}

	public synchronized int getShowingTextStartPosition() {
		return mShowingTextStartPosition;
	}

	public synchronized int getShowingTextEndPosition() {
		return mShowingTextEndPosition;
	}

	public synchronized void setPositionY(int position) {
		mPosition = position;
	}

	public void setPositionX(int x) {
		mPosX = x;
	}

	public void isTail(boolean on) {
		mIsTail = on;
	}

	public synchronized int getTodoExtra() {
		return mTodoExtraY;
	}
	public synchronized int setTodoExtra(int extra) {
		return mTodoExtraY = extra;
	}
	public synchronized int getPositionY() {
		return mPosition;
	}

	public int getPositionX() {
		return mPosX;
	}

	private void showLineDate(SimpleGraphics graphics, LineViewData[] list, int len, int blank) {
		if(len > list.length){
			len = list.length;
		}

		for (int i = 0; i < len; i++) {
			if (list[i] == null) {
				continue;
			}
			// drawLine
			graphics.setColor(list[i].getColor());
			if(mPosX >0) {
				mPosX = 0;
			}
			if(mPosX < -1*(getWidth()*mScale-getWidth())) {
				mPosX = -1*(int)(getWidth()*mScale-getWidth());
			}
			int x = (getWidth()) / 20 + mPosX*19/20; //todo mPosY
			int y = mTodoExtraY+
			(int)(graphics.getTextSize()*1.2) * (blank + i + 1);
			int yy = y + (int)(graphics.getTextSize()*0.2);

			graphics.drawText(list[i], x, y);
			if (list[i].getStatus() == LineViewData.INCLUDE_END_OF_LINE) {
				int c = list[i].getColor();
				graphics.setColor(Color.argb(127,Color.red(c),Color.green(c),Color.blue(c)));
				graphics.setStrokeWidth(1);
				graphics.drawLine(10, yy, graphics.getWidth()-10, yy);
			}
		}
	}

	private DrawingPosition mDrawingPosition = new DrawingPosition();
	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		CyclingListInter<LineViewData> showingText = mInputtedText;
		int start = 0;
		int end = 0;
		int blank = 0;
		LineViewData[] list = null;
		int len = 0;
		try {
			if(showingText instanceof SimpleLockInter) {
				((SimpleLockInter) showingText).beginLock();
			}

			updateStatus(graphics, showingText);
			drawBG(graphics);
			{
				//test 
				int s = graphics.getTextSize();
				graphics.setTextSize(s*3);
				graphics.setColor(mTestTextColor);
				graphics.drawText(""+mShowingTextStartPosition+":"+mShowingTextEndPosition, 30, s*4);
				graphics.setTextSize(s);
			}
			mDrawingPosition.updateInfo(mPosition, getHeight(),
					mTextSize, mScale, showingText);
			start = mDrawingPosition.getStart();
			end = mDrawingPosition.getEnd();
			blank = mDrawingPosition.getBlank();
			if (start > end) {
				len = 0;
			} else {
				len = end-start;
			}
			if(mCashBuffer.length < len) {
				int buffeSize = len;
				if(buffeSize<mDefaultCashSize) {
					buffeSize = mDefaultCashSize;
				}
				mCashBuffer = new LineViewData[buffeSize];
			}
			list = mCashBuffer;
			list = showingText.getElements(list, start, end);

		} finally {
			if(showingText instanceof SimpleLockInter) {
				((SimpleLockInter) showingText).endLock();
			}
		}

		showLineDate(graphics, list, len, blank);
		mShowingTextStartPosition = start;
		mShowingTextEndPosition = end;
	}


	public void setBGImage(SimpleImage image) {
		mImage = image;
	}

	private void drawBG(SimpleGraphics graphics) {
		graphics.drawBackGround(mBgColor);
		if(mImage != null) {
			graphics.drawImageAsTile(mImage, 0, 0,getWidth(), getHeight());
		}
	}

	private void updateStatus(SimpleGraphics graphics,
			CyclingListInter<LineViewData> showingText) {
		mNumOfLine = (int)(getHeight() / (mTextSize*1.2*mScale));//todo mScale
		{
			// todo refactaring
			// current Position
			if(!mIsTail||mPosition > 1) {
				int a = resetAddPositionY();
				if(a != 0){
					mCash += showingText.getNumOfAdd();
					mPosition = mCash+a;
				} else {
					mCash = 0;
					mPosition += showingText.getNumOfAdd();				
				}
			}
			showingText.clearNumOfAdd();
		}
		int blankSpace = mNumOfLine / 2;
		if (mPosition < -(mNumOfLine - blankSpace)) {
			setPositionY(-(mNumOfLine - blankSpace) - 1);
		} else if (mPosition > (showingText.getNumberOfStockedElement() - blankSpace)) {
			setPositionY(showingText.getNumberOfStockedElement() - blankSpace);
		}
		graphics.setTextSize((int)(mTextSize*mScale));//todo mScale
	}

	public synchronized void addPositionY(int position) {
		mAddedPoint += position;
	}

	private synchronized int resetAddPositionY() {
		int tmp = mAddedPoint;
		mAddedPoint = 0;
		return tmp;
	}

}

