package info.kyorohiro.helloworld.display.widget.lineview.edit;

import android.test.MoreAsserts;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;

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
	public void isSync(boolean isSync) {
		mOwner.isSync(isSync);
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
	public KyoroString get(int i) {
		return mDiffer.get(mOwner, i);
	}

	@Override
	public BreakText getBreakText() {
		return mOwner.getBreakText();
	}

	@Override
	public KyoroString[] getElements(KyoroString[] ret, int start, int end) {
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
		if (mCursorLine < 0) {
			mCursorLine = 0;
		} else if (mCursorLine >= getNumberOfStockedElement()) {
			mCursorLine = getNumberOfStockedElement() - 1;
		}

		CharSequence c = get(mCursorLine);
		if (mCursorRow < 0) {
			// 移動する。
			if (mCursorLine > 0) {
				mCursorLine -= 1;
				CharSequence cc = get(mCursorLine);
				mCursorRow = cc.length();
			} else {
				mCursorRow = 0;
			}
		} else if (mCursorRow > c.length()) {
			// 移動する。
			if (mCursorLine < getNumberOfStockedElement() - 1) {
				mCursorRow = 0;
				mCursorLine += 1;
			} else {
				mCursorRow = c.length();
			}
		}
	}

	public void crlf() {
		crlf(true);
	}
	public void crlf(boolean addLf) {
		String lf = "";
		if(addLf){
			lf = "\r\n";
		}

		int index = getNumberOfStockedElement() - 1;// +1;
		if(index<0){
			index=0;
		}
		if (index > mCursorLine) {
			index = mCursorLine;
		}
		CharSequence c = get(mCursorLine);
		int row = c.length();
		if (row > mCursorRow) {
			row = mCursorRow;
		}

		if (row == c.length()) {
			mCursorLine += 1;
			mDiffer.addLine(mCursorLine, lf);
			mCursorRow = 0;
		} else if (row == 0) {
			mDiffer.setLine(mCursorLine, lf);
			mCursorLine += 1;
			mCursorRow = 0;
			mDiffer.addLine(mCursorLine, c);
		} else {
			mDiffer.setLine(mCursorLine, c.subSequence(0, row) + lf);
			mCursorLine += 1;
			mCursorRow = 0;
			mDiffer.addLine(mCursorLine, c.subSequence(row, c.length()));

		}
	}

	public void delete() {
		int index = getNumberOfStockedElement() - 1;
		if (index > mCursorLine) {
			index = mCursorLine;
		}

		CharSequence l = get(index);
		// 指定された行が空
		if (l == null || l.length() == 0
				|| (l.length() == 1 && l.subSequence(0, 1).equals("\n"))
				|| (l.length() == 2 && l.subSequence(0, 2).equals("\r\n"))) {
			deleteLinePerVisible();
			return;
		}
		if (index == 0 && mCursorRow <= 0) {
			return;
		}

		int row = mCursorRow;
		if (row >= l.length()) {
			row = l.length();
		}

		if ((l.length() >= 2 && l.length() - 2 <= row && row <= l.length()
				&& l.charAt(l.length() - 2) == '\r' && l.charAt(l.length() - 1) == '\n')) {
			row = l.length() - 2;
		} else if ((l.length() >= 1 && l.length() - 1 <= row
				&& row <= l.length() && l.charAt(l.length() - 1) == '\n')) {
			row = l.length() - 1;
		}

		// todo;
		if (row <= 0 && index > 0) {
			CharSequence f = get(index - 1);
			CharSequence e = get(index);
			mCursorLine = index;
			int br = f.length();
			int bc = mCursorLine - 1;
			if(mCursorLine<0){
			mCursorLine = 0;
			}
			deleteLinePerVisible();
			deleteLinePerVisible();
			if(mCursorLine<0){
				mCursorLine=0;
				crlf();
				mCursorLine=0;
			} else {
				if(f.length()>1&&f.charAt(f.length()-1)=='\n'){
					crlf();
				} else {
					crlf(false);
				}
			}
			// deleteLine();
			///*
			if(f.length()>0&&f.charAt(f.length()-1)=='\n'){
				if(f.length()>1&&f.charAt(f.length()-2)=='\r'){
					f = f.subSequence(0, f.length()-2);
				}else {
					f = f.subSequence(0, f.length()-1);					
				}
				br = f.length();
			}
			//if(e.length()>0&&e.charAt(f.length()-1)=='\n'){
			//	if(e.length()>1&&e.charAt(e.length()-2)=='\r'){
			//		e = e.subSequence(0, e.length()-2);
			//	}else {
			//		e = e.subSequence(0, e.length()-1);					
			//	}
			//}
			commit("" + f + e, 999);//*/
			setCursor(br, bc);
			return;
		}

		if (l.charAt(l.length() - 1) == '\n'
				|| index == getNumberOfStockedElement() - 1) {
			CharSequence f = "";
			if (row > 1) {
				f = l.subSequence(0, row - 1);
			}
			CharSequence e = "";
			if (row < l.length()) {
				e = l.subSequence(row, l.length());
			}
			mDiffer.setLine(index, "" + f + e);
			mCursorRow = row - 1;
		} else {
			CharSequence f = l.subSequence(0, row - 1);
			CharSequence e = "";
			if (row < l.length()) {
				e = l.subSequence(row, l.length());
			}
			int br = row - 1;
			int bc = mCursorLine;
			deleteLinePerVisible();
			if (mCursorLine + 1 < getNumberOfStockedElement()) {
				mCursorLine += 1;
				mCursorRow = 0;
			}
			// crlf();
			// mCursorC =row-1;
			commit("" + f + e, index);
			setCursor(br, bc);
		}
		if (mCursorRow < 0) {
			mCursorRow = 0;
		}
	}

	public void deleteLinePerVisible() {
		int index = getNumberOfStockedElement();// +1;
		if(index == 0){
			return;
		}
		if (index >= mCursorLine) {
			index = mCursorLine;
		}
		CharSequence deleted = get(index);
		mDiffer.deleteLine(index);
		mCursorLine -= 1;
		if (mCursorLine >= 0) {
			mCursorRow = get(mCursorLine).length();
		} else {
			mCursorRow = 0;
		}
		CharSequence c = get(mCursorLine);
		if (deleted.charAt(deleted.length() - 1) == '\n'
				&& c.charAt(c.length() - 1) != '\n') {
			mDiffer.setLine(mCursorLine, "" + c + "\n");
		}
		setCursor(mCursorRow, mCursorLine);

	}

	@Override
	public void pushCommit(CharSequence text, int cursor) {
		// android.util.Log.v("log","col="+mCursorCol + ",row="+mCursorRow);
		android.util.Log.v("kiyo", "[-b1-]col="+mCursorLine+",row="+mCursorRow);
		int index = getNumberOfStockedElement();// +1;
		if (index > mCursorLine) {
			index = mCursorLine;
		}

		CharSequence l = "";
		android.util.Log.v("kiyo", "[-b2-]col="+index+",numof="+getNumberOfStockedElement());
		if(index<getNumberOfStockedElement()){
			l = get(index);
		}
		android.util.Log.v("kiyo", "[-b3-]len="+l.length()+","+l);
		if(mCursorRow > l.length()){
			mCursorRow = l.length();
		}
		android.util.Log.v("kiyo", "[-d1-]col="+mCursorLine+",row="+mCursorRow);
		//if (l.length() < mCursorRow || l.charAt(mCursorRow) == '\n') {
		//	mCursorRow -= 1;
		//}
		android.util.Log.v("kiyo", "[-d1-]col="+mCursorLine+",row="+mCursorRow);
		android.util.Log.v("kiyo", "[-a-]col="+mCursorLine+",row="+mCursorRow);
		commit(text, cursor);
	}

	@Override
	public int getMaxOfStackedElement() {
		return mOwner.getMaxOfStackedElement();
	}

	private void moveCursor(int move) {
		android.util.Log.v("kiyo", "[-1-]col="+mCursorLine+",row="+mCursorRow);
		mCursorRow += move;
		CharSequence c = "";
		if(mCursorLine>=getNumberOfStockedElement()){
			return;
		}
		while(true) {
			c = get(mCursorLine);
			android.util.Log.v("kiyo", "[-2-]col="+mCursorLine+",row="+mCursorRow+",len="+c.length()+","+c);
			if(c==null){
				c="";
			}
			if(mCursorRow <= c.length()){
				return;
			}
			if(mCursorLine >= getNumberOfStockedElement()){
				//setCursor(mCursorRow, mCursorLine);
				return;
			}
			mCursorLine += 1;
			mCursorRow = mCursorRow-c.length();
		}
	}
	//commit textに\nがふくまれる場合はどうかするはてな
	private void commit(CharSequence text, int cursor) {
		android.util.Log.v("kiyo", "[-0-]col="+mCursorLine+",row="+mCursorRow);
		//---------
		if(mCursorLine<=0) {
			mCursorLine = 0;
		}
		if(mCursorRow<=0){
			mCursorRow = 0;
		}
		//---------
		int cursorMove = text.length();
		if(cursor<=0){
			cursorMove = 0;
		}

		int cursorRow = mCursorRow;
		int cursorLine = mCursorLine;
		try {
			if(text.length()>0&&text.charAt(text.length()-1)=='\n'){
				if(text.length()>1&&text.charAt(text.length()-2)=='\r'){
					text = text.subSequence(0, text.length()-2);
				}else {
					text = text.subSequence(0, text.length()-1);					
				}
			}
			if(0>=getNumberOfStockedElement()){
				mDiffer.addLine(0, "\n");
			}

			CharSequence ct = "";
			if(0<getNumberOfStockedElement()){
				ct=get(cursorLine);
			}
			if (cursorRow >= ct.length()) {
				cursorRow = ct.length();
			}
			CharSequence f = composeString(ct, cursorRow, text);
			android.util.Log.v("kiyo", "_f0=" + f+","+cursorLine+"."+cursor);

			int w = this.getBreakText().getWidth();
			int breakLinePoint = 0;
			int currentLineLength = 0;
			boolean hasAlreadyExecuteFirstAction = false;
			int i = 0;
			int g = 0;
			do {
				i++;
				//android.util.Log.v("kiyo", "_aaa1=" + f.length() + "," + w + ","+ f);
				breakLinePoint = getBreakText().breakText(f, 0, f.length(), w);
				currentLineLength = f.length();
				//android.util.Log.v("kiyo", "_aaa0=" + i+",row="+mCursorRow+",col="+mCursorLine);
				//android.util.Log.v("kiyo", "_aaa1=" + i+",row="+(currentLineLength+g)+">"+breakLinePoint+","+g);
				//android.util.Log.v("kiyo", "_aaa0=" + i+","+f);
				if(f.length()>0&&f.charAt(f.length()-1)=='\n'){
					if(f.length()>1&&f.charAt(f.length()-2)=='\r'){
						g=-2;
					}else {
						g=-1;				
					}
				} else {
					g = 0;
				}
				//			if (!hasAlreadyExecuteFirstAction) {

				if(g>=0){
					g=0;
					mDiffer.setLine(cursorLine, f.subSequence(0, breakLinePoint));
					if ((currentLineLength+g) > breakLinePoint) {
						cursorLine += 1;
						cursorRow = 0;
						android.util.Log.v("kiyo", "_aaa2 break=" + breakLinePoint +",curre="+currentLineLength);
						f = f.subSequence(breakLinePoint, currentLineLength);
						if(f.length()>0&&f.charAt(f.length()-1)!='\n'&&cursorLine<getNumberOfStockedElement()) {
							f = ""+f+get(cursorLine);
						}
					} else {
						cursorRow += text.length();
						break;
					}
				} else {
					g=0;
					if (currentLineLength > breakLinePoint) {
						mDiffer.addLine(cursorLine, f.subSequence(0, breakLinePoint));
						cursorLine += 1;
						cursorRow += text.length();
						android.util.Log.v("kiyo", "_aaa2 break=" + breakLinePoint +",curre="+currentLineLength);
						f = f.subSequence(breakLinePoint, currentLineLength);
					} else {
						if(!hasAlreadyExecuteFirstAction) {
							mDiffer.setLine(cursorLine, f.subSequence(0, breakLinePoint));					
						} else {
							mDiffer.addLine(cursorLine, f.subSequence(0, breakLinePoint));
							hasAlreadyExecuteFirstAction = true;
						}
						cursorRow = breakLinePoint;
						break;
					}
				}
			} while ((currentLineLength+g) > breakLinePoint&&f.length()>0);
		} finally {
			moveCursor(cursorMove);
		}
	}

	public static CharSequence composeString(CharSequence b, int i,
			CharSequence s) {
		if (i >= b.length()) {
			i = b.length();
		}
		//----------
		//if(i<0){
		//	i=0;
		//}
		//------
		if (i == 0) {
			return "" + s + b;
		} else if (i == b.length()) {
			return "" + b + s;
		} else {
			CharSequence f = b.subSequence(0, i);
			CharSequence e = b.subSequence(i, b.length());
			return "" + f + s + e;
		}
	}

	@Override
	public void dispose() {
		mOwner.dispose();
	}

}