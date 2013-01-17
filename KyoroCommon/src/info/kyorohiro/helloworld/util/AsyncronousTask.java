package info.kyorohiro.helloworld.util;

public class AsyncronousTask implements Runnable {
	private boolean mIsAlive = false;
	private Runnable mTask = null;

	public AsyncronousTask(Runnable task) {
		mIsAlive = true;
		mTask = task;
	}

	public void run() {
		try {
			mTask.run();
			
		} finally {
			mIsAlive = false;
			awake();
		}
	}

	public synchronized void awake() {
		notifyAll();
	}

	public synchronized boolean waitForTask() {
		if (mIsAlive) {
			try {
//				android.util.Log.v("kiyo","-------1------");
				wait();
//				android.util.Log.v("kiyo","-------2------");
				return true;
			} catch (InterruptedException e) {
//				android.util.Log.v("kiyo","------------e------");
				e.printStackTrace();
				return false;
			}
//			android.util.Log.v("kiyo","-------3------");
		} else {
			return true;
		}
	}
}
