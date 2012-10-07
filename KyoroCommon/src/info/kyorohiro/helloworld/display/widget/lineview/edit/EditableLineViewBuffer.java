package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;

public class EditableLineViewBuffer implements LineViewBufferSpec, IMEClient {

	private Differ mDiffer = new Differ();
	private LineViewBufferSpec mOwner = null;
	private int mCursorCol = 0;
	private int mCursorLine = 0;

	public EditableLineViewBuffer(LineViewBufferSpec owner) {
		super();
		mOwner = owner;
	}

	@Override
	public int getNumOfAdd() {
		return mOwner.getNumOfAdd();
	}

	@Override
	public void clearNumOfAdd() {
		mOwner.clearNumOfAdd();
	}

	@Override
	public int getNumberOfStockedElement() {
		return mOwner.getNumberOfStockedElement() + mDiffer.length();
	}


	@Override
	public LineViewData get(int i) {
		return mDiffer.get(mOwner, i);
	}

	@Override
	public BreakText getBreakText() {
		return mOwner.getBreakText();
	}

	@Override
	public LineViewData[] getElements(LineViewData[] ret, int start, int end) {
		for (int i = start, j = 0; i < end; i++, j++) {
			ret[j] = get(i);
		}
		return ret;
	}

	public int getRow() {
		return mCursorCol;
	}

	public int getCol() {
		return mCursorLine;
	}

	@Override
	public void setCursor(int row, int col) {
		mCursorCol = row;
		mCursorLine = col;
	}

	public void delete() {
		int index = getNumberOfStockedElement();//+1;
		if(index > mCursorLine) {
			index = mCursorLine;
		}
		android.util.Log.v("log","delete:"+index);
		mDiffer.deleteLine(index);		
	}

	@Override
	public void pushCommit(CharSequence text, int cursor) {
		//android.util.Log.v("log","col="+mCursorCol + ",row="+mCursorRow);
		int index = getNumberOfStockedElement();//+1;
		if(index > mCursorLine) {
			index = mCursorLine;
		}
		commit(text, cursor);
	}

	@Override
	public int getMaxOfStackedElement() {
		return mOwner.getMaxOfStackedElement();
	}

	private void commit(CharSequence text, int cursor) {
		CharSequence ct = get(mCursorLine);
		if(mCursorCol >= ct.length()) {
			mCursorCol = ct.length();
		}
		CharSequence f = composeString(ct, mCursorCol, text);

		int w = this.getBreakText().getWidth();
		int len = 0;
		int le = 0;
		boolean a = true;
		int i=0;
		do {
			android.util.Log.v("kiyo","_aaa0="+i);i++;
			android.util.Log.v("kiyo","_aaa1="+f.length()+","+w+","+f);
			len = getBreakText().breakText(f, 0, f.length(), w);
			le = f.length();
			android.util.Log.v("kiyo","_aaa2="+len+","+le);
			if(a) {
				mDiffer.setLine(mCursorLine, f.subSequence(0, len));
				a = false;
				if(le>len) {
					mCursorLine+=1;
					mCursorCol = 0;
				} else {
					mCursorCol += len;
				}
			} else {
				mDiffer.addLine(mCursorLine, f.subSequence(0, len));				
				if(le>len) {
					mCursorLine+=1;
					mCursorCol = 0;
				} else {
					mCursorCol += len;
				}
			}
			f = f.subSequence(len, le);
		} while(le>len);
	}

	public static CharSequence composeString(CharSequence b, int i, CharSequence s) {
		if(i>=b.length()){
			i=b.length();
		}
		if(i==0){
			return ""+s+b;
		}
		else if(i==b.length()){
			return ""+b+s;
		} else {
			CharSequence f = b.subSequence(0, i);
			CharSequence e = b.subSequence(i,b.length());
			return ""+f+s+e;
		}
	}

}