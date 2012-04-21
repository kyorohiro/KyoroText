package info.kyorohiro.helloworld.textviewer;

import java.io.File;

import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivity;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivityOpenFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetFontAction;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class KyoroTextViewerActivity extends MainActivity {
	private SimpleStage mStage = null;
	private TextViewer mTextViewer = null;
	private int mViewerWidth = 100;
	private int mViewerHeight = 100;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mTextViewer = newTextViewer();
		mTextViewer.setRect(mViewerWidth, mViewerHeight);
		mStage = new SimpleStage(this);
		mStage.getRoot().addChild(mTextViewer);


		int modeForDisableSoftKeyboard = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		getWindow().setSoftInputMode(modeForDisableSoftKeyboard);
		setContentView(mStage);

		setMenuAction(new MainActivityOpenFileAction(mTextViewer));
		setMenuAction(new MainActivitySetFontAction(mTextViewer));

		doFileOpenIntentAction();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mStage.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mStage.stop();
	}

	private TextViewer newTextViewer() {
		WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();
		int width = disp.getWidth();
		int height = disp.getHeight();
		mViewerWidth = width;
		mViewerHeight = height;
		if(mViewerWidth>mViewerHeight){
			int t = mViewerWidth;
			mViewerWidth = mViewerHeight;
			mViewerHeight = t;
		}
		return new TextViewer(12, mViewerWidth*9/10);
	}

	private void doFileOpenIntentAction() {
		Intent intentFromExteralApplication = getIntent();
		try{
			if(intentFromExteralApplication != null){
				String action = intentFromExteralApplication.getAction();
				Uri uri;
				if(action != null && Intent.ACTION_VIEW.equals(action)){
					uri = intentFromExteralApplication.getData();
					if(uri != null){
						mTextViewer.readFile(new File(uri.getPath()));
					}
				}
			}
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
}