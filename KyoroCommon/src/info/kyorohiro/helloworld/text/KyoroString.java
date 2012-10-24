package info.kyorohiro.helloworld.text;
///*
public class KyoroString  implements CharSequence {
	public char[] mContent = null;
	private long mLinePosition = 0;
	private boolean mIncludeLF = false;
	private int mColor = 0;

	// file buffer
	private long mBeginPoint = -1;
	private long mEndPoint = -1;

	public static KyoroString newKyoroStringWithLF(CharSequence content, int color) {
		if(content.charAt(content.length()-1)!='\n'){
			content = ""+content+"\n";//todo \r\n or \n
		}
		return new KyoroString(content, color);
	}

	public KyoroString(CharSequence content, int color) {
		int len = content.length();
		char[] contentBuffer = new char[len];
		for(int i=0;i<len;i++){
			contentBuffer[i] = content.charAt(i);
		}
		init(contentBuffer, len);
		mColor = color;
	}

	public KyoroString(char[] content, int length) {
		init(content, length);
	}

	private void init(char[] content, int length) {
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

	public boolean includeLF(){
		return mIncludeLF;
	}

	public void setBeginPointer(long pointer){
		mBeginPoint = pointer;
	}

	public long getBeginPointer(){
		return mBeginPoint;
	}

	public void setEndPointer(long pointer){
		mEndPoint = pointer;
	}

	public long getEndPointer(){
		return mEndPoint;
	}

	public void setLinePosition(long linePosition) {
		mLinePosition = linePosition;
	}

	public long getLinePosition() {
		return mLinePosition;
	}

	public int getColor() {
		return mColor;
	}
	
	public void setColor(int color) {
		mColor = color;
	}

}
