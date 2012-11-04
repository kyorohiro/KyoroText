package info.kyorohiro.helloworld.text;

///*
public class KyoroString  implements CharSequence {
	public char[] mContent = null;
	private long mLinePosition = 0;
	private boolean mIncludeLF = false;
	private boolean mIncludeCRLF = false;
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

	public KyoroString(CharSequence content) {
		init(content, 0x000000);
	}

	public KyoroString(CharSequence content, int color) {
		init(content, color);
	}

	public KyoroString(char[] content, int length) {
		init(content, 0, length);
	}

	public KyoroString(char[] content, int start, int end) {
		init(content, start, end);
	}

	private void init(CharSequence content, int color) {
		int len = content.length();
		char[] contentBuffer = new char[len];
		for(int i=0;i<len;i++){
			contentBuffer[i] = content.charAt(i);
		}
		init(contentBuffer, 0, len);
		mColor = color;		
	}
	private void init(char[] content, int start, int end) {
		int length = end-start;
		mContent = new char[length];
//		android.util.Log.v("kiyo","dd="+start+",end="+end+","+length+",c="+content.length);
		System.arraycopy(content, start, mContent, 0, length);
		if(mContent.length >0 && mContent[length-1]=='\n'){
			mIncludeLF = true;
			if(mContent.length>1&&mContent[length-2]=='\r') {
				mIncludeCRLF = true;
			}
		} else {
			mIncludeLF = false;
		}
	}

	private int mPargedLF_CRLF = 0;
	private int mPargedEND = 0;
	public void pargeLF(boolean includeCR) {
		mPargedLF_CRLF = length()-lengthWithoutLF(includeCR)+mPargedEND;
	}
	public void pargeEnd() {
		mPargedEND++;
	}

	public void releaseParge() {
		mPargedLF_CRLF = 0;
		mPargedEND = 0;
	}
	@Override
	public char charAt(int index) {
		return mContent[index];
	}

	public char[] getChars() {
		return mContent;
	}

	public int lengthWithoutLF(boolean includeCR) {
		if(includeCR&&includeCRLF()){
			return length()-2;
		}
		else if(includeLF()) {
			return length()-1;
		} else {
			return length();
		}
	}

	@Override
	public int length() {
		return mContent.length-mPargedLF_CRLF-mPargedEND;
	}

	public KyoroString newKyoroString(int start, int end) {
		KyoroString ret = new KyoroString(mContent, start, end);
		ret.setColor(getColor());
		return ret;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return newKyoroString(start, end);
	}

	@Override
	public String toString() {
		return new String(mContent, 0, length());
	}

	public boolean includeCRLF(){
		return mIncludeCRLF;
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

//
//	private int mPointer = 0;
//	private int mLength = 512;
//	private char[] mBuffer = new char[mLength];
//
//	public void append(char moji){
//		if(mPointer >= mLength){
//			mLength *=2;
//			char[] tmp = new char[mLength*2];
//			for(int i=0;i<mBuffer.length;i++) {
//				tmp[i] = mBuffer[i];
//			}
//			mBuffer = tmp;
//		}
//		mBuffer[mPointer] = moji;
//		mPointer++;
//	}
//
}
