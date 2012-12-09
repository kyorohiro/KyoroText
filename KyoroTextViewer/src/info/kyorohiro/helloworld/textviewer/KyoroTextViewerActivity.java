package info.kyorohiro.helloworld.textviewer;

import java.io.File;

import info.kyorohiro.helloworld.android.adapter.SimpleStageForAndroid;
import info.kyorohiro.helloworld.android.base.MainActivity;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivityOpenFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySaveFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCRLFAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCharsetAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCharsetDetectionAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetTextSizeAction;
import info.kyorohiro.helloworld.textviewer.manager.LineViewGroup;
import info.kyorohiro.helloworld.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.textviewer.util.Util;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;


public class KyoroTextViewerActivity extends MainActivity {
	private SimpleStageForAndroid mStage = null;
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
		mStage = new SimpleStageForAndroid(this);
		mStage.getRoot().addChild(mViewerManager);
		int modeForDisableSoftKeyboard = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		getWindow().setSoftInputMode(modeForDisableSoftKeyboard);
		setContentView(mStage);

		setMenuAction(new MainActivityOpenFileAction(mViewerManager));
		setMenuAction(new MainActivitySetCharsetAction(mViewerManager));
		setMenuAction(new MainActivitySetTextSizeAction(mViewerManager));
		setMenuAction(new MainActivitySetCharsetDetectionAction(mViewerManager));		
		setMenuAction(new MainActivitySaveFileAction(mViewerManager));
		setMenuAction(new MainActivitySetCRLFAction(mViewerManager));
		// todo following yaxtuke sigoto
		mViewerManager.setEvent(new A());
	}

	// todo following yaxtuke sigoto
	// guard for editeed text can not be removed
	private class A implements LineViewManager.Event {
		@Override
		public boolean startCombine(SimpleDisplayObject alive, SimpleDisplayObject killtarget) {
			if(killtarget instanceof TextViewer){
				TextViewer target = null;
				target = (TextViewer)killtarget;
				return !target.isGuard();
//				return !target.isEdit();
			}
			else if(killtarget instanceof LineViewGroup) {
				LineViewGroup target = null;
				target = (LineViewGroup)killtarget;
				return !target.isGuard();
//				return !target.isEdit();
			}
			return true;
		}
		
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
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showDialog();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
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
		//
		// �ҏW����Buffer���Ǘ�����N���X��start/stop����^�C�~���O�����߂�B
		KyoroServiceForForgroundApp.startForgroundService(KyoroApplication.getKyoroApplication(), "start");
	}

	@Override
	protected void onPause() {
		super.onPause();
		mStage.stop();
	}

	@Override
	protected void onDestroy() {
		mViewerManager.dispose();
		// �ҏW����Buffer���Ǘ�����N���X��start/stop����^�C�~���O�����߂�B
		KyoroServiceForForgroundApp.stopForgroundService(this, null);
		super.onDestroy();
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
		return new LineViewManager(textSize, screenWidth,screenHeight, screenMargine,(int)Util.inchi2pixel(Util.mm2inchi(22/2)));
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
						mViewerManager.getFocusingTextViewer().readFile(new File(uri.getPath()), true);
					}
				}
			}
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void showDialog() {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setMessage("if delete editing/viewing data ");
		b.setPositiveButton("finish", new P1());
		b.setNegativeButton("back", new P2());
		b.show();
	}

	private class P1 implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			KyoroTextViewerActivity.this.finish();
		}
	}

	private class P2 implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
		}
	}	
}