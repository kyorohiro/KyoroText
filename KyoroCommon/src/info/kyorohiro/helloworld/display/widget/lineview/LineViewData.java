package info.kyorohiro.helloworld.display.widget.lineview;


public class LineViewData implements CharSequence {
	public static int INCLUDE_END_OF_LINE = 1;
	public static int EXCLUDE_END_OF_LINE = 0;

	private int mColor = 0;
	private int mStatus = 0;
	private CharSequence mLine;
	public LineViewData(CharSequence line, int color, int status) {
		mLine = line;
		mColor = color;
		mStatus = status;
	}


	public  int length() {
		return mLine.length();
	}

	public  CharSequence subSequence(int start, int end) {
		return mLine.subSequence(start, end);
	}

	@Override
	public  String toString() {
		if(mLine == null){
			return "";
		}
		return mLine.toString();
	}

	public int getColor() {
		return mColor;
	}
	
	public void setColor(int color) {
		mColor = color;
	}

	public int getStatus() {
		return mStatus;
	}


	@Override
	public char charAt(int index) {
		return mLine.charAt(index);
	}
}
