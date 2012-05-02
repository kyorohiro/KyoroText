package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.util.CyclingList;

public class LockableCyclingList extends CyclingList<FlowingLineDatam>
implements SimpleLockInter{
	public LockableCyclingList(int listSize) {
		super(listSize);
	}

	@Override
	public synchronized FlowingLineDatam get(int i) {
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
	public synchronized void add(FlowingLineDatam element) {
		lock();
		super.add(element);
	}

	@Override
	public synchronized int getNumberOfStockedElement() {
		lock();
		return super.getNumberOfStockedElement();
	}

	@Override
	public synchronized void head(FlowingLineDatam element) {
		lock();
		super.head(element);
	}

	@Override
	public synchronized void clear() {
		lock();
		super.clear();
	}

	@Override
	public synchronized FlowingLineDatam[] getLast(FlowingLineDatam[] ret, int numberOfRetutnArrayElement) {
		lock();
		return super.getLast(ret, numberOfRetutnArrayElement);
	}

	@Override
	public synchronized FlowingLineDatam[] getElements(FlowingLineDatam[] ret, int start,
			int end) {
		try {
			lock();
			return super.getElements(ret, start, end);
		} finally {
		}
	}

	@Override
	public synchronized void beginLock() {
		locked = true;
		cThread = Thread.currentThread();
	}

	@Override
	public synchronized void endLock() {
		locked = false;
		notifyAll();
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
