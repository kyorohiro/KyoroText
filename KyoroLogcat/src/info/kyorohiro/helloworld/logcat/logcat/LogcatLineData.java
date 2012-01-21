package info.kyorohiro.helloworld.logcat.logcat;

public class LogcatLineData implements CharSequence{
	public static int INCLUDE_END_OF_LINE = 1;
	public static int EXCLUDE_END_OF_LINE = 0;

	private String mLine = "";
	private int mColor = 0;
	private int mStatus = 0;
	
	public LogcatLineData(String line, int color, int status) {
		mLine = line;
		mColor = color;
		mStatus = status;
	}

	public char charAt(int index) {
		return mLine.charAt(index);
	}

	public int length() {
		return mLine.length();
	}

	public CharSequence subSequence(int start, int end) {
		return mLine.subSequence(start, end);
	}

	@Override
	public String toString() {
		return mLine;
	}

	public int getColor(){
		return mColor;
	}
}
