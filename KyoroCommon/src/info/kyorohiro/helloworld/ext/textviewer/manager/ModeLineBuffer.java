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
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ModeLineTask;
import info.kyorohiro.helloworld.text.KyoroString;

// now creating 
public class ModeLineBuffer extends TextViewer {

	public static final String MODE_LINE_BUFFER = CursorableLineView.MODE_EDIT+"mode_line_buffer";
	private ModeLineTask mTask = null;

	public ModeLineBuffer(int textSize, int width, int mergine, boolean message) {
		super(new EmptyLineViewBufferSpecImpl(400, BufferManager.getManager().getFont()),textSize, width, mergine,
				BufferManager.getManager().getFont(),//new SimpleFontForAndroid(),
				BufferManager.getManager().getCurrentCharset());
		//android.util.Log.v("kiyo","new");
		if(BufferManager.getManager().currentBrIsLF()){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		getLineView().setConstantMode(MODE_LINE_BUFFER);
	}

	public boolean isEmptyTask() {
		if(mTask == null) {
		//	android.util.Log.v("kiyo","true");
			return true;
		} else {
		//	android.util.Log.v("kiyo","false");
			return false;
		}
	}
	@Override
	public boolean readFile(File file, boolean updataCurrentPath)
			throws FileNotFoundException, NullPointerException {
		return false;
	}

	public void done() {
		ModeLineTask task = mTask;
		if(task !=null) {
			KyoroString text = this.getLineView().getKyoroString(0);
			if(text != null) {
				task.enter(text.toString());
			}
			task.end();
			//todo
			mTask = null;
			if(getLineView() != null) {
				getLineView().clear();
			}
			// 
			// todo refactring target
//			endTask();
		}
		hideModeLine();
	}

	public void hideModeLine() {
		Object o = getParent();
//		android.util.Log.v("kiyo","-----A");
		if(o instanceof BufferGroup){
//			android.util.Log.v("kiyo","-----B");
			((BufferGroup)o).setSeparatorPoint(0.2f);
		}
//		android.util.Log.v("kiyo","-----C");
	}

	public void startModeLineTask(ModeLineTask task) {
		mTask = task;
		mTask.begin();
	}

	public Thread mCurrentTask = null;
	public void endTask() {
		//android.util.Log.v("kiyo","endTask");
		if(mCurrentTask !=null && mCurrentTask.isAlive()) {
			mCurrentTask.interrupt();
			mCurrentTask = null;
		}
		mCurrentTask = null;
		mTask = null;
		if(getLineView() != null) {
			getLineView().clear();
		}
	}
	public void startTask(Runnable task) {
		//android.util.Log.v("kiyo","startTask");
		if(task == null) {
			endTask();
			return;
		}
		if(mCurrentTask !=null && mCurrentTask.isAlive()) {
			mCurrentTask.interrupt();
			mCurrentTask = null;
		}
		mCurrentTask = new Thread(task);
		mCurrentTask.start();
	}
	
}
