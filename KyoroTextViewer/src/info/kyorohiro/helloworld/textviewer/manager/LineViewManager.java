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
//	private LineViewGroup mRoot = null;
	private static LineViewManager sInstance = null;

	public static LineViewManager getManager(){
		return sInstance;
	}

	//
	// å„Ç≈SingletoneÇ…Ç∑ÇÈÅB
	public LineViewManager(int textSize, int width, int height, int mergine) {
		sInstance = this;
		mWidth = width;
		mHeight = height;
		mTextSize = textSize;
		mMergine = mergine;
		mFocusingViewer = newTextViewr();
		addChild(new LineViewGroup(mFocusingViewer));
//		addChild(mRoot);
	}

	public TextViewer newTextViewr(){
		return new TextViewer(mTextSize, mWidth, mMergine);
	}
	@Override
	public void paint(SimpleGraphics graphics) {
		int len = numOfChild();
		for(int i=0;i<len;i++){
			if(getChild(i) instanceof LineViewGroup){
			getChild(i).setRect(graphics.getWidth(), graphics.getHeight());
			}
		}
		super.paint(graphics);
	}

	public int getMergine() {
		return mMergine;
	}

	public int getTextSize() {
		return mTextSize;
	}

	public void changeFocus(TextViewer textViewer) {
		mFocusingViewer = textViewer;
	}

	public TextViewer getFocusingTextViewer() {
		return mFocusingViewer;
	}

}
