package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

//import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleFontForAndroid;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.sample.SimpleSwitchButton;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ModeLineTask;
import info.kyorohiro.helloworld.text.KyoroString;

// now creating 
public class ModeLineBuffer extends TextViewer {

	public static final String MODE_LINE_BUFFER = CursorableLineView.MODE_EDIT+"mode_line_buffer";
	private ModeLineTask mTask = null;

	public ModeLineBuffer(int textSize, int width, int mergine, boolean message) {
		super(new EmptyLineViewBufferSpecImpl(400, LineViewManager.getManager().getFont()),textSize, width, mergine,
				LineViewManager.getManager().getFont(),//new SimpleFontForAndroid(),
				LineViewManager.getManager().getCurrentCharset());
		if(LineViewManager.getManager().currentBrIsLF()){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		getLineView().setConstantMode(MODE_LINE_BUFFER);
	}

	public boolean isEmptyTask() {
		if(mTask == null) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean readFile(File file, boolean updataCurrentPath)
			throws FileNotFoundException, NullPointerException {
		return false;
	}

	public void done() {
		if(mTask !=null) {
			KyoroString text = this.getLineView().getKyoroString(0);
			if(text != null) {
				mTask.enter(text.toString());
			}
			mTask.end();
			mTask = null;
			this.getLineView().clear();
		}
		hideModeLine();
	}

	public void hideModeLine() {
		Object o = getParent();
		android.util.Log.v("kiyo","-----A");
		if(o instanceof LineViewGroup){
			android.util.Log.v("kiyo","-----B");
			((LineViewGroup)o).setSeparatorPoint(0.2f);
		}
		android.util.Log.v("kiyo","-----C");
	}

	public void startModeLineTask(ModeLineTask task) {
		mTask = task;
		mTask.begin();
	}
	
}
