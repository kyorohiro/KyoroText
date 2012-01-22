package info.kyorohiro.helloworld.util;

import javax.xml.ws.FaultAction;

/**
 * duplicate asynchronously CyclingList.
 * 
 * todo
 */
public class AsyncDuplicateCyclingList<T> implements CyclingListInter<T> {

	private CyclingList<T> mBase = null;
	private CyclingList<T> mCopy = null;
	private Task mTask = null;
	private boolean taskStatus = false;
	private int mBufferSize = 0;

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
		if (taskStatus && mTask.isAlive()) {
			copy();
		} else {
			if (filter(element)) {
				mCopy.add(element);
			}
		}
		mBase.add(element);
	}

	@Override
	public int getNumberOfStockedElement() {
		return mBase.getNumberOfStockedElement();
	}

	public CyclingList<T> getCopyingList() {
		return mCopy;
	}

	private int mPosition = 0;

	private synchronized void copy() {
		T t = mBase.get(mPosition);
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
				for (mPosition = 0; mPosition < len; mPosition++) {
					copy();
					Thread.sleep(0);
				}
			} catch (InterruptedException e) {

			}
		}
	}

	/**
	 * todo: duplacated object's clear function's code is unimplements.
	 */
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
}
