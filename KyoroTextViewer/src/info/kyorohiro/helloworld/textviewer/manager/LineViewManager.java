package info.kyorohiro.helloworld.textviewer.manager;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

import android.graphics.Color;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;


//
// first step 
//   show TextViewer under LineViewManager
//
public class LineViewManager extends SimpleDisplayObjectContainer {
	private int mWidth = 100;
	private int mHeight = 100;
	private TextViewer mFocusingViewer = null;

	public LineViewManager(int textSize, int width, int height, int mergine) {
		mWidth = width;
		mHeight = height;
		mFocusingViewer = new TextViewer(textSize, width, mergine);
		addChild(mFocusingViewer);
		addChild(new SeparateUI());
	}
	
	public TextViewer getFocusingTextViewer() {
		return mFocusingViewer;
	}


	public static class SeparateUI extends SimpleDisplayObject {
		public static int COLOR = Color.GREEN;
		private int mPrevTouchDownX = 0;
		private int mPrevTouchDownY = 0;
		private int mPrevX = 0;
		private int mPrevY = 0;
		private boolean mIsInside = false;

		public SeparateUI() {
			setRect(20, 20);
		}

		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.setColor(COLOR);
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
					//this.setPoint(x-mPrevTouchDownX,y-mPrevTouchDownY);
					int[] xy = new int[2];
					getGlobalXY(xy);
					android.util.Log.v("kiyo","xy="+xy[0]+","+xy[1]);
					setPoint(xy[0]+x-mPrevTouchDownX+mPrevX, xy[1]+y-mPrevTouchDownY+mPrevY);
					return true;
				}
			} else if(action == MotionEvent.ACTION_UP){
				mIsInside = false;				
			}
			return super.onTouchTest(x, y, action);
		}

		private boolean isInside(int x, int y) {
			SimpleDisplayObject target = this;
			int width = target.getWidth()/2;
			int height = target.getWidth()/2;
			boolean isInsideAboutH = false;
			boolean isInsideAboutW = false;
			
			if(-width<x&& width<x){
				isInsideAboutW = true;
			}
			if(-height<y&& height<y){
				isInsideAboutH = true;
			}
			return isInsideAboutW&isInsideAboutW;
		}
	}

}
