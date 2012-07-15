package info.kyorohiro.helloworld.display.widget.lineview;

import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimplePoint;
import info.kyorohiro.helloworld.display.simple.SimpleStage;

public class TouchAndZoomForLineView extends SimpleDisplayObject {
	private LineView mLineViewer = null;

	public TouchAndZoomForLineView(LineView viewer) {
		mLineViewer = viewer;
	}

	private boolean mStartZoom = false;
	private float mStartScale = 0;
	private int mStartTextSize = 0;
	private int mStartCenterY = 0;
	private int mStartCenterX = 0;
	private int mStartPosY = 0;
	private float mStartLength = 0;

	private int mY = 0;
	private boolean mIsClear = false;

	
	private int getLength(){
		SimpleStage stage = SimpleDisplayObject.getStage(this); 
		SimplePoint[] p = stage.getMultiTouchEvent();
		int xx = p[0].getX()-p[1].getX();
		int yy = p[0].getY()-p[1].getY();
		return xx*xx + yy*yy;		
	}

	private int getCenterY() {
		SimpleStage stage = SimpleDisplayObject.getStage(this); 		
		SimplePoint[] p = stage.getMultiTouchEvent();
		int ret = (p[0].getY()+p[1].getY())/2;
		return ret;
	}

	private int getCenterX() {
		SimpleStage stage = SimpleDisplayObject.getStage(this); 		
		SimplePoint[] p = stage.getMultiTouchEvent();
		int ret = (p[0].getX()+p[1].getX())/2;
		return ret;
	}

	private boolean doubleTouched() {
		SimpleStage stage = SimpleDisplayObject.getStage(this); 		
		SimplePoint[] p = stage.getMultiTouchEvent();
		if(p[0].isVisible() && p[1].isVisible()) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public synchronized boolean onTouchTest(int x, int y, int action) {
		SimpleStage stage = SimpleDisplayObject.getStage(this); 
		
		if(action == MotionEvent.ACTION_UP&& mIsClear) {
			mIsClear = false;
			return true;
		}

		// multi touch 
		if(stage != null && stage.isSupportMultiTouch()) {
			if(!doubleTouched()) {
				mStartZoom = false;
				mStartLength =0;
				return false;
			}
			mIsClear = true;
			if(mStartZoom == false) {
				mStartZoom = true;
				mStartScale = mLineViewer.getScale();
				mStartTextSize = mLineViewer.getTextSize();
				mStartLength =  getLength();
				mStartCenterX = getCenterX();
				mStartCenterY = getCenterY();
				mStartPosY = (int)(mLineViewer.getPositionY() 
								+ (mY-mStartCenterY)/(mStartTextSize*mStartScale*1.2));
				return true;
			}
			

			int currentLength = getLength();//xx*xx + yy*yy;
			if(mStartLength != 0) {
				float nextScale = mStartScale +=(currentLength-mStartLength)/(400*400);
				if(nextScale<1.0){
					nextScale = 1.0f;
				}else if(nextScale>6) {
					nextScale =6.0f;
				}
				// keeping position
				double keepPos = (mY-mStartCenterY);

				//
				int movePos = (int)(keepPos/(mStartTextSize*nextScale*1.2));
				mScalePositionY = mStartPosY-movePos;
				mCurrentScale = nextScale;						
				//}
			}
			mStartLength = currentLength;
			return true;
		}
		return false;
	}


	private int mScalePositionY = 0;
	private float mCurrentScale = 0;
	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		mY = mLineViewer.getHeight();
		if(mStartZoom){
			mLineViewer.addScalePositionY(mScalePositionY);
			mLineViewer.setScale(mCurrentScale);
		}
	}
	
}