package info.kyorohiro.helloworld.util;

//
// KyoroString‚É‹zŽû‚³‚ê‚éB
public class CharArrayBuilder {
	private int mPointer = 0;
	private int mLength = 256;
	private char[] mBuffer = new char[mLength];

	public void append(char moji){
		if(mPointer >= mLength){
			updateBuffer();
		}
		mBuffer[mPointer] = moji;
		mPointer++;
	}

	public void setLength(int length) {
		if(mLength < length) {
			mLength = length*2/3;
			updateBuffer();
		}
	}
	private void updateBuffer() {
		mLength *=2;
		char[] tmp = new char[mLength*2];
		for(int i=0;i<mBuffer.length;i++) {
			tmp[i] = mBuffer[i];
		}
		mBuffer = tmp;
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

}
