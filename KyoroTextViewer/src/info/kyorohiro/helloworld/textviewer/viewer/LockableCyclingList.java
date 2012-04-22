package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.android.util.SimpleLock;
import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class LockableCyclingList extends CyclingList<FlowingLineDatam>
implements SimpleLockInter{

	private SimpleLock mLock = new SimpleLock();

	public LockableCyclingList(int listSize) {
		super(listSize);
	}

	@Override
	public synchronized FlowingLineDatam get(int i) {
		try {
			lock();
//			beginLock();
			return super.get(i);
		} finally {
//			endLock();
		}
	}

	//todo
	public synchronized int getMaxOfStackedElement() {
		try {
			lock();
//			beginLock();
			return super.getMaxOfStackedElement();
		} finally {
//			endLock();
		}
	}

	//todo
	public synchronized void setNumOfAdd(int num) {
		try {
			lock();
//			beginLock();
			super.setNumOfAdd(num);
		} finally {
//			endLock();
		}
	}

	@Override
	public synchronized int getNumOfAdd() {
		try {
			lock();
//			beginLock();
			return super.getNumOfAdd();
		} finally {
//			endLock();
		}
	}

	@Override
	public synchronized void clearNumOfAdd() {
		try {
			lock();
//			beginLock();
			super.clearNumOfAdd();
		} finally {
//			endLock();
		}		
	}

	@Override
	public synchronized void add(FlowingLineDatam element) {
		try {
			lock();
//			beginLock();
			super.add(element);
		} finally {
//			endLock();
		}
	}

	@Override
	public synchronized int getNumberOfStockedElement() {
		try {
			lock();
//			beginLock();
			return super.getNumberOfStockedElement();
		} finally {
//			endLock();
		}
	}

	@Override
	public synchronized void head(FlowingLineDatam element) {
		try {
			lock();
//			beginLock();
			super.head(element);
		} finally {
//			endLock();
		}
	}

	@Override
	public synchronized void clear() {
		try {
			lock();
//			beginLock();
			super.clear();
		} finally {
//			endLock();
		}
	}

	@Override
	public synchronized FlowingLineDatam[] getLast(FlowingLineDatam[] ret,
			int numberOfRetutnArrayElement) {
		try {
			lock();
//			beginLock();
			return super.getLast(ret, numberOfRetutnArrayElement);
		} finally {
//			endLock();
		}
	}

	@Override
	public synchronized FlowingLineDatam[] getElements(FlowingLineDatam[] ret, int start,
			int end) {
		try {
			lock();
//			beginLock();
			return super.getElements(ret, start, end);
		} finally {
//			endLock();
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
