package info.kyorohiro.helloworld.textviewer.manager;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import android.graphics.Color;
import android.view.MotionEvent;

public class SeparateUI extends SimpleDisplayObject {
	public static int COLOR_UNLOCK = Color.GREEN;
	public static int COLOR_LOCK = Color.RED;
	private int mPrevTouchDownX = 0;
	private int mPrevTouchDownY = 0;
	private int mPrevX = 0;
	private int mPrevY = 0;
	private boolean mIsInside = false;
	private boolean mIsReached = false;
	private LineViewGroup mManager = null;

	public SeparateUI(LineViewGroup manager) {
		setRect(20, 20);
		mManager = manager;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		if(mIsReached){
			graphics.setColor(COLOR_LOCK);			
		} else {
			graphics.setColor(COLOR_UNLOCK);		
		}
		graphics.drawCircle(0, 0, getWidth()/2);
		graphics.drawLine(0, 0, -1000, 0);
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if(action == MotionEvent.ACTION_DOWN){
			if(isInside(x, y)){
				mIsInside = true;
				int[] xy = new int[2];
				getGlobalXY(xy);
				mPrevTouchDownX = xy[0]+x;
				mPrevTouchDownY = xy[1]+y;
				mPrevX = getX();
				mPrevY = getY();
				return true;
			}
		} else if(action == MotionEvent.ACTION_MOVE) {
			if(mIsInside){
				int[] xy = new int[2];
				getGlobalXY(xy);
				setPoint(xy[0]+x-mPrevTouchDownX+mPrevX, xy[1]+y-mPrevTouchDownY+mPrevY);
				
				if(!mIsReached) {
					if(isReached(x, y)){
						mIsReached = true;
						mManager.divide(this);
					}
				} else {
					if(isUnreached(x, y)){
						mIsReached = false;
						mManager.combine(this);
					}
				}
				return true;
			}
		} else if(action == MotionEvent.ACTION_UP){
			mIsInside = false;				
		}
		return super.onTouchTest(x, y, action);
	}

	private boolean isReached(int x, int y) {
		SimpleDisplayObject target = this;
		SimpleDisplayObject parent = (SimpleDisplayObject)getParent();
		int a = parent.getWidth()-(x+getX()+target.getWidth()*4);
		if(a<0) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isUnreached(int x, int y) {
		SimpleDisplayObject target = this;
		SimpleDisplayObject parent = (SimpleDisplayObject)getParent();
		int a = (x+getX()-target.getWidth()*4);
		if(a<0) {
			return true;
		} else {
			return false;
		}
	}


	private boolean isInside(int x, int y) {
		SimpleDisplayObject target = this;
		int width = target.getWidth()/2;
		int height = target.getWidth()/2;
		boolean isInsideAboutH = false;
		boolean isInsideAboutW = false;
		width *=2;
		height *=2;
		
		if(-width<x&& x<width){
			isInsideAboutW = true;
		}
		if(-height<y&& y<height){
			isInsideAboutH = true;
		}
		return isInsideAboutW&isInsideAboutH;
	}

}
