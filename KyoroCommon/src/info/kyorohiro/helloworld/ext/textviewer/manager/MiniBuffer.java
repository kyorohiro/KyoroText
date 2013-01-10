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
import info.kyorohiro.helloworld.display.widget.lineview.sample.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.MiniBufferTask;
import info.kyorohiro.helloworld.text.KyoroString;

// now creating 
public class MiniBuffer extends TextViewer {

	public static final String MODE_LINE_BUFFER = CursorableLineView.MODE_EDIT+" command";
	private MiniBufferTask mTask = null;

	public MiniBuffer(int textSize, int width, int mergine, boolean message) {
		super(new EmptyLineViewBufferSpecImpl(400, BufferManager.getManager().getFont(textSize)),textSize, width, mergine,
				BufferManager.getManager().getFont(textSize),//new SimpleFontForAndroid(),
				BufferManager.getManager().getCurrentCharset());
		//android.util.Log.v("kiyo","new");
		if(BufferManager.getManager().currentBrIsLF()){
			getLineView().isCrlfMode(false);
		} else {
			getLineView().isCrlfMode(true);
		}
		getLineView().setConstantMode(MODE_LINE_BUFFER);

		File a = new File(BufferManager.getManager().getApplication().getApplicationDirectory(),"minibuffer");
		try {
			if(!a.exists()){
				a.createNewFile();
			} else {
				a.delete();
				a.createNewFile();
			}
			super.readFile(a, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void next() {
		MiniBufferTask task = mTask;
		if(task !=null) {
			String text = getString();
			if(text != null) {
				task.tab(text);
			}
		}
	}

	private String getString() {
		StringBuilder b = new StringBuilder();
		for(int i=0;i<this.getLineView().numOfChild();i++) {
			KyoroString text = this.getLineView().getKyoroString(i);
			if(text != null) {
				String tmp = text.toString();
				tmp = tmp.replaceAll("\r\n|\n", "");
				b.append(tmp);
			}
		}
		return b.toString();
	}
	public void done() {
		MiniBufferTask task = mTask;
		if(task !=null) {
			String text = getString();
			if(text != null) {
				task.enter(text);
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

	public void startMiniBufferTask(MiniBufferTask task) {
		if(task != mTask && task != null) {
			mTask = task;
			mTask.begin();
		}
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
