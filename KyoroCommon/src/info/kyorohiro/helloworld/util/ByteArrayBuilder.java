package info.kyorohiro.helloworld.util;

//
// KyoroString�ɋz����B
public class ByteArrayBuilder {
	private int mPointer = 0;
	private int mLength = 256;
	private byte[] mBuffer = new byte[mLength];

	public void append(byte moji){
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
		byte[] tmp = new byte[mLength*2];
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

	public byte[] getAllBufferedMoji(){
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