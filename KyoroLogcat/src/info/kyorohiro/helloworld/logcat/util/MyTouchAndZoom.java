package info.kyorohiro.helloworld.logcat.util;

import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimplePoint;
import info.kyorohiro.helloworld.display.simple.SimpleStage;

public class MyTouchAndZoom extends SimpleDisplayObject {
	private LogcatViewer mViewer = null;

	public MyTouchAndZoom(LogcatViewer viewer) {
		mViewer = viewer;
	}

	private boolean mStartZoom = false;
	private float mStartScale = 0;
	private int mStartTextSize = 0;
	private int mStartDensity = 0;
	private int mStartPosY = 0;
	private int mStartPos = 0;

	private int mY = 0;
	private float mPrevLength = 0;
	private boolean mIsClear = false;

	
	@Override
	public synchronized boolean onTouchTest(int x, int y, int action) {
		SimpleStage stage = SimpleDisplayObject.getStage(this); 
		if(action == MotionEvent.ACTION_UP&& mIsClear) {
			 mIsClear = false;
			return true;
		}

		if(stage != null && stage.isSupportMultiTouch()) {
			SimplePoint[] p = stage.getMultiTouchEvent();
			if(p[0].isVisible() && p[1].isVisible()) {
				int xx = p[0].getX()-p[1].getX();
				int yy = p[0].getY()-p[1].getY();
				 mIsClear = true;
				if(mStartZoom == false) {
					mStartScale = mViewer.getScale();
					mStartTextSize = mViewer.getTextSize();
					mPrevLength = mStartDensity = xx*xx + yy*yy;
					mStartPosY = (p[0].getY()+p[1].getY())/2;
					mStartPos = (int)(mViewer.getPositionY() + (mY-mStartPosY)/(mStartTextSize*mStartScale*1.2));
				}
				mStartZoom = true;
				
				int currentLength = xx*xx + yy*yy;
				if(mPrevLength != 0) {
					float nextScale = mStartScale +=(currentLength-mPrevLength)/(400*400);
					if(nextScale<1.0){
						nextScale = 1.0f;
					}else if(nextScale>6) {
						nextScale =6.0f;
					}
					//android.util.Log.d("kiyohiro", "nextScale="+nextScale);
					// keeping position
					double keepPos = (mY-mStartPosY);
					
					//
					int movePos = (int)(keepPos/(mStartTextSize*nextScale*1.2));
					mAAAAAAAAAAAA = mStartPos-movePos;
					mBBBBBBBBBBBB = nextScale;						
				//}
				}
				mPrevLength = currentLength;
				return true;
			} else {
				mStartZoom = false;
				mPrevLength =0;
			}
		}
		return false;
	}


	private int mAAAAAAAAAAAA = 0;
	private float mBBBBBBBBBBBB = 0;
	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		// TODO Auto-generated method stub
		mY = mViewer.getHeight();
		if(mStartZoom){
		mViewer.setPositionY(mAAAAAAAAAAAA);
		mViewer.setScale(mBBBBBBBBBBBB);
		}
	}
	
}