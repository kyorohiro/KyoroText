package info.kyorohiro.helloworld.display.widget.editview.differ;

import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.DifferAddAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.DifferDeleteAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.DifferGetAction;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.TaskTicket;

import java.util.LinkedList;


public class Differ {
	public static final String KEY_DIFFER_FONT_COLOR1 = "KEY_DIFFER_FONT_COLOR1";
	private final DifferGetAction mGetAction = new DifferGetAction();
	private final DifferAddAction mAddAction = new DifferAddAction();
	private final DifferDeleteAction mDeleteAction = new DifferDeleteAction();

	private LinkedList<Line> mLineList = new LinkedList<Line>();
	private int mLength = 0;

	private String mType = "";
	private String mExtra = "";
	private int mColor = SimpleGraphicUtil.BLACK;
	private boolean mIsLogOn = false;

	public void logon() {
		mIsLogOn = true;
	}

	public void logoff() {
		mIsLogOn = false;
	}


	//
	// no test
	public Line getLine(int location) {
		return mLineList.get(location);
	}

	public void addLine(int location, Line line) {
		mLineList.add(location, line);
	}

	public void removeLine(int location) {
		mLineList.remove(location);
	}
	//
	public int numOfLine() {
		return mLineList.size();
	}

	public void asisSetColor(int color) {
		mColor = color;
	}

	public int getColor() {
		return mColor;
	}

	public void asisSetType(String type) {
		mType = type;
	}

	public void asisSetExtra(String extra) {
		mExtra = extra;
	}

	public synchronized void clear() {
		mLength = 0;
		mLineList.clear();
	}

	public int lengthOfLine() {
		return mLineList.size();
	}

	public int length() {
		return mLength;
	}

	public synchronized TaskTicket<String> save(LineViewBufferSpec spec, VirtualFile file) {
		SaveTaskForDiffer stask = new SaveTaskForDiffer(this, spec, file);
		Thread t = new Thread(stask);
		t.start();
		return stask.getTicket();
	}

	public synchronized KyoroString get(LineViewBufferSpec spec, int _index) {
		return mGetAction.get(this, spec, _index);
	}

	public synchronized void addLine(int i, CharSequence line) {
		debugPrint("begin addLine["+i+"]"+mLength+","+line);
		if(line instanceof KyoroString) {
			((KyoroString) line).setExtra(mExtra);
			((KyoroString) line).setType(mType);			
		} else {
//			android.util.Log.v("kiyo","---NG--");
			line = new KyoroString(line);
			((KyoroString) line).setExtra(mExtra);
			((KyoroString) line).setType(mType);						
		}
		mAddAction.add(this, i, line);
		mLength++;
		debugPrint();	
	}

	public synchronized void setLine(int i, CharSequence line) {
		debugPrint("begin setLine["+i+"]"+mLength+","+line);
		deleteLine(i);
		addLine(i, line);
		debugPrint("end setLine["+i+"]"+mLength+","+line);
		debugPrint();
	}

	public synchronized void deleteLine(int i) {
		debugPrint("begin deleteLine["+i+"]"+mLength);
		mDeleteAction.delete(this, i);
		mLength -= 1;
		debugPrint("end deleteLine["+i+"]"+mLength);
	}


	public void checkAllSortedLine(CheckAction targetJob) {
		int len = mLineList.size();
		int index = 0;
		int dummy = 0;
		int patchedPosition = 0;
		int unpatchedPosition = 0;
		try {
			targetJob.init();
			for (int lineLocation = 0; lineLocation < len; lineLocation++) {
				Line targetLine = mLineList.get(lineLocation);
				patchedPosition += targetLine.begin();
				unpatchedPosition += targetLine.begin();
				//debugPrint("#line+"+lineLocation+","+patchedPosition+","+unpatchedPosition);
				if (targetLine instanceof DeleteLine) {
					patchedPosition += 0;
					unpatchedPosition += targetLine.length();
					if (!targetJob.check(this, lineLocation, patchedPosition, unpatchedPosition, index)) {
						return;
					}
					index += targetLine.begin();// - l.length();
				} else {
					patchedPosition += targetLine.length();
					unpatchedPosition += 0;
					if (!targetJob.check(this, lineLocation, patchedPosition, unpatchedPosition, index)) {
						return;
					}
					index += targetLine.begin() + targetLine.length();
				}
			}
		} finally {
			targetJob.end(mLineList);
		}
	}

	public static interface CheckAction {
		public void init();
		// if return false, when check action is end.
		public abstract boolean check(Differ owner, int lineLocation, int patchedPosition, int unpatchedPosition, int index);
		public void end(LinkedList<Line> ll);
	}

	interface Line {
		// public int consume();
		public int begin();

		public int length();

		public void setStart(int start);

		public void set(int index, CharSequence line);

		public void rm(int index);

		public CharSequence get(int i);

		public void insert(int index, CharSequence line);
	}

	public void debugPrint(String message) {
		if(mIsLogOn) {
			android.util.Log.v("kiyo", "#Differ#"+message);
		}
	}
	public void debugPrint() {	
		if(true){
			return;
		}
		/*
		 android.util.Log.v("ll", "#---start---" + mLine.size());
		 
		int j = 0;
		for (Line l : mLine) {
			android.util.Log.v("ll", "#" + l.length()+","+l.begin());
			android.util.Log.v("ll", "#" + l.toString());
			
			for (int i = 0; i < l.length(); i++) {
				android.util.Log.v("ll", "#[" + j + "][" + i + "]=" + l.get(i)+","+l.begin()+","+l.length());
			}
			j++;
		}
		android.util.Log.v("ll", "#---end---" + mLine.size());
		*/
	}

}
