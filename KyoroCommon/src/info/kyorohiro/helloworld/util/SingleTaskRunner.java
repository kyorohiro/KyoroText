package info.kyorohiro.helloworld.util;


/**
 * 
 * Management Task. Current running thread is end. and nextTask is run.
 *
 */
public class SingleTaskRunner {

	private Thread mTaskRunner = null;
	private Runnable mNextTask = null;
	private Thread mTaskUpdater = null;

	private void log(String message) {
		//System.out.println("#SingleTaskRunner#"+message);
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
		if(mTaskUpdater == null || !mTaskUpdater.isAlive()) {
			mTaskUpdater = new UpdateTaskThread();
			mTaskUpdater.start();
		}
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
			Thread tmp = mTaskRunner;
			mTaskRunner = null;
			try {
				tmp.interrupt();
				if(tmp != null&&tmp.isAlive()) {
					tmp.join();
				}
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
