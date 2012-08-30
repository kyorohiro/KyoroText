package info.kyorohiro.helloworld.textviewer;

import java.io.File;

import info.kyorohiro.helloworld.android.base.MainActivity;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivityOpenFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCharsetAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCharsetDetectionAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetTextSizeAction;
import info.kyorohiro.helloworld.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.textviewer.util.Util;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

public class KyoroTextViewerActivity extends MainActivity {
	private SimpleStage mStage = null;
//	private TextViewer mTextViewer = null;
	private int mViewerWidth = 100;
	private int mViewerHeight = 100;
	@SuppressWarnings("unused")
	private boolean modifyIntent = false; 
	private LineViewManager mViewerManager = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mViewerManager = newTextManager();
		mStage = new SimpleStage(this);
		mStage.getRoot().addChild(mViewerManager);
		int modeForDisableSoftKeyboard = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		getWindow().setSoftInputMode(modeForDisableSoftKeyboard);
		setContentView(mStage);

		setMenuAction(new MainActivityOpenFileAction(mViewerManager));
		setMenuAction(new MainActivitySetCharsetAction(mViewerManager));
		setMenuAction(new MainActivitySetTextSizeAction(mViewerManager));
		setMenuAction(new MainActivitySetCharsetDetectionAction(mViewerManager));
		
		
		mViewerManager.setCircleMenuRadius(
				(int)Util.inchi2pixel(Util.mm2‚‰nchi(22/2)));
	}

	public void startStage() {
		if(mStage != null) {
			mStage.start();
		}
	}

	public void stopStage() {
		if(mStage != null) {
			mStage.stop();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		startStage();
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		startStage();
		return super.onTrackballEvent(event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		startStage();
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		modifyIntent = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		doFileOpenIntentAction();
		mStage.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mStage.stop();
	}

	private int[] getWindowSize(){
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int width = disp.getWidth();
		int height = disp.getHeight();
		return new int[]{width, height};
	}

	private LineViewManager newTextManager() {
		int textSize = KyoroSetting.getCurrentFontSize();
		int[] widthHeight = getWindowSize();
		mViewerWidth = widthHeight[0];
		mViewerHeight = widthHeight[1];
		if (mViewerWidth>mViewerHeight) {
			int t = mViewerWidth;
			mViewerWidth = mViewerHeight;
			mViewerHeight = t;
		}
		int screenMargine = mViewerWidth*1/20;
		int screenWidth = mViewerWidth-screenMargine/2; // mod 2 is my feeling value so design only. 
		int screenHeight = mViewerHeight;
		return new LineViewManager(textSize, screenWidth,screenHeight, screenMargine);
	}

	private void doFileOpenIntentAction() {
		Intent intentFromExteralApplication = getIntent();
		modifyIntent = false;
		try {
			if (intentFromExteralApplication != null) {
				String action = intentFromExteralApplication.getAction();
				if (action != null && Intent.ACTION_VIEW.equals(action)) {
					Uri uri = intentFromExteralApplication.getData();
					if (uri != null) {
						mViewerManager.getFocusingTextViewer().readFile(new File(uri.getPath()));
					}
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}