package info.kyorohiro.helloworld.display.widget.lineview;

import java.util.ArrayList;

public class FlowingLineDatam implements CharSequence{
	public static int INCLUDE_END_OF_LINE = 1;
	public static int EXCLUDE_END_OF_LINE = 0;

	//private CharSequence mLine = "";
	private ArrayList<CharSequence> mLine = null;
	private int mColor = 0;
	private int mStatus = 0;

	public FlowingLineDatam(CharSequence line, int color, int status) {
		mLine = new ArrayList<CharSequence>();
		mLine.add(line);
		mColor = color;
		mStatus = status;
	}

	private FlowingLineDatam(ArrayList<CharSequence> line, int color, int status) {
		mLine = line;
		mColor = color;
		mStatus = status;	
	}

	public synchronized char charAt(int index) {
		int length = 0;
		int prev = 0;
		for(CharSequence line : mLine){
			length += line.length();
			if(index<length){
				return line.charAt(index-prev);
			}
			prev = length;
		}
		throw new IndexOutOfBoundsException();
	}

	public synchronized int length() {
		int length = 0;
		for(CharSequence line : mLine){
			length +=line.length();
		}
		return length;
	}

	// kiyohiro add but not support filter now;
	public synchronized void insert(CharSequence insted, int position){
		android.util.Log.v("mo","p="+position+",l="+length());
		for(CharSequence s: mLine){
			android.util.Log.v("mo",""+s.toString());
		}
		android.util.Log.v("mo","====");
		ArrayList<CharSequence> next = new ArrayList<CharSequence>();
		next.add(subSequence(0, position));
		next.add(insted);
		next.add(subSequence(position, length()));
		ArrayList<CharSequence> tmp = mLine;
		mLine = next;
		tmp.clear();
		tmp = null;

		android.util.Log.v("mo","p="+position+",l="+length());
		for(CharSequence s: mLine){
			android.util.Log.v("mo",""+s.toString());
		}
		android.util.Log.v("mo","----");

	}

	public synchronized CharSequence subSequence(int start, int end) {
		if(start<0||length()<end){
			throw new IndexOutOfBoundsException();
		}
		if(end<start){
			int t = start;
			start = end;
			end = t;
		}

		if(start==end){
			return "";
		}

		int length = 0;
		int prev = 0;
		ArrayList<CharSequence> next = new ArrayList<CharSequence>();
		for(CharSequence line : mLine){
			length +=line.length();
			int s=0;
			int e=line.length();
			if(start<length&&start>prev){
				s = start-prev;
			}

			if(end<length&&e>(end-prev)) {
				e = end-prev;
			}

			if(e<s){e=s;}

			//
			if(start<length){
				next.add(line.subSequence(s, e));
			}
			if(length>=end){
				break;
			}
			prev = length;
		}
		return new FlowingLineDatam(next, mColor, mStatus);
	}

	@Override
	public synchronized String toString() {
		if(mLine == null){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for(CharSequence line : mLine){
			builder.append(line);
		}
		return builder.toString();
	}

	public int getColor() {
		return mColor;
	}

	public int getStatus() {
		return mStatus;
	}
}
