package info.kyorohiro.helloworld.edit;

import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndMoveActionForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndZoomForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.EditableLineView.EditableLineViewBuffer;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

public class KyoroEditActivity extends Activity {
    private SimpleStage mStage = null;
    private EditableLineView mEdit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStage = new SimpleStage(this);
//        mViewer = new SimpleEdit();
//       mViewer.setRect(400, 400);
//       mStage.getRoot().addChild(mViewer);
        
        mEdit = new EditableLineView(new LineViewBufferSpec(){
			@Override
			public int getNumOfAdd() {return 0;}
			@Override
			public void clearNumOfAdd() {}
			@Override
			public LineViewData get(int i) {
				return new LineViewData("", Color.BLUE, LineViewData.INCLUDE_END_OF_LINE);
			}
			@Override
			public int getNumberOfStockedElement() {return 0;}
			@Override
			public LineViewData[] getElements(LineViewData[] ret, int start,int end) {
				for(int i=start;i<end;i++){
					ret[i] = get(i);
				}
				return ret;
			}
			@Override
			public BreakText getBreakText() {
				return new MyBreakText();
			}
        });
        mEdit.setRect(400, 400);
        mStage.getRoot().addChild(new TouchAndMoveActionForLineView(mEdit));
        mStage.getRoot().addChild(new TouchAndZoomForLineView(mEdit));
        mStage.getRoot().addChild(mEdit);
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