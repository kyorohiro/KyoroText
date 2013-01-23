package info.kyorohiro.helloworld.ext.textviewer.manager.message;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher.Receiver;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile.FindFileJob;
import info.kyorohiro.helloworld.text.KyoroString;

import java.lang.ref.WeakReference;

public class FindFileReceiver implements Receiver {
	public final static String TYPE = "find";
	public static FindFileJob sTask = null;
	private WeakReference<FindFileJob> mTask = null;
	public FindFileReceiver(FindFileJob task) {
		sTask = task;
		mTask = new WeakReference<FindFileJob>(task);
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public void onReceived(KyoroString message, String type) {
		FindFileJob task = mTask.get();
		if(!task.isAlive()){
			return;
		}
		if(task != null) {
			task.input(message.getExtra());
		}
	}
}
