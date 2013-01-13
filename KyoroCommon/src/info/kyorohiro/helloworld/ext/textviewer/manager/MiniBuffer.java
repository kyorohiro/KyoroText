package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import info.kyorohiro.helloworld.display.simple.SimpleFont;
//import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleFontForAndroid;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.sample.SimpleSwitchButton;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.sample.EmptyLineViewBufferSpecImpl;
import info.kyorohiro.helloworld.ext.textviewer.viewer.BufferBuilder;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewerBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.MiniBufferJob;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.SingleTaskRunner;

// now creating 
public class MiniBuffer extends TextViewer {

	public static final String MODE_LINE_BUFFER = CursorableLineView.MODE_EDIT+" command";
	private MiniBufferJob mMiniBufferJob = null;

	public static MiniBuffer newMiniBuffer(BufferManager manager, int textSize, int width, int mergine, boolean message) {
		MiniBuffer ret = null;
		File file = new File(BufferManager.getManager().getApplication().getApplicationDirectory(),"minibuffer");
		BufferBuilder builder = new BufferBuilder(file);
		TextViewerBuffer buffer;
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			buffer = builder.readFile(manager.getFont(), textSize, width);
			ret = new MiniBuffer(manager, buffer, textSize, width, mergine);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	private MiniBuffer(BufferManager manager, TextViewerBuffer buffer, int textSize, int width, int mergine) {
		super(buffer, textSize, width, mergine, manager.getCurrentCharset());
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
		//	super.readFile(a, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean miniBufferBobisEmpty() {
		if(mMiniBufferJob == null) {
		//	android.util.Log.v("kiyo","true");
			return true;
		} else {
		//	android.util.Log.v("kiyo","false");
			return false;
		}
	}


	public void next() {
		MiniBufferJob task = mMiniBufferJob;
		if(task !=null) {
			String text = getString();
			if(text != null) {
				task.tab(text);
			}
		}
	}


	public void done() {
		MiniBufferJob task = mMiniBufferJob;
		if(task !=null) {
			String text = getString();
			if(text != null) {
				task.enter(text);
			}
			task.end();
			//todo
			mMiniBufferJob = null;
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
		if(o instanceof BufferGroup){
			((BufferGroup)o).setSeparatorPoint(0.2f);
		}
	}

	public synchronized void startMiniBufferJob(MiniBufferJob task) {
		if(task != mMiniBufferJob && task != null) {
			mMiniBufferJob = task;
			mMiniBufferJob.begin();
		}
	}


	//
	// MiniBuffer Task Runner
	//
	private SingleTaskRunner mSingleTaskRunner = new SingleTaskRunner();
	public synchronized void startTask(Runnable nextTask) {
		mSingleTaskRunner.startTask(nextTask);
	}

	public synchronized void endTask() {
		mSingleTaskRunner.endTask();
		mMiniBufferJob = null;
		if(getLineView() != null) {
			getLineView().clear();
		}
	}

	
	//
	// utility
	//
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

	
	//
	// TextViewr Func is restrict
	//
	@Override
	public boolean readFile(File file)
			throws FileNotFoundException, NullPointerException {
		return false;
	}

}
