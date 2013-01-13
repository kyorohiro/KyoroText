package info.kyorohiro.helloworld.display.widget.editview.differ;

import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.editview.differ.DeleteLine;
import info.kyorohiro.helloworld.display.widget.editview.differ.DifferAddAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.DifferDeleteAction;
import info.kyorohiro.helloworld.display.widget.editview.differ.DifferGetAction;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;

import java.util.LinkedList;


public class Differ {
	private final DifferGetAction mGetAction = new DifferGetAction();
	private final DifferAddAction mAddAction = new DifferAddAction();
	private final DifferDeleteAction mDeleteAction = new DifferDeleteAction();

	private LinkedList<Line> mLine = new LinkedList<Line>();
	private int mLength = 0;

	private String mType = "";
	private String mExtra = "";
	private int mColor = SimpleGraphicUtil.BLACK;

	//
	// no test
	public Line getLine(int location) {
		return mLine.get(location);
	}

	//
	public int numOfLine() {
		return mLine.size();
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
		mLine.clear();
	}

	public int lengthOfLine() {
		return mLine.size();
	}

	public int length() {
		return mLength;
	}

	public synchronized void save(VirtualFile file) {
		
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


	public void checkAllSortedLine(CheckAction action) {
		int len = mLine.size();
		int index = 0;
		int start = 0;
		int end = 0;
		int indexFromBase = 0;
		try {
			action.init();
			for (int x = 0; x < len; x++) {
				Line l = mLine.get(x);
				if (l instanceof DeleteLine) {
					//indexFromBase += l.length();
					start = index + l.begin();
					end = start;// + l.length();
					index += l.begin();// - l.length();
					//for(int i=0;i<l.length();i++){
						if (!action.check(mLine, x, start, end, indexFromBase)) {
							return;
						}
					//}
				} else {
					start = index + l.begin();
					end = start + l.length();
					index += l.begin() + l.length();
					//indexFromBase += end - start;
					if (!action.check(mLine, x, start, end, indexFromBase)) {
						return;
					}
				}
			}
		} finally {
			action.end(mLine);
		}
	}

	public static abstract class CheckAction {
		public void init() {
		};

		// if return false, when check action is end.
		public abstract boolean check(LinkedList<Line> l, int x, int start,
				int end, int indexFromBase);

		public void end(LinkedList<Line> ll) {
		};
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
		//android.util.Log.v("kiyo", "#Differ#"+message);
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
