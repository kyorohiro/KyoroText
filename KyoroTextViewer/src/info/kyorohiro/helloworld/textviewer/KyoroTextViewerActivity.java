package info.kyorohiro.helloworld.textviewer;

import java.io.File;

import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleFontForAndroid;
import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleStageForAndroid;
import info.kyorohiro.helloworld.pfdep.android.base.MainActivity;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivityNewShellBufferAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivityOpenFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySaveFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySaveasFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCRLFAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCharsetAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCharsetDetectionAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetColorAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetTextSizeAction;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferGroup;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.AppDependentAction;
import info.kyorohiro.helloworld.textviewer.task.CopyTask;
import info.kyorohiro.helloworld.textviewer.task.PastTask;
import info.kyorohiro.helloworld.textviewer.util.Util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class KyoroTextViewerActivity extends MainActivity {
	private SimpleStageForAndroid mStage = null;
	// private TextViewer mTextViewer = null;
	private int mViewerWidth = 100;
	private int mViewerHeight = 100;
	@SuppressWarnings("unused")
	private boolean modifyIntent = false;
	private BufferManager mViewerManager = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//
		mViewerManager = newTextManager();
		mStage = new SimpleStageForAndroid(this);
		mStage.getRoot().addChild(mViewerManager);
		int modeForDisableSoftKeyboard = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
				| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		getWindow().setSoftInputMode(modeForDisableSoftKeyboard);

		LinearLayout layout = new LinearLayout(this);
		layout.addView(mStage);
		setContentView(layout);
		// setContentView(mStage);
		setMenuAction(new MainActivitySetCharsetDetectionAction(mViewerManager));
		setMenuAction(new MainActivityOpenFileAction(mViewerManager));
		setMenuAction(new MainActivitySaveFileAction(mViewerManager));
		setMenuAction(new MainActivitySaveasFileAction(mViewerManager));
		setMenuAction(new MainActivityNewShellBufferAction(mViewerManager));
		setMenuAction(new MainActivitySetTextSizeAction(mViewerManager));
		setMenuAction(new MainActivitySetCharsetAction(mViewerManager));
		setMenuAction(new MainActivitySetCRLFAction(mViewerManager));
		setMenuAction(new MainActivitySetColorAction(mViewerManager));

		// todo following yaxtuke sigoto
		mViewerManager.setEvent(new A());
	}

	// todo following yaxtuke sigoto
	// guard for editeed text can not be removed
	private class A implements BufferManager.Event {
		@Override
		public boolean startCombine(SimpleDisplayObject alive,
				SimpleDisplayObject killtarget) {
			if (killtarget instanceof TextViewer) {
				TextViewer target = null;
				target = (TextViewer) killtarget;
				return !target.isGuard();
				// return !target.isEdit();
			} else if (killtarget instanceof BufferGroup) {
				BufferGroup target = null;
				target = (BufferGroup) killtarget;
				return !target.isGuard();
				// return !target.isEdit();
			}
			return true;
		}
	}

	public static class MyBuilder extends AppDependentAction {
		@Override
		public SimpleFont newSimpleFont() {
			return new SimpleFontForAndroid();
		}

		public void copyStart() {
			CopyTask.copyStart();
		}

		@Override
		public void pastStart() {
			PastTask.pasteStart();
		}

		@Override
		public File getFilesDir() {
			return KyoroApplication.getKyoroApplication()
					.getApplicationContext().getFilesDir();
		}

		@Override
		public boolean currentBrIsLF() {
			if (KyoroSetting.VALUE_LF.equals(KyoroSetting.getCurrentCRLF())) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String getCurrentCharset() {
			return KyoroSetting.getCurrentCharset();
		}

		@Override
		public void setCurrentFile(String path) {
			KyoroSetting.setCurrentFile(path);
		}
	}

	public void startStage() {
		if (mStage != null) {
			mStage.start();
		}
	}

	public void stopStage() {
		if (mStage != null) {
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
		if (keyCode == KeyEvent.KEYCODE_BACK) {
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
		KyoroServiceForForgroundApp.startForgroundService(
				KyoroApplication.getKyoroApplication(), "start");
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

	private int[] getWindowSize() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int width = disp.getWidth();
		int height = disp.getHeight();
		return new int[] { width, height };
	}

	private BufferManager newTextManager() {
		int textSize = KyoroSetting.getCurrentFontSize();
		int[] widthHeight = getWindowSize();
		mViewerWidth = widthHeight[0];
		mViewerHeight = widthHeight[1];
		if (mViewerWidth > mViewerHeight) {
			int t = mViewerWidth;
			mViewerWidth = mViewerHeight;
			mViewerHeight = t;
		}
		int screenMargine = mViewerWidth * 1 / 20;
		int screenWidth = mViewerWidth - screenMargine / 2; // mod 2 is my
															// feeling value so
															// design only.
		int screenHeight = mViewerHeight;

		int circleSize = 18 / 2;
		if (60 < Util.inchi2mm(Util.pixel2inchi(mViewerWidth))) {
			circleSize = 22 / 2;
		} else {
			circleSize = 18/ 2;
		} 
		int baseTextSize = (int) Util.inchi2pixel(Util.mm2inchi(1.6));
		// color
		if(KyoroSetting.DEFAULT_COLOR_MOONLIGHT.equals(KyoroSetting.getCurrentColor())){
			BufferManager.setMoonLight();			
		} else {
			BufferManager.setSnowLight();
		}
		return new BufferManager(KyoroApplication.getKyoroApplication(),
				new MyBuilder(), baseTextSize, textSize, screenWidth,
				screenHeight, screenMargine, (int) Util.inchi2pixel(Util
						.mm2inchi(circleSize)));
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
						TextViewer fo = mViewerManager.getFocusingTextViewer();
						if (fo instanceof MiniBuffer) {
							mViewerManager.otherWindow();
							fo = mViewerManager.getFocusingTextViewer();
						}
						mViewerManager.getFocusingTextViewer().readFile(
								new File(uri.getPath()));
					}
				}
			}
		} catch (Throwable t) {
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