package info.kyorohiro.helloworld.text;
///*
public class KyoroString  implements CharSequence {
	public char[] mContent = null;
	private long mLinePosition = 0;
	private boolean mIncludeLF = false;

	public KyoroString(char[] content, int length) {
		mContent = new char[length];
		System.arraycopy(content, 0, mContent, 0, length);
		if(mContent.length >0 && mContent[length-1]=='\n'){
			mIncludeLF = true;
		} else {
			mIncludeLF = false;
		}
	}

	@Override
	public char charAt(int index) {
		return mContent[index];
	}

	@Override
	public int length() {
		return mContent.length;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return new String(mContent, start, end-start);
	}

	@Override
	public String toString() {
		return new String(mContent, 0, mContent.length);
	}

	public void setLinePosition(long linePosition) {
		mLinePosition = linePosition;
	}

	public long getLinePosition() {
		return mLinePosition;
	}

	public boolean includeLF(){
		return mIncludeLF;
	}
}
//*/