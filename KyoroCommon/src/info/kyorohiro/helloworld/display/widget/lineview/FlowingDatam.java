package info.kyorohiro.helloworld.display.widget.lineview;

public class FlowingDatam implements CharSequence{
	public static int STRING = 0;
	public static int CURSOR = 1;
	public static int IMAGE = 2;//
	public static int IMAGE_SLAVE=3;//
	public static int HR = 4;//
	

	private CharSequence mLine = "";
	private int mStatus = 0;

	public FlowingDatam(CharSequence line, int status) {
		mLine = line;
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
		if(mLine == null){
			return "";
		}
		return mLine.toString();
	}

	public int getStatus() {
		return mStatus;
	}
}
