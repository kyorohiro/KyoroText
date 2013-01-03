package info.kyorohiro.helloworld.ext.textviewer.manager.task;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferGroup;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

public class OtherWindowTask implements Runnable {
	@Override
	public void run() {
		otherWindow();
	}
	//
	// following code otherWindow must to be moving another class
	//　
	public void otherWindow() {
	//	android.util.Log.v("kiyo","----otherWindow");
		TextViewer v = BufferManager.getManager().getFocusingTextViewer();
		if(v.getParent() instanceof BufferGroup) {
			int i = ((BufferGroup)v.getParent()).getIndex(v);
			TextViewer f = otherWindow(v.getParent(), i+1);
			if(f!=null) {
				BufferManager.getManager().changeFocus(f);
			}
		}
		if(BufferManager.getManager().getFocusingTextViewer() == BufferManager.getManager().getModeLineBuffer()&& BufferManager.getManager().getModeLineBuffer().isEmptyTask()) {
			otherWindow();
		}
		//android.util.Log.v("kiyo","----/otherWindow");
	}

	private  TextViewer otherWindow(Object v, int index) {
		//android.util.Log.v("kiyo","----otherWindow -1-:" +index);
		if(v instanceof BufferGroup) {
			//android.util.Log.v("kiyo","----otherWindow -1-2:");
			return otherWindow((BufferGroup)v, index);
		} else if(v instanceof TextViewer) {
			//android.util.Log.v("kiyo","----otherWindow -1-3:");
			return (TextViewer)v;
		} else {
			//android.util.Log.v("kiyo","----otherWindow -1-4:");
			return otherWindow(BufferManager.getManager().getRoot(), 0);
		}
	}
	private TextViewer otherWindow(BufferGroup v, int index) {
		//android.util.Log.v("kiyo","----otherWindow -2-" +index+","+v.numOfChild());
		for(int i=index;i<v.numOfChild();i++) {
			if(v.getChild(i) instanceof TextViewer) {
			//	android.util.Log.v("kiyo","----otherWindow -2-1 " +i);
				return (TextViewer)v.getChild(i);
			} else if(v.getChild(i) instanceof BufferGroup) {
				//android.util.Log.v("kiyo","----otherWindow -2-2 " +i);
				return otherWindow(v.getChild(i), 0);
			}
		}
		if(v instanceof SimpleDisplayObjectContainer){
			int j = ((SimpleDisplayObjectContainer)v.getParent()).getIndex(v);
			//android.util.Log.v("kiyo","----otherWindow -3-"+j);
			return otherWindow(v.getParent(), j+1);
		} else {
		//	android.util.Log.v("kiyo","----otherWindow -4-");
			return otherWindow(this, 0);
		}
	}
}
