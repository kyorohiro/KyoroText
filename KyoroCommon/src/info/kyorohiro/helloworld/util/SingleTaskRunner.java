package info.kyorohiro.helloworld.util;


public class SingleTaskRunner {

	private Thread mTaskRunner = null;
	private Runnable mNextTask = null;

	private void log(String message) {
		System.out.println("#SingleTaskRunner#"+message);
	}

	//
	// current running thread is end. and nextTask is run.
	//
	public synchronized void startTask(Runnable nextTask) {
		log("startTask");
		// 
		// this method don't call updateTask method.
		// if you call updateTask. depend on this method caller.
		// 
		mNextTask = nextTask;
		UpdateTaskThread t = new UpdateTaskThread();
		t.start();
	}

	public synchronized void endTask() {
		log("endTask");
		if(mTaskRunner !=null && mTaskRunner.isAlive()) {
			mTaskRunner.interrupt();
			mTaskRunner = null;
		}
		mTaskRunner = null;
	}


	public synchronized void updateTask() {
		log("done 1");
		if(mNextTask != null) {
			updateTask(mNextTask);
			mNextTask = null;
		}
	}
	public synchronized void updateTask(Runnable task) {
		log("done 2");
		if(task == null) {
			endTask();
			return;
		}
		if(mTaskRunner !=null && mTaskRunner.isAlive()) {
			mTaskRunner.interrupt();
			try {
				Thread tmp = mTaskRunner;
				if(tmp != null&&tmp.isAlive()) {
					tmp.join();
				}
				mTaskRunner = null;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mTaskRunner = new Thread(task);
		mTaskRunner.start();
	}

	public class UpdateTaskThread extends Thread {
		@Override
		public void run() {
			updateTask();
		}
	}
}
