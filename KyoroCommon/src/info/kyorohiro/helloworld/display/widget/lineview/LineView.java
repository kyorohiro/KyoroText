package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.util.CyclingListInter;
import info.kyorohiro.helloworld.util.LineViewBufferSpec;
import android.graphics.Color;
import android.graphics.Paint;

public class LineView
extends SimpleDisplayObjectContainer {
//extends SimpleDisplayObject {
	private SimpleImage mImage = null;
	private int mNumOfLine = 300;
	private LineViewBufferSpec<LineViewData> mInputtedText = null;
	private int mBlankY = 0;
	private int mPositionY = 0;
	private int mPositionX = 0;
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
	private int mCash  = 0;
	private int mTestTextColor = Color.parseColor("#33FFFF00");
	//
	//==========================================================

	public LineView(LineViewBufferSpec<LineViewData> inputtedText, int textSize) {
		mInputtedText = inputtedText;
		mTextSize = textSize;
	}

	public LineView(LineViewBufferSpec<LineViewData> inputtedText, int textSize, int cashSize) {
		mInputtedText = inputtedText;
		mTextSize = textSize;
		mDefaultCashSize = cashSize;
	}

	public void setBgColor(int color) {mBgColor = color;}
	public int getBgColor() {return mBgColor;}
	public void setScale(float scale) {mScale = scale;}
	public float getScale() {return mScale;}
	public void setTextSize(int textSize) {mTextSize = textSize;}
	public int getTextSize() {return mTextSize;}
	public synchronized void setCyclingList(CyclingListInter<LineViewData> inputtedText) {mInputtedText = inputtedText;}
	public synchronized LineViewBufferSpec<LineViewData> getLineViewBuffer() {return mInputtedText;}

	@Deprecated
	public synchronized CyclingListInter<LineViewData> getCyclingList() {return (CyclingListInter<LineViewData>)mInputtedText;}
	public synchronized int getShowingTextStartPosition() {return mShowingTextStartPosition;}
	public synchronized int getShowingTextEndPosition() {return mShowingTextEndPosition;}
	public synchronized void setPositionY(int position) {mPositionY = position;}
	public synchronized int getPositionY() {return mPositionY;}
	public void setPositionX(int x) {mPositionX = x;}
	public int getPositionX() {return mPositionX;}
	public int getBlinkY(){return mBlankY;}
	public void isTail(boolean on) {mIsTail = on;}
	private DrawingPosition mDrawingPosition = new DrawingPosition();

	public void setBGImage(SimpleImage image) {
		mImage = image;
	}
	public synchronized void addPositionY(int position) {
		mAddedPoint += position;
	}
	private synchronized int resetAddPositionY() {
		int tmp = mAddedPoint;
		mAddedPoint = 0;
		return tmp;
	}

	public int getXForShowLine(int x,int y) {
		return (getWidth()) / 20 + mPositionX*19/20;
	}

	public int getYForShowLine(int textSize,int x,int y) {
		int yy = (int)(textSize*1.2) * (mBlankY + y + 1);
		return yy;
	}

	public int getYToPosY(int y) {
		int n= (int)(y/(getTextSize()*1.2));
		int yy = n-mBlankY-1;
		android.util.Log.v("kiyo","yy="+yy+","+y+","+mBlankY);
		return yy;
	}

	public LineViewData getLineViewData(int cursorCol){
		if(mCashBuffer == null || cursorCol >= mCashBuffer.length|| cursorCol<0){
			return null;
		}
		LineViewData data = mCashBuffer[cursorCol];
		return data;
	}
	@Deprecated
	public int getXToPosX(Paint paint, int cursorCol, int x, int cur) {
		if(mCashBuffer == null || cursorCol >= mCashBuffer.length|| cursorCol<0){
			android.util.Log.v("nky","ret="+mCashBuffer.toString()+","+cursorCol +","+x+","+mCashBuffer.length);
			return cur;
		}
		LineViewData data = mCashBuffer[cursorCol];
		if(data== null){
			return cur;
		}
		int ret = paint.breakText(data, 0, data.length(), false,
				x-getXForShowLine(0,cursorCol),//(float)(getWidth()*8/10.0)-getXForShowLine(0,cursorCol),
				null);
		android.util.Log.v("nky","ret="+ret+",data="+data);
		return ret;
	}

	public int getLineYForShowLine(int textSize,int x,int y) {
		int yy = (int)(textSize*1.2) * (mBlankY + y + 1);
		int yyy = yy + (int)(textSize*0.2);
		return yyy;
	}

	private void showLineDate(SimpleGraphics graphics, LineViewData[] list, int len) {
		if(len > list.length){
			len = list.length;
		}

		if(mPositionX >0) {
			mPositionX = 0;
		}
		if(mPositionX < -1*(getWidth()*mScale-getWidth())) {
			mPositionX = -1*(int)(getWidth()*mScale-getWidth());
		}

		for (int i = 0; i < len; i++) {
			if (list[i] == null) {
				continue;
			}
			// drawLine
			graphics.setColor(list[i].getColor());

			int x = getXForShowLine(0,i);
//			int y = (int)(graphics.getTextSize()*1.2) * (mBlankY + i + 1);
//			int yy = y + (int)(graphics.getTextSize()*0.2);
			int y = getYForShowLine(graphics.getTextSize(), 0,i);
			int yy = getLineYForShowLine(graphics.getTextSize(), 0,i);

			graphics.drawText(list[i], x, y);
			if (list[i].getStatus() == LineViewData.INCLUDE_END_OF_LINE) {
				int c = list[i].getColor();
				graphics.setColor(Color.argb(127,Color.red(c),Color.green(c),Color.blue(c)));
				graphics.setStrokeWidth(1);
				graphics.drawLine(10, yy, graphics.getWidth()-10, yy);
			}
		}
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		LineViewBufferSpec<LineViewData> showingText = mInputtedText;
		int start = 0;
		int end = 0;
//		int blank = 0;
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
			mDrawingPosition.updateInfo(mPositionY, getHeight(),
					mTextSize, mScale, showingText);
			start = mDrawingPosition.getStart();
			end = mDrawingPosition.getEnd();
			mBlankY = mDrawingPosition.getBlank();
			if (start > end) {
				len = 0;
			} else {
				len = end-start;
			}
			android.util.Log.v("kiyo","1:"+list);
			if(mCashBuffer.length < len) {
				int buffeSize = len;
				if(buffeSize<mDefaultCashSize) {
					buffeSize = mDefaultCashSize;
				}
				mCashBuffer = new LineViewData[buffeSize];
			}
			list = mCashBuffer;
			list = showingText.getElements(list, start, end);
			android.util.Log.v("kiyo","2:"+list);

		} finally {
			if(showingText instanceof SimpleLockInter) {
				((SimpleLockInter) showingText).endLock();
			}
		}

		if(list != null) {//bug fix
			showLineDate(graphics, list, len);
		}
		mShowingTextStartPosition = start;
		mShowingTextEndPosition = end;
		super.paint(graphics);
	}

	private void updateStatus(SimpleGraphics graphics, LineViewBufferSpec<LineViewData> showingText) {
		mNumOfLine = (int)(getHeight() / (mTextSize*1.2*mScale));//todo mScale
		{
			// todo refactaring
			// current Position
			if(!mIsTail||mPositionY > 1) {
				int a = resetAddPositionY();
				if(a != 0){
					mCash += showingText.getNumOfAdd();
					mPositionY = mCash+a;
				} else {
					mCash = 0;
					mPositionY += showingText.getNumOfAdd();				
				}
			}
			showingText.clearNumOfAdd();
		}
		int blankSpace = mNumOfLine / 2;
		if (mPositionY < -(mNumOfLine - blankSpace)) {
			setPositionY(-(mNumOfLine - blankSpace) - 1);
		} else if (mPositionY > (showingText.getNumberOfStockedElement() - blankSpace)) {
			setPositionY(showingText.getNumberOfStockedElement() - blankSpace);
		}
		graphics.setTextSize((int)(mTextSize*mScale));//todo mScale
	}

	private void drawBG(SimpleGraphics graphics) {
		graphics.drawBackGround(mBgColor);
		if(mImage == null) { return;}
		graphics.drawImageAsTile(mImage, 0, 0,getWidth(), getHeight());
	}
	
	@Override
	public boolean onTouchTest(int x, int y, int action) {
		return super.onTouchTest(x, y, action);
	}
}

