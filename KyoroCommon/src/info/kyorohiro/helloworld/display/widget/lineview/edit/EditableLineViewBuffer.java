package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;

//
// next 改行時にCRLF付けるようにする。
//
public class EditableLineViewBuffer implements LineViewBufferSpec, IMEClient {

	private Differ mDiffer = new Differ();
	private LineViewBufferSpec mOwner = null;
	private int mCursorRow = 0;
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
		return mCursorRow;
	}

	public int getCol() {
		return mCursorLine;
	}

	@Override
	public void setCursor(int row, int col) {
		mCursorRow = row;
		mCursorLine = col;
		if(mCursorLine < 0) {
			mCursorLine = 0;
		}
		else if(mCursorLine >= getNumberOfStockedElement()) {
			mCursorLine = getNumberOfStockedElement()-1;
		}

		CharSequence c = get(mCursorLine);
		if(mCursorRow < 0) {
			//移動する。
			if(mCursorLine > 0) {
				mCursorLine-=1;
				mCursorRow = c.length();
			} else {
				mCursorRow = 0;
			}
		}
		else if(mCursorRow > c.length()){
			//移動する。
			if(mCursorLine < getNumberOfStockedElement()-1) {
				mCursorRow = 0;
				mCursorLine += 1;
			} else {
				mCursorRow = c.length();
			}
		}
	}

	public void crlf() {
		int index = getNumberOfStockedElement()-1;//+1;
		if(index > mCursorLine) {
			index = mCursorLine;
		}
		CharSequence c = get(mCursorLine);
		int row = c.length();
		if(row>mCursorRow) {
			row = mCursorRow;
		}

		if(row == c.length()) {
			mCursorLine += 1;
			mDiffer.addLine(mCursorLine, "\r\n");
			mCursorRow = 0;
		}
		else if(row ==0){
			mDiffer.setLine(mCursorLine, "\r\n");
			mCursorLine += 1;
			mCursorRow = 0;
			mDiffer.addLine(mCursorLine, c);
		}
		else {
			mDiffer.setLine(mCursorLine, c.subSequence(0, row)+"\r\n");
			mCursorLine += 1;
			mCursorRow = 0;
			mDiffer.addLine(mCursorLine, c.subSequence(row,c.length()));
			
		}
	}

	public void delete() {
		//if(true) {
		//	deleteLine();
		//	return;
		//}
		int index = getNumberOfStockedElement()-1;
		if(index > mCursorLine) {
			index = mCursorLine;
		}

		CharSequence l = get(index);
		// 指定された行が空
		if(l == null || l.length() == 0||
		  (l.length()==1&&l.subSequence(0, 1).equals("\n")) ||  
		  (l.length()==2&&l.subSequence(0, 2).equals("\r\n"))
		  ) {
			deleteLine();
			return;
		} 
		if(index==0&&mCursorRow<=0){
			return;
		}

		int row = mCursorRow;
		if(row>=l.length()) {
			row = l.length();
		}
		
		if((l.length()>=2&&l.length()-2<=row&&row<=l.length()
				&&l.charAt(l.length()-2) == '\r'
				&&l.charAt(l.length()-1) == '\n')){
			row =l.length()-2;
		}
		else if((l.length()>=1&&l.length()-1<=row&&row<=l.length()
				&&l.charAt(l.length()-1) == '\n')){
			row =l.length()-1;
		}

		//todo;
		if(row<=0&&index !=0) {
			CharSequence f = get(index-1);
			CharSequence e = get(index);
			mCursorLine = index-1;
			deleteLine();
			crlf();
			commit(""+f+e, index-1);
			return;
		}

		if(l.charAt(l.length()-1)=='\n'||index==getNumberOfStockedElement()-1) {
			CharSequence f = "";
			if(row>1){
				f= l.subSequence(0, row-1);
			} 
			CharSequence e = "";
			if(row < l.length()){
				e = l.subSequence(row, l.length());
			}
			mDiffer.setLine(index, ""+f+e);
			mCursorRow =row-1;
		} else {
			CharSequence f = l.subSequence(0, row-1);
			CharSequence e = "";
			if(row < l.length()){
				e = l.subSequence(row, l.length());
			}
			deleteLine();
			commit(""+f+e, index);
		}
		if(mCursorRow<0){
			mCursorRow=0;
		}
	}

	public void deleteLine() {
		int index = getNumberOfStockedElement();//+1;
		if(index >= mCursorLine) {
			index = mCursorLine;
		}
		android.util.Log.v("log","delete:"+index);
		mDiffer.deleteLine(index);	
		mCursorLine-=1;
		if(mCursorLine >=0){
			mCursorRow = get(mCursorLine).length();
		}else {
			mCursorRow =0;
		}
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
		if(mCursorRow >= ct.length()) {
			mCursorRow = ct.length();
		}
		CharSequence f = composeString(ct, mCursorRow, text);

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
					mCursorRow = 0;
				} else {
					mCursorRow += text.length();
//					mCursorRow = 0;
				}
			} else {
				mDiffer.addLine(mCursorLine, f.subSequence(0, len));				
				if(le>len) {
					mCursorLine+=1;
					mCursorRow += text.length();;
				} else {
					mCursorRow = len;
//					mCursorRow = 0;
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