package info.kyorohiro.helloworld.util;



public class CyclingList<X> {

	private final Object[] mList;
	private int mNextAddedPoint = 0;
	private boolean mListIsFull = false;//

	public CyclingList(int listSize) {
		mList = new Object[listSize];
	}

	public synchronized void clear(){
		mNextAddedPoint = 0;
		mListIsFull = false;//
	}

	public synchronized void add(Object element) {
		mList[mNextAddedPoint] = element;
		mNextAddedPoint = mNextAddedPoint + 1;

		if (mNextAddedPoint >= mList.length) {
			mListIsFull = true;//
		}
		mNextAddedPoint = mNextAddedPoint % mList.length;
	}

	public synchronized Object[] getLast(int numberOfRetutnArrayElement) {
		int lengthOfList = numberOfRetutnArrayElement;
		int max = getNumberOfStockedElement();
		if (max <= lengthOfList) {
			lengthOfList = max;
		}
		Object[] ret = new Object[lengthOfList];
		for (int i = 0; i < lengthOfList; i++) {
			ret[i] = get(max - lengthOfList + i);
		}
		return ret;
	}

	public synchronized Object[] getElements(int start, int end) {
		int max = getNumberOfStockedElement();
		if (max < end) {
			end = max;
		}
		if (start < 0) {
			start = 0;
		}
		if (start > end) {
			int t = start;
			start = end;
			end = t;
		}
		int lengthOfList = end - start;

		Object[] ret = new Object[lengthOfList];
		for (int i = 0; i < end - start; i++) {
			ret[i] = get(i + start);
		}
		return ret;
	}

	public Object get(int i) {
		int num = i % mList.length;
		if (mListIsFull) {
			return mList[num];
		} else {
			num = (mNextAddedPoint + num) % mList.length;
			return mList[num];
		}
	}

	public int getNumberOfStockedElement() {
		if (mListIsFull) {
			return mNextAddedPoint;
		} else {
			return mList.length;
		}
	}

}
