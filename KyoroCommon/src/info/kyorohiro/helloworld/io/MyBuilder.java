package info.kyorohiro.helloworld.io;


//
//
// KyoroString‚É‹zŽû‚³‚ê‚éB
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

	public void clearFirst(int num) {
		if(num <0){
			return;
		}
		for(int i=0;(i+num)<mBuffer.length;i++) {
			mBuffer[i] = mBuffer[i+num]; 
		}		
		mPointer -= num;
		if(mPointer < 0){
			mPointer = 0;
		}
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
