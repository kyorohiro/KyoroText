package info.kyorohiro.helloworld.util;

public class TaskTicket<T> extends AsyncronousTask {
	private Task<T> mTask = null;
	public TaskTicket(Task<T> task) {
		super(task);
		mTask = task;
	}

	public T getT() throws InterruptedException {
		if(!syncTask()) {
			throw new InterruptedException("--0127--");
		}
		return mTask.get();
	}

	public static interface Task<T> extends Runnable {
		public T get();
		@Override
		public void run();
	}
}
