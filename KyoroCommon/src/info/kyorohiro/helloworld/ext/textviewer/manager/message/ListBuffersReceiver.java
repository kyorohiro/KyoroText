package info.kyorohiro.helloworld.ext.textviewer.manager.message;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher.Receiver;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ListBuffers.ListBuffersJob;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

public class ListBuffersReceiver implements Receiver {
	public static ListBuffersJob sTask = null;
	private WeakReference<ListBuffersJob> mTask = null;
	private TextViewer mTarget = null;
	public ListBuffersReceiver(ListBuffersJob task, TextViewer target) {
		sTask = task;
		mTask = new WeakReference<ListBuffersJob>(task);
		mTarget = target;
	}


	@Override
	public String getType() {
		return "list-buffers";
	}

	@Override
	public void onReceived(KyoroString message, String type) {
//		android.util.Log.v("kiyo","rev="+message);
		ListBuffersJob task = mTask.get();

		if(task != null) {
			//task.input(message.toString());
			//task.input(message.getExtra());
			//task.tab(message.getExtra());
			String[] sp = message.getExtra().split(":");
			
			if(sp != null && sp.length >= 1) {
//				android.util.Log.v("kiyo","rev--1--"+sp[0]);
				int pos = Integer.parseInt(sp[0]);
//				android.util.Log.v("kiyo","rev--2--"+sp[0]);
//				BufferManager.getManager().otherWindow();
				TextViewer target = BufferManager.getManager().getBufferList().getTextViewer(pos);
//				target.dispose();
//				TextViewer viewer = BufferManager.getManager().newTextViewr();
				try {
//					android.util.Log.v("kiyo","rev--3--"+target.getCurrentPath());
//					viewer.readFile(new File(target.getCurrentPath()), false);
					change(target.getCurrentPath());
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				android.util.Log.v("kiyo","rev--3--");
			}
		}
	}

	public void change(String path) {
		if(mTarget != null && !mTarget.isDispose()){
			try {
				mTarget.readFile(new File(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
//		BufferManager.getManager().convertTextViewer(newViewer);
	}
}