package info.kyorohiro.helloworld.ext.textviewer.manager.message;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher.Receiver;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile.FindFileTask;
import info.kyorohiro.helloworld.text.KyoroString;

import java.lang.ref.WeakReference;

public class FindFileReceiver implements Receiver {
	public final static String TYPE = "find";
	public static FindFileTask sTask = null;
	private WeakReference<FindFileTask> mTask = null;
	public FindFileReceiver(FindFileTask task) {
		sTask = task;
		mTask = new WeakReference<FindFileTask>(task);
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void onReceived(KyoroString message, String type) {
		FindFileTask task = mTask.get();
		if(!task.isAlive()){
			return;
		}
		if(task != null) {
			task.input(message.getExtra());
		}
	}
}
