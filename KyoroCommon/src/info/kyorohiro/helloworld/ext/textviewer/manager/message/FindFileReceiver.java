package info.kyorohiro.helloworld.ext.textviewer.manager.message;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher.Receiver;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile.FindFileJob;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;

import java.lang.ref.WeakReference;

public class FindFileReceiver implements Receiver {
	public final static String TYPE = "find";
	public static FindFileJob sTask = null;
	public static FindFileReceiver sReceiver = null;
//	private WeakReference<FindFileJob> mTask = null;

	private FindFileReceiver(FindFileJob task) {
		sTask = task;
//		mTask = new WeakReference<FindFileJob>(task);
	}

	public static FindFileReceiver newFindFileReceiver(FindFileJob task) {
		sReceiver = new FindFileReceiver(task);
		return sReceiver;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void onReceived(KyoroString message, String type) {
//		android.util.Log.v("kiy","onRe,"+type+","+message);
		FindFileJob task = sTask;//mTask.get();
		if(task == null) {
			return;
		}
		if(!task.isAlive()) {
			TextViewer info = BufferManager.getManager().getInfoBuffer();
			if(info == null || info.isDispose()) {
				return;
			} else {
				task.asisSetTextViewer(info);
			}
		}

		if(task != null) {
			task.input(message.getExtra());
		}
	}

}
