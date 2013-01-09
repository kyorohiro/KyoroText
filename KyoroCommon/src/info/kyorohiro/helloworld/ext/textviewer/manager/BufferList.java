package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

//
//
//
//
public class BufferList {
	private LinkedList<TextViewer> mList = new LinkedList<TextViewer>();

	public void add(TextViewer viewer) {
//		android.util.Log.v("kiyo","--add--"+viewer.getCurrentPath());
		mList.add(viewer);
	}

	public int size () {
		return mList.size();
	}

	public TextViewer getTextViewer(int i) {
		return mList.get(i);
	}
	public synchronized void lockAll() {
		for(int i=0;i<mList.size();i++) {
			TextViewer c = mList.get(i);
			if(!c.isDispose()) {
				c.getLineView().isLockScreen(true);
			}
		}		
	}

	public synchronized void unlockAll() {
		for(int i=0;i<mList.size();i++) {
			TextViewer c = mList.get(i);
			if(!c.isDispose()) {
				c.getLineView().isLockScreen(false);
			}
		}		
	}

	public synchronized void updateFileName(File currentPath, File backupPath) {
		String path = currentPath.getAbsolutePath();
		if(backupPath != null) {
			for(int i=0;i<mList.size();i++) {
				TextViewer c = mList.get(i);
				if(!c.isDispose()&&backupPath.equals(""+c.asisGetBufferPath().getAbsolutePath())){
					try {
						c.asisChangeBufferPath(backupPath);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public synchronized void doGrabage() {
		LinkedList<TextViewer> grabage = new LinkedList<TextViewer>();
		for(int i=0;i<mList.size();i++) {
			TextViewer c = mList.get(i);
			if(c.isDispose()) {
				grabage.add(c);
			}
		}
		for(int i=0;i<grabage.size();i++) {
			mList.remove(grabage.get(i));
		}
	}
}
