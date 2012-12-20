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

	public synchronized void waitForTask() {
		if (mIsAlive) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
