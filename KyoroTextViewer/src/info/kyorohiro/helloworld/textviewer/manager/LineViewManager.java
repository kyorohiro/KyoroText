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
	private int mTextSize = 16;
	private int mMergine = 10;
	private TextViewer mFocusingViewer = null;

	public LineViewManager(int textSize, int width, int height, int mergine) {
		mWidth = width;
		mHeight = height;
		mTextSize = textSize;
		mMergine = mergine;
		mFocusingViewer = new TextViewer(textSize, width, mergine);
		addChild(mFocusingViewer);
		addChild(new SeparateUI(this));
	}
	
	public TextViewer getFocusingTextViewer() {
		return mFocusingViewer;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		int y=0;
		for(int i=0;i<numOfChild();i++) {
			if(getChild(i) instanceof TextViewer) {
				getChild(i).setPoint(0, y);
				getChild(i).setRect(graphics.getWidth(), graphics.getHeight()/(numOfChild()-1));
				y +=graphics.getHeight()/(numOfChild()-1);
			}
		}
		super.paint(graphics);
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if(MotionEvent.ACTION_DOWN == action){
			focusTest(x, y);
		}
		return super.onTouchTest(x, y, action);
	}

	private void focusTest(int x, int y) {
		for(int i=0;i<numOfChild();i++) {
			if(getChild(i) instanceof TextViewer) {
				int cx = ((TextViewer)getChild(i)).getX();
				int cy = ((TextViewer)getChild(i)).getY();
				int cw = ((TextViewer)getChild(i)).getWidth();
				int ch = ((TextViewer)getChild(i)).getHeight();
				if(cx<x&&x<cx+cw) {
					if(cy<y&&y<cy+ch){
						mFocusingViewer = (TextViewer)getChild(i);
						break;
					}
				}
			}
		}		
	}

	public void divide(SeparateUI separate) {
		addChild(new TextViewer(mTextSize, mWidth, mMergine));
	}

	public static class SeparateUI extends SimpleDisplayObject {
		public static int COLOR = Color.GREEN;
		private int mPrevTouchDownX = 0;
		private int mPrevTouchDownY = 0;
		private int mPrevX = 0;
		private int mPrevY = 0;
		private boolean mIsInside = false;
		private boolean mIsReached = false;
		private LineViewManager mManager = null;

		public SeparateUI(LineViewManager manager) {
			setRect(20, 20);
			mManager = manager;
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
					int[] xy = new int[2];
					getGlobalXY(xy);
					setPoint(xy[0]+x-mPrevTouchDownX+mPrevX, xy[1]+y-mPrevTouchDownY+mPrevY);
					
					if(!mIsReached) {
						if(isReached(x, y)){
							mIsReached = true;
							mManager.divide(this);
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

}
