package info.kyorohiro.helloworld.util;

import java.util.ArrayList;

public class CyclingList<X> {

	private final ArrayList<X> mList;
	private final int mMaxOfStackedElement;
	private int mNextAddedPoint = 0;
	private boolean mListIsFull = false;//

	public CyclingList(int listSize) {
		mMaxOfStackedElement = listSize;
		mList = new ArrayList<X>(listSize);
		for(int i=0;i<listSize;i++){
			mList.add(i, null);
		}
	}

	public synchronized void clear() {
		mNextAddedPoint = 0;
		mListIsFull = false;//
	}

	public synchronized void add(X element) {
		mList.set(mNextAddedPoint, element);
		mNextAddedPoint = mNextAddedPoint + 1;

		if (mNextAddedPoint >= mMaxOfStackedElement) {
			mListIsFull = true;//
		}

		mNextAddedPoint = mNextAddedPoint % mMaxOfStackedElement;
	}

	public synchronized X[] getLast(X[] ret, int numberOfRetutnArrayElement) {
		int lengthOfList = numberOfRetutnArrayElement;
		int max = getNumberOfStockedElement();
		if (max <= lengthOfList) {
			lengthOfList = max;
		}
		for (int i = 0; i < lengthOfList; i++) {
			ret[i] = get(max - lengthOfList + i);
		}
		return ret;
	}

	public synchronized X[] getElements(X[] ret, int start, int end) {
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
		if (ret.length < lengthOfList) {
			lengthOfList = ret.length;
		}

		for (int i = 0; i < end - start; i++) {
			ret[i] = get(i + start);
		}
		return ret;
	}

	public X get(int i) {
		int num = i % mMaxOfStackedElement;
		if (!mListIsFull) {
			return mList.get(num);
		} else {
			num = (mNextAddedPoint + num) % mMaxOfStackedElement;
			return mList.get(num);
		}
	}

	public int getNumberOfStockedElement() {
		if (!mListIsFull) {
			return mNextAddedPoint;
		} else {
			return mMaxOfStackedElement;
		}
	}

}
