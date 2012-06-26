package info.kyorohiro.helloworld.edit;

import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.SimpleEdit;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class KyoroEditActivity extends Activity {
    private SimpleStage mStage = null;
    private SimpleEdit mViewer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStage = new SimpleStage(this);
        mViewer = new SimpleEdit();
        mViewer.setRect(400, 400);
        
        mStage.getRoot().addChild(mViewer);
		int modeForDisableSoftKeyboard = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        getWindow().setSoftInputMode(modeForDisableSoftKeyboard);
        setContentView(mStage);
    }

    @Override
    protected void onStart() {
    	super.onStart();
    	mStage.start();
    }

    @Override
    protected void onStop() {
    	super.onStop();
    	mStage.stop();
    }
    
}