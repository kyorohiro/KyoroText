package info.kyorohiro.helloworld.io;

public class MyBuilder {
	private int mPointer = 0;
	private int mLength = 512;
	private char[] mBuffer = new char[mLength];

	public void append(char moji){
		if(mPointer >= mLength){
			mLength *=2;
			char[] tmp = new char[mLength*2];
			for(int i=0;i<mBuffer.length;i++) {
				tmp[i] = mBuffer[i];
			}
			mBuffer = tmp;
		}
		mBuffer[mPointer] = moji;
		mPointer++;
	}

	public void clear() {
		mPointer = 0;
	}

	public char[] getAllBufferedMoji(){
		return mBuffer;
	}

	public int getCurrentBufferedMojiSize(){
		return mPointer;
	}

	public void removeLast(){
		if(0<mPointer) {
			mPointer--;
		}
	}

	public String toString(){
		return new String(mBuffer, 0, mPointer);
	}
}
