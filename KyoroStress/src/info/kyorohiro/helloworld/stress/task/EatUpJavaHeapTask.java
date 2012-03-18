package info.kyorohiro.helloworld.stress.task;


import info.kyorohiro.helloworld.stress.KyoroSetting;

import java.util.LinkedList;

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
		StressUtility.eatUpJavaHeap(mBuffer, mEatUpSize, mAtomSize);
	}
}
