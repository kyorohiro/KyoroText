package info.kyorohiro.helloworld.logcat.util;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimplePoint;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import android.view.MotionEvent;

public class MyTouchAndMove extends SimpleDisplayObject {
	
	private int mPrevX = 0;
	private int mPrevY = 0;
	private long mPrevTime = 0;
	private int mMovingX = 0;
	private int mMovingY = 0;
	private int mHeavyX = 0;
	private int mHeavyY = 0;
	private int mPowerX = 0;
	private int mPowerY = 0;
	private int mPower_prevX = 0;
	private int mPower_prevY = 0;
	private long mPower_time = 0;
	private LogcatViewer mViewer = null;

	public MyTouchAndMove(LogcatViewer viewer) {
		mViewer = viewer;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		if(mHeavyX<-5||mHeavyX>5) {
			mHeavyX /=1.2;
			//int textSize = mViewer.getTextSize();
			mViewer.setPositionX(mViewer.getPositionX() + mHeavyX/*/textSize*/);
		}
		if(mHeavyY<-5||mHeavyY>5) {
			mHeavyY /=1.2;
			int textSize = (int)(mViewer.getTextSize()*mViewer.getScale());// todo 2.5f
			//mViewer.getTextSize();
			mViewer.setPositionY(mViewer.getPositionY() + mHeavyY/textSize);
		}
		
		//todo
		mY = graphics.getHeight();
	}

	private int mY = 0;
	private float mPrevLength = 0;

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		

		SimpleStage stage = SimpleDisplayObject.getStage(this); 
		if(stage != null && stage.isSupportMultiTouch()) {
			SimplePoint[] p = stage.getMultiTouchEvent();
			//
			// todo modify
			if(p[0].isVisible() && p[1].isVisible()) {
				int xx = p[0].getX()-p[1].getX();
				int yy = p[0].getY()-p[1].getY();
				
				int currentLength = xx*xx + yy*yy;
				if(mPrevLength != 0) {
					float s = mViewer.getScale()+(currentLength-mPrevLength)/60000;
					if(s<1.0){
						s = 1.0f;
					}
					if(s>6) {
						s =6.0f;
					}			
					mViewer.setScale(s);
				}
				mPrevLength = currentLength;
				return true;
			} else {
				mPrevLength =0;
			}
		}
		
		
		
		if(mPrevY == -999 || mPrevX == -999) {
			mPrevY = y;
			mPrevX = x;
			mPrevTime = System.currentTimeMillis();
			return false;
		}

		if(action == MotionEvent.ACTION_MOVE) {
			mHeavyX = 0;
			mMovingY += y-mPrevY;
			mPrevY = y;
			mMovingX += x-mPrevX;
			mPrevX = x;
			mPrevTime = System.currentTimeMillis();
			updateMovePower(x, y);
			int textSize = (int)(mViewer.getTextSize()*2.5f);// todo 2.5f
			boolean ret = false; 
			if(mMovingY<textSize || textSize<mMovingY) {
				int notMuchValue = mMovingY%textSize;
				mViewer.setPositionY(mViewer.getPositionY() + (mMovingY-notMuchValue)/textSize);
				mMovingY = notMuchValue;
				ret = true;
			}
			
			if(mMovingX<textSize || textSize<mMovingX) {
				int notMuchValue = mMovingY%textSize;
				mViewer.setPositionX(mViewer.getPositionX() + (mMovingX-notMuchValue));
				mMovingX = notMuchValue;
				ret = true;
			}
			return ret;
		}
		else if(action == MotionEvent.ACTION_DOWN) {
			mHeavyX = 0;
			mMovingY = 0;
			mPrevTime = 0;
		}
		else if(action == MotionEvent.ACTION_UP) {
			mHeavyX = mPowerX*8;
			mHeavyY = mPowerY*8;
			mPrevY = -999;
			mPrevX = -999;
			mMovingY = 0;
			mMovingX = 0;
			mPrevTime = 0;
		}

		return false;
	}

	public void updateMovePower(int x, int y) {
		if(mPower_time <0) {
			mPower_time = System.currentTimeMillis();
			mPower_prevY = y;
			mPower_prevX = x;
		} else {
			//int tmp = (int)(System.currentTimeMillis()-mPower_time);
			//if(tmp<100) {
			//	return;
			//}
			//mPower_time = 0;//System.currentTimeMillis();
			//mPower_time = y;
			mPowerY = y -mPower_prevY;
			mPower_prevY = y;
			mPowerX = x -mPower_prevX;
			mPower_prevX = x;
		}
	}
	
}