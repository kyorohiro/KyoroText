package info.kyorohiro.helloworld.util;

//
// KyoroStringÇ…ãzé˚Ç≥ÇÍÇÈÅB
public class FloatArrayBuilder {
	private int mPointer = 0;
	private int mLength = 256;
	private float[] mBuffer = new float[mLength];

	public void setLength(int length) {
		if(mLength < length) {
			updateBuffer();
		}
	}

	private void updateBuffer() {
		mLength *=2;
		float[] tmp = new float[mLength*2];
		for(int i=0;i<mBuffer.length;i++) {
			tmp[i] = mBuffer[i];
		}
		mBuffer = tmp;
	}
	public void append(float moji){
		if(mPointer >= mLength){
			updateBuffer();
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

	public float[] getAllBufferedMoji(){
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
