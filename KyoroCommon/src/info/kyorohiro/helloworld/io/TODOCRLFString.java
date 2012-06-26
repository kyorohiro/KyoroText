package info.kyorohiro.helloworld.io;

public class TODOCRLFString  implements CharSequence {
	public static int MODE_INCLUDE_LF = 1;
	public static int MODE_EXCLUDE_LF = 0;
	public char[] mContent = null;
	public int mMode = MODE_EXCLUDE_LF;
	private long mLinePosition = 0;

	public TODOCRLFString(char[] content, int length, int mode) {
		mContent = new char[length];
		System.arraycopy(content, 0, mContent, 0, length);
		mMode = mode;
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
		return new String(mContent, start, end);
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
		if(mMode == TODOCRLFString.MODE_INCLUDE_LF){
			return true;
		} else {
			return false;
		}
	}
}
