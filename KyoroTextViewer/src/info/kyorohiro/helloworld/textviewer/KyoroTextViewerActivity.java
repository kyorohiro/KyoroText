package info.kyorohiro.helloworld.textviewer;

import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivity;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivityOpenFileAction;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetFontAction;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.util.CyclingList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class KyoroTextViewerActivity extends MainActivity {
	SimpleStage mStage = null;
	TextViewer mTextViewer = null;

	public KyoroTextViewerActivity() {
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        {
        	WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        	Display disp = wm.getDefaultDisplay();
        	int width = disp.getWidth();
        	int height = disp.getHeight();
        	int viewerWidth = width;
        	int viewerHeight = height;
        	if(viewerWidth>viewerHeight){
        		int t = viewerWidth;
        		viewerWidth = viewerHeight;
        		viewerHeight = t;
        	}
            mTextViewer = new TextViewer(12, viewerWidth*9/10);
        	mTextViewer.setRect(viewerWidth, viewerHeight);
        }
        mStage = new SimpleStage(this);
        mStage.getRoot().addChild(mTextViewer);
        setContentView(mStage);

		setMenuAction(new MainActivityOpenFileAction(mTextViewer));
		setMenuAction(new MainActivitySetFontAction(mTextViewer));

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


}