package info.kyorohiro.helloworld.stress.task;


import java.util.LinkedList;

public class EatUpJavaHeapTask implements Runnable {
	private LinkedList<byte[]> mBuffer = new LinkedList<byte[]>();
	public int mEatUpSize = 10*1024*1024;
	public int mAtomSize = 1024*4;
	public EatUpJavaHeapTask(LinkedList<byte[]> buffer) {
		mBuffer = buffer;
	}
	@Override
	public void run() {
		StressUtility.eatUpJavaHeap(mBuffer, mEatUpSize, mAtomSize);
	}		
}
