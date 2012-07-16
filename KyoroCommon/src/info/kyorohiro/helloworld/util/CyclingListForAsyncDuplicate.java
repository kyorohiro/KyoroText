package info.kyorohiro.helloworld.util;


/**
 * duplicate asynchronously CyclingList.
 */
public class CyclingListForAsyncDuplicate<T> implements CyclingListInter<T> {

	private CyclingList<T> mBase = null;
	private CyclingList<T> mCopy = null;
	private Task mTask = null;
	private boolean mTaskStatus = false;
	private int mBufferSize = 0;
	private int mIndexForNextCopy = 0;

	public CyclingListForAsyncDuplicate(CyclingList<T> base, int copyDataBuffer) {
		mBase = base;
		mCopy = new CyclingList<T>(copyDataBuffer);
		mBufferSize = copyDataBuffer;
	}

	
	public synchronized void start() {
		stop();
		mTaskStatus = true;
		mCopy = new CyclingList<T>(mBufferSize);
		mTask = new Task();
		mTask.start();
	}

	public synchronized void stop() {
		mTaskStatus = false;
		if (taskIsAlive()) {
			mTask.interrupt();
		}
	}

	public boolean taskIsAlive() {
		if (mTask == null) {
			return false;
		}

		return mTaskStatus;
		// mTask.isAlive())
	}

	@Override
	public T get(int i) {
		return mBase.get(i);
	}

	@Override
	public synchronized void add(T element) {
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
		if (mTaskStatus && mTask.isAlive()) {
			return true;
		} else {
			return false;
		}
	}
	public CyclingList<T> getDuplicatingList() {
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

	@Override
	public synchronized void head(T element) {
		mBase.head(element);
		mCopy.head(element);
		mIndexForNextCopy++;
	}


	@Override
	public int getNumOfAdd() {
		return mCopy.getNumOfAdd();
	}


	@Override
	public void clearNumOfAdd() {
		mCopy.clearNumOfAdd();
	}


	@Override
	public synchronized int getMaxOfStackedElement() {
		return mCopy.getMaxOfStackedElement();
	}
}
