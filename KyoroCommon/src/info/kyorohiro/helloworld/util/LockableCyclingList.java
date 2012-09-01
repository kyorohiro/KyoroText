package info.kyorohiro.helloworld.util;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;

public class LockableCyclingList extends CyclingList<LineViewData>
implements SimpleLockInter {
	public LockableCyclingList(int listSize) {
		super(listSize);
	}

	@Override
	public synchronized LineViewData get(int i) {
		lock();
		return super.get(i);
	}

	//todo
	public synchronized int getMaxOfStackedElement() {
		lock();
		return super.getMaxOfStackedElement();
	}

	//todo
	public synchronized void setNumOfAdd(int num) {
		lock();
		super.setNumOfAdd(num);
	}

	@Override
	public synchronized int getNumOfAdd() {
		lock();
		return super.getNumOfAdd();
	}

	@Override
	public synchronized void clearNumOfAdd() {
		lock();
		super.clearNumOfAdd();
	}

	@Override
	public synchronized void add(LineViewData element) {
		lock();
		super.add(element);
	}

	@Override
	public synchronized int getNumberOfStockedElement() {
		lock();
		return super.getNumberOfStockedElement();
	}

	@Override
	public synchronized void head(LineViewData element) {
		lock();
		super.head(element);
	}

	@Override
	public synchronized void clear() {
		lock();
		super.clear();
	}

	@Override
	public synchronized LineViewData[] getLast(LineViewData[] ret, int numberOfRetutnArrayElement) {
		lock();
		return super.getLast(ret, numberOfRetutnArrayElement);
	}

	@Override
	public synchronized LineViewData[] getElements(LineViewData[] ret, int start,
			int end) {
		try {
			lock();
			return super.getElements(ret, start, end);
		} finally {
		}
	}

	int num = 0;
	@Override
	public synchronized void beginLock() {
		locked = true;
		cThread = Thread.currentThread();
		num++;
		//android.util.Log.v("kiyo","LOCK:"+num);
	}

	@Override
	public synchronized void endLock() {
		locked = false;
		num--;
		//android.util.Log.v("kiyo","UNLOCK:"+num);
		if(num == 0){
			notifyAll();
		}
	}

	private boolean locked = false;
	private Thread cThread = null;
	private synchronized void lock() {
		if(locked&&cThread !=null&& cThread != Thread.currentThread()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
