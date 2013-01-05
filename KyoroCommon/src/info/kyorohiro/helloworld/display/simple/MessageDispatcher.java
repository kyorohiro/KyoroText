package info.kyorohiro.helloworld.display.simple;

import java.util.LinkedList;
import java.util.WeakHashMap;

//
// 依存関係を無視して、
//
public class MessageDispatcher {
	private static MessageDispatcher sInstance = new MessageDispatcher();
	private MessageDispatcher() {
	}

	public static MessageDispatcher getInstance() {
		if(sInstance == null) {
			sInstance = new MessageDispatcher();
		}
		return sInstance;
	}

//	private LinkedList<Receiver> mReceivers = new LinkedList<Receiver>();
	private WeakHashMap<String, Receiver> mReveivers = new WeakHashMap<String, Receiver>();
	public synchronized void send(String message, String type) {
		Receiver r = mReveivers.get(type);
		if(r != null) {
			r.onReceived(message, type);
		}
	}

	public void addReceiver(Receiver r) {
		mReveivers.put(r.getType(), r);
	}

	public void remove(Receiver r) {
		Receiver t = mReveivers.get(r.getType());
		if(t!=null&&t==r) {
			mReveivers.remove(r.getType());
		}
	}
	public static interface Receiver {
		public String getType();
		public void onReceived(String message, String type);
	}
}
