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

//
//
//
}
