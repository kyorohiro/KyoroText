package info.kyorohiro.helloworld.stress.task;


import info.kyorohiro.helloworld.stress.KyoroSetting;

import java.util.LinkedList;

import android.test.IsolatedContext;

public class EatUpJavaHeapTask implements Runnable {
	private LinkedList<byte[]> mBuffer = new LinkedList<byte[]>();
	public static int mEatUpSize = 10*1024*1024;
	public static int mAtomSize = 1024*4;

	public EatUpJavaHeapTask(LinkedList<byte[]> buffer) {
		try {
			mEatUpSize = KyoroSetting.getEatupHeapSize() * 1024;
		} catch(Throwable t) {
			t.printStackTrace();
		}
		mBuffer = buffer;
	}

	@Override
	public void run() {
		String retryValue = null;
		try {
			do {
				retryValue = KyoroSetting.getRetry();
				StressUtility.eatUpJavaHeap(mBuffer, mEatUpSize, mAtomSize);
				Thread.sleep(1500);
			} while(KyoroSetting.RETRY_ON.equals(retryValue)&& mBuffer.size()*mEatUpSize < mAtomSize);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
