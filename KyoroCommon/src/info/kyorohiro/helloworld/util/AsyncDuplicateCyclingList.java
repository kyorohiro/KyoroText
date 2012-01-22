package info.kyorohiro.helloworld.util;


/**
 * duplicate asynchronously CyclingList.
 */
public class AsyncDuplicateCyclingList<T> implements CyclingListInter<T> {

	private CyclingList<T> mBase = null;
	private CyclingList<T> mCopy = null;
	private Task mTask = null;
	private boolean taskStatus = false;
	private int mBufferSize = 0;
	private int mIndexForNextCopy = 0;

	public AsyncDuplicateCyclingList(CyclingList<T> base, int copyDataBuffer) {
		mBase = base;
		mCopy = new CyclingList<T>(copyDataBuffer);
		mBufferSize = copyDataBuffer;
	}

	
	public synchronized void start() {
		stop();
		taskStatus = true;
		mCopy = new CyclingList<T>(mBufferSize);
		mTask = new Task();
		mTask.start();
	}

	public synchronized void stop() {
		taskStatus = false;
		if (taskIsAlive()) {
			mTask.interrupt();
		}
	}

	public boolean taskIsAlive() {
		if (mTask == null) {
			return false;
		}

		return taskStatus;
		// mTask.isAlive())
	}

	@Override
	public T get(int i) {
		return mBase.get(i);
	}

	@Override
	public synchronized void add(T element) {
		// following todo
		if (copyTaskIsAlive()) {
			copy();
		} else {
			copy(element);
		}
		mBase.add(element);
	}

	@Override
	public int getNumberOfStockedElement() {
		return mBase.getNumberOfStockedElement();
	}

	@Override
	public void clear() {
		mBase.clear();
		mCopy.clear();
	}

	@Override
	public T[] getLast(T[] ret, int numberOfRetutnArrayElement) {
		return mBase.getLast(ret, numberOfRetutnArrayElement);
	}

	@Override
	public T[] getElements(T[] ret, int start, int end) {
		return mBase.getElements(ret, start, end);
	}

	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	//--------------------------------------------------------------------------
	
	public boolean copyTaskIsAlive(){
		if (taskStatus && mTask.isAlive()) {
			return true;
		} else {
			return false;
		}
	}
	public CyclingList<T> getCopyingList() {
		return mCopy;
	}

	private synchronized void copy() {
		T t = mBase.get(mIndexForNextCopy);
		if (filter(t)) {
			mCopy.add(t);
		}
	}

	private synchronized void copy(T t) {
		if (filter(t)) {
			mCopy.add(t);
		}
	}

	protected boolean filter(T t) {
		return true;
	}

	public class Task extends Thread {
		@Override
		public void run() {
			super.run();
			int len = mBase.getNumberOfStockedElement();
			try {
				for (mIndexForNextCopy = 0; mIndexForNextCopy < len; mIndexForNextCopy++) {
					copy();
					Thread.sleep(0);
				}
			} catch (InterruptedException e) {

			}
		}
	}
}
