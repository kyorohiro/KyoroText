package info.kyorohiro.helloworld.util;

//
public class FloatArrayBuilder {
	private int mPointer = 0;
	private float[] mBuffer = new float[256];

	public void setBufferLength(int length) {
		if(mBuffer.length < length) {
			updateBuffer();
		}
	}

	private void updateBuffer() {
		float[] tmp = new float[mBuffer.length*2];
		for(int i=0;i<mBuffer.length;i++) {
			tmp[i] = mBuffer[i];
		}
		mBuffer = tmp;
	}

	public void append(float moji){
		if(mPointer >= mBuffer.length){
			updateBuffer();
		}
		mBuffer[mPointer] = moji;
		mPointer++;
	}

	public void clear() {
		mPointer = 0;
	}

	public float[] getBuffer(){
		return mBuffer;
	}

	public int length(){
		return mPointer;
	}

	public void removeLast(){
		if(0<mPointer) {
			mPointer--;
		}
	}

}
