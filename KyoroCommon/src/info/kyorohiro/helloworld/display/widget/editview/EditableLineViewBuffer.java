package info.kyorohiro.helloworld.display.widget.editview;

import java.util.LinkedList;

import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.editview.IMEClient;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.text.KyoroString;

public class EditableLineViewBuffer implements LineViewBufferSpec, IMEClient {

	private Differ mDiffer = new Differ();
	private LineViewBufferSpec mOwner = null;
	private int mCursorRow = 0;
	private int mCursorLine = 0;
	private boolean mIsCrlfMode = false;

	public EditableLineViewBuffer(LineViewBufferSpec owner) {
		super();
		mOwner = owner;
	}

	public Differ getDiffer() {
		return mDiffer;
	}

	public synchronized void clear() {
		mCursorRow = 0;
		mCursorLine = 0;
		mIsCrlfMode = false;
		mIsCrlfMode = false;
		mDiffer.clear();
	}
	public synchronized boolean isEdit() {
		return (mDiffer.lengthOfLine() <= 0 ? false : true);
	}

	public void IsCrlfMode(boolean isCrlfMode) {
		mIsCrlfMode = isCrlfMode;
	}

	@Override
	public synchronized void isSync(boolean isSync) {
		mOwner.isSync(isSync);
	}

	@Override
	public synchronized boolean isSync() {
		return mOwner.isSync();
	}

	@Override
	public synchronized int getNumOfAdd() {
		return mOwner.getNumOfAdd();
	}

	@Override
	public synchronized void clearNumOfAdd() {
		mOwner.clearNumOfAdd();
	}

	@Override
	public synchronized int getNumberOfStockedElement() {
		// android.util.Log.v("kiyo","len="+mDiffer.length() );
		return mOwner.getNumberOfStockedElement() + mDiffer.length();
	}

	private boolean isNormalize = false;

	@Override
	public synchronized KyoroString get(int i) {
		// android.util.Log.v("kiyo","sync="+isSync());
		if (!isSync() && isEOF(i) ){//&& !isNormalize) {
			// todo need to lock
			//isNormalize = true;
			//normalize(i);
			//isNormalize = false;
		}
		return mDiffer.get(mOwner, i);
	}

	@Override
	public BreakText getBreakText() {
		return mOwner.getBreakText();
	}

	public synchronized int getRow() {
		return mCursorRow;
	}

	public synchronized int getCol() {
		return mCursorLine;
	}

	@Override
	public void setCursor(int row, int col) {
		// this method Should belong to LIneView
		mCursorRow = row;
		mCursorLine = col;
		// android.util.Log.v("kiyo","#<N-1-01>----col="+getCol()+",row="+getRow());
		if (mCursorLine < 0) {
			// android.util.Log.v("kiyo","#<N-1-02>----col="+getCol()+",row="+getRow());
			mCursorLine = 0;
		} else if (mCursorLine >= getNumberOfStockedElement()) {
			// android.util.Log.v("kiyo","#<N-1-03>----col="+getCol()+",row="+getRow());
			mCursorLine = getNumberOfStockedElement() - 1;
			if (mCursorLine < 0) {
				// android.util.Log.v("kiyo","#<N-1-04>----col="+getCol()+",row="+getRow());
				mCursorLine = 0;
			}
		}
		// android.util.Log.v("kiyo","#<N-1-10>----col="+getCol()+",row="+getRow());
		if (mCursorLine < getNumberOfStockedElement()) {
			// android.util.Log.v("kiyo","#<N-1-11>----col="+getCol()+",row="+getRow());
			KyoroString c = get(mCursorLine);
			if (mCursorRow < 0) {
				// android.util.Log.v("kiyo","#<N-1-12>----col="+getCol()+",row="+getRow());
				mCursorRow = 0;
			} else if (mCursorRow > c.lengthWithoutLF(mIsCrlfMode)) {
				// android.util.Log.v("kiyo","#<N-1-13>----col="+getCol()+",row="+getRow());
				mCursorRow = c.lengthWithoutLF(mIsCrlfMode);
			}
		} else {
			// android.util.Log.v("kiyo","#<N-1-21>----col="+getCol()+",row="+getRow());
			mCursorRow = 0;
		}
	}

	public synchronized void crlf() {
		crlf(true, false);
	}

	public synchronized void crlf(boolean addLf, boolean removelf) {
		String lf = "";
		if (addLf) {
			if (mIsCrlfMode) {
				lf = "\r\n";
			} else {
				lf = "\n";
			}
		}

		int index = getNumberOfStockedElement() - 1;// +1;
		if (index < 0) {
			index = 0;
		}
		if (index > mCursorLine) {
			index = mCursorLine;
		}
		// mCursorLine = index;
		int row = 0;
		KyoroString c = null;
		if (mCursorLine < getNumberOfStockedElement()) {
			// todo
			c = get(mCursorLine);
			row = c.length();
			// return;
		} else {
			// todo
			c = new KyoroString("");
		}

		if (row > mCursorRow) {
			row = mCursorRow;
		}

		if (row == 0) {
			mDiffer.setLine(mCursorLine, lf);
			mCursorLine += 1;
			mCursorRow = 0;

			if (c.includeLF()) {
				mDiffer.addLine(mCursorLine, c);
			} else {
				int _col = mCursorLine;
				int _row = mCursorRow;
				commit(c, 1);
				setCursor(_row, _col);
			}
		} else {
			mDiffer.setLine(mCursorLine, c.subSequence(0, row) + lf);
			mCursorLine += 1;
			mCursorRow = 0;
			if (c.includeLF()) {
				mDiffer.addLine(mCursorLine, c.subSequence(row, c.length()));
			} else {
				int _col = mCursorLine;
				int _row = mCursorRow;
				commit(c.subSequence(row, c.length()), mCursorLine);
				setCursor(_row, _col);
			}
		}
	}

	private synchronized boolean isEOF(int index) {
		int length = getNumberOfStockedElement();
		index += 1;
		if (index < length || isLoading()) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized void normalize(int index) {
		boolean isEOF = isEOF(index);
		if (isEOF) {
			KyoroString line = get(index);
			if (line.isNowLoading()) {
				return;
			}
			if (line.includeLF()) {
				mDiffer.addLine(index + 1, "");
//				mIsNormalized = true;
			}
		}
	}

	public synchronized void deleteChar() {
		// int col = getCol();
		// int row = getRow();
		// android.util.Log.v("test","#deleteChar#-1r="+getRow()+",c="+getCol());
		if (getCol() == 0 && getRow() == 0) {
			return;
		}
		// moveCursor(-1);
		setCursorBack(getRow() - 1, getCol());
		if (get(getCol()).length() == getRow()) {
			//android.util.Log.v("test","#deleteChar#-2r="+getRow()+",c="+getCol());
			setCursorBack(getRow() - 1, getCol());
		}
		//android.util.Log.v("test","#deleteChar#-3r="+getRow()+",c="+getCol());
		backwardDeleteChar();
		//android.util.Log.v("test","#deleteChar#-4r="+getRow()+",c="+getCol());
	}

	// copy EditableLineView
	public void setCursorBack(int row, int col) {
		int _rowTmp = row;
		int _colTmp = col;
		if (_rowTmp < 0) {
			// �ｽﾚ難ｿｽ�ｽ�ｽ�ｽ�ｽB
			if (_colTmp > 0) {
				_colTmp -= 1;
				KyoroString cc = this.get(_colTmp);
				_rowTmp = cc.lengthWithoutLF(mIsCrlfMode);
			} else {
				_rowTmp = 0;
			}
		}
		setCursorAndCRLF(_rowTmp, _colTmp);
	}

	// copy EditableLineView
	public void setCursorAndCRLF(int row, int col) {
		// this method Should belong to LIneView
		int _rowTmp = row;
		int _colTmp = col;
		if (_colTmp < 0) {
			_colTmp = 0;
		} else if (_colTmp >= this.getNumberOfStockedElement()) {
			_colTmp = this.getNumberOfStockedElement();
		}

		KyoroString c = this.get(_colTmp);
		if (_rowTmp < 0) {
			_rowTmp = 0;
		} else if (_rowTmp > c.lengthWithoutLF(mIsCrlfMode)) {
			_rowTmp = c.lengthWithoutLF(mIsCrlfMode);
		}
		mCursorLine = _colTmp;
		mCursorRow = _rowTmp;
	}

	public synchronized void deleteLinePerVisible() {
		int index = getNumberOfStockedElement();// +1;
		if (index == 0) {
			return;
		}
		if (index >= mCursorLine) {
			index = mCursorLine;
		}
		mDiffer.deleteLine(index);
		mCursorLine -= 1;
		mCursorRow = 0;
		setCursor(mCursorRow, mCursorLine);

	}

	public synchronized void backwardDeleteChar() {
		//android.util.Log.v("kiyo","#backwardDeleteChar#<0>----------------");
		if (0 >= getNumberOfStockedElement()) {
			return;
		}
		// todo normalize before get cursorposition
		int index = getCol();
		int row = getRow();
		//android.util.Log.v("kiyo","#backwardDeleteChar#<0>----------index="+index+",row="+row);
		KyoroString text = get(index);
		KyoroString next = null;
		KyoroString prev = null;
		if (index + 1 < getNumberOfStockedElement()) {
			next = get(index + 1);
		}
		if (index - 1 >= 0) {
			prev = get(index - 1);
		}

		if ((text.includeLF() && row <= text.lengthWithoutLF(mIsCrlfMode))
				|| (!text.includeLF() && row < text
						.lengthWithoutLF(mIsCrlfMode))) {
			//android.util.Log.v("kiyo","#backwardDeleteChar#<A>----------------");
			deleteLinePerVisible();
			setCursor(0, index);
			if (row + 1 <= text.lengthWithoutLF(mIsCrlfMode)) {
				//android.util.Log.v("kiyo","#backwardDeleteChar#<A-1>----------------");
				if (text.includeLF()) {
					//android.util.Log.v("kiyo","#backwardDeleteChar#<A-1-1>----col="+getCol()+",row="+getRow());
					crlf(true, false);
					//android.util.Log.v("kiyo","#backwardDeleteChar#<A-1-2>----col="+getCol()+",row="+getRow());
					setCursor(0, index);
					//android.util.Log.v("kiyo","#backwardDeleteChar#<A-1-3>----col="+getCol()+",row="+getRow());
				} else {
					if (getCol() < index) {
						//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-1>----col="+getCol()+",row="+getRow());
						if (prev == null) {
							//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-2>----col="+getCol()+",row="+getRow());
							setCursor(0, 0);
						} else {
							if (!prev.includeLF()) {
								//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-3>----col="+getCol()+",row="+getRow());
								setCursor(prev.lengthWithoutLF(mIsCrlfMode),
										getCol());
							} else {
								//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-4>----col="+getCol()+",row="+getRow());
								// crlf(true, false);
								normalize(getCol());// todo set index OtOfBounce
								//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-5>----col="+getCol()+",row="+getRow());
								setCursor(0, index);
							}
							// setCursor(0, getCol());
							//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-6>----col="+getCol()+",row="+getRow());
						}
					} else {
						//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-7>----col="+getCol()+",row="+getRow());
						setCursor(0, index);
						//android.util.Log.v("kiyo","#backwardDeleteChar#<A-2-8>----col="+getCol()+",row="+getRow());
					}
				}
				//android.util.Log.v("kiyo","#backwardDeleteChar#<A-G1-10>----col="+getCol()+",row="+getRow());
				commit(""
						+ text.subSequence(0, row)
						+ text.subSequence(row + 1,
								text.lengthWithoutLF(mIsCrlfMode)), 1);
				// android.util.Log.v("kiyo","#<A-1-11>----col="+getCol()+",row="+getRow());
				// normalize(getCol());
				// 別メソッドにしたほうが良いかも
				if (index >= getNumberOfStockedElement()) {
					index = getNumberOfStockedElement() - 1;
					if (prev == null) {
						row = 0;
					} else {
						row = prev.lengthWithoutLF(mIsCrlfMode);
					}
				}
				setCursor(row, index);

				//android.util.Log.v("kiyo","#backwardDeleteChar#<A-1-10>----col="+getCol()+",row="+getRow());
			} else {
				 //android.util.Log.v("kiyo","#backwardDeleteChar#<A-2>----------------");
				setCursor(0, index);
				commit("" + text.subSequence(0, row), 1);
				setCursor(row, index);
			}
			// android.util.Log.v("kiyo","#</A>----------------");
			// setCursor(row, index);
		} else {
			//android.util.Log.v("kiyo","#backwardDeleteChar#<B minus>");
			if (next != null) {
				//android.util.Log.v("kiyo","#backwardDeleteChar#<B>------col="+getCol()+",row="+getRow());
				if (index < getMaxOfStackedElement()) {
					// android.util.Log.v("kiyo","#<-->----------------");
					// android.util.Log.v("kiyo","#<B-1>------col="+getCol()+",row="+getRow());
					setCursor(0, index + 1);
					// android.util.Log.v("kiyo","#<B-2>------col="+getCol()+",row="+getRow());
					backwardDeleteChar();
					// android.util.Log.v("kiyo","#<B-3>------col="+getCol()+",row="+getRow());
					setCursor(row, index);
					// android.util.Log.v("kiyo","#<B-4>------col="+getCol()+",row="+getRow());
				}
				// android.util.Log.v("kiyo","#<B/>------col="+getCol()+",row="+getRow());
			}
		}
	}

	private void log(String log) {
//		android.util.Log.v("kiyo", "elvb:" + log);
	}


	public synchronized void yank_debug() {
		log("debug=#yank----begin-");
		for (String line : mYankBuffer) {
			log("debug=#" + line + "#");
		}
		log("debug=#yank----end-r=" + getRow() + ",c=" + getCol());
	}

	public synchronized void get_debug() {
		log("debug=#get----begin-");
		for (int i = 0; i < getNumberOfStockedElement(); i++) {
			log("debug=#[" + i + "]" + get(i) + "#");
		}
		log("debug=get----end-r=" + getRow() + ",c=" + getCol());
	}

	public int debugGetYankSize() {
		return mYankBuffer.size();
	}

	public String debugGetYank(int i) {
		return mYankBuffer.get(i);
	}

	//
	// TODO following yank
	//
	private static boolean mYankBufferClear = false;
	private static LinkedList<String> mYankBuffer = new LinkedList<String>();
	public static synchronized void clearYank() {
		mYankBufferClear = true;
	}
	public synchronized void yank() {
		try {
			for (String line : mYankBuffer) {
				if (line.endsWith("\n")) {
					log("-#-1--" + line);
					crlf();
				} else if (line.length() != 0) {
					log("-#-2--" + line);
					pushCommit(line, 1);
				}
			}
		} finally {
			mYankBufferClear = true;
		}
	}

	public static void appendYankBuffer(String text) {
		if(mYankBufferClear) {
			mYankBuffer.clear();
			mYankBufferClear = false;
		}
		mYankBuffer.add(text);		
	}

	public synchronized void killLine() {
		if (0 >= getNumberOfStockedElement()) {
			return;
		}
		int index = getCol();
		int row = getRow();
		KyoroString text = get(index);

		// crlf only or empty
		if (text.lengthWithoutLF(mIsCrlfMode) == 0) {
			deleteLinePerVisible();
			setCursor(row, index);
			appendYankBuffer(""+text);
		}
		// end of line without crlf
		else if (text.lengthWithoutLF(mIsCrlfMode) == mCursorRow) {
			// delete crlf
			if (text.includeLF()) {
				backwardDeleteChar();
				appendYankBuffer(""
						+ text.subSequence(text.lengthWithoutLF(mIsCrlfMode),
								text.length()));
				// mYankBuffer.add("\r\n");
			}
			// delete nextline
			else {
				if (index + 1 < getNumberOfStockedElement()) {
					setCursor(0, index + 1);
					killLine();
				}
			}
			setCursor(row, index);
		}
		// other
		else {
			deleteLinePerVisible();
			if (row >= text.length()) {
				row = text.length();
			}
			setCursor(0, index);
			if (text.includeLF()) {
				crlf(true, false);
			}
			if(index<getNumberOfStockedElement()) {
				setCursor(0, index);
			} else if(index-1>=0){
				setCursor(get(index-1).lengthWithoutLF(mIsCrlfMode), index-1);
			}
			commit(text.subSequence(0, row), 1);
			setCursor(row, index);
			appendYankBuffer(""
					+ text.subSequence(row, text.lengthWithoutLF(mIsCrlfMode)));
		}
		normalize(getCol());
		setCursor(row, index);
	}

	@Override
	public synchronized void pushCommit(CharSequence text, int cursor) {
		int index = getNumberOfStockedElement();// +1;
		if (index > mCursorLine) {
			index = mCursorLine;
		}

		CharSequence l = "";
		if (index < getNumberOfStockedElement()) {
			l = get(index);
		}
		if (mCursorRow > l.length()) {
			mCursorRow = l.length();
		}
		if (0 <= mCursorRow - 1 && l.charAt(mCursorRow - 1) == '\n') {
			mCursorRow -= 1;
			if (mIsCrlfMode && 0 <= mCursorRow - 1
					&& l.charAt(mCursorRow - 1) == '\r') {
				mCursorRow -= 1;
			}
		}
		commit(text, cursor);
	}

	@Override
	public synchronized int getMaxOfStackedElement() {
		return mOwner.getMaxOfStackedElement();
	}

	private final KyoroString EMPTY = new KyoroString("");

	private synchronized void moveCursor(int move) {
		mCursorRow += move;
		KyoroString c = EMPTY;// <--- 無駄
		if (mCursorLine >= getNumberOfStockedElement()) {
			return;
		} else if (mCursorLine < 0) {
			return;
		}

		if (move > 0) {
			while (true) {
				c = get(mCursorLine);
				if (c == null) {
					c = EMPTY;
				}
				if (mCursorRow <= c.length()) {
					return;
				}
				if (mCursorLine >= getNumberOfStockedElement()) {
					// setCursor(mCursorRow, mCursorLine);
					return;
				}
				mCursorLine += 1;
				mCursorRow = mCursorRow - c.length();
			}
		} else if (move < 0) {
			// 未実装
			// このメソッドはなくなる予定
		}

	}

	// todo rewrite!!
	private synchronized void commit(CharSequence text, int cursor) {
		//android.util.Log.v("kiyo", "#commit#[-0-]col="+mCursorLine+",row="+mCursorRow+","+text);
		// ---------
		if (mCursorLine <= 0) {
			mCursorLine = 0;
		}
		if (mCursorRow <= 0) {
			mCursorRow = 0;
		}
		// ---------
		int cursorMove = text.length();
		if (cursor <= 0) {
			cursorMove = 0;
		}

		int cursorRow = mCursorRow;
		int cursorLine = mCursorLine;
		try {
			if (text.length() > 0 && text.charAt(text.length() - 1) == '\n') {
				if (mIsCrlfMode && text.length() > 1
						&& text.charAt(text.length() - 2) == '\r') {
					text = text.subSequence(0, text.length() - 2);
				} else {
					text = text.subSequence(0, text.length() - 1);
				}
			}
			// / todo delete
			// / if (0 >= getNumberOfStockedElement()) {
			// / if (mIsCrlfMode) {
			// / mDiffer.addLine(0, "\r\n");
			// / } else {
			// / mDiffer.addLine(0, "\n");
			// / }
			// / }

			CharSequence ct = "";
			if (cursorLine < getNumberOfStockedElement()) {
				ct = get(cursorLine);
			}
			if (cursorRow >= ct.length()) {
				cursorRow = ct.length();
			}
			// android.util.Log.v("kiyo", "__f0=" + ct+","+".");
			KyoroString f = composeString(ct, cursorRow, text);
			// android.util.Log.v("kiyo", "__f0=" +
			// f+","+cursorLine+"."+cursor);

//			int w = this.getBreakText().getWidth();
			int breakLinePoint = 0;
			int currentLineLength = 0;
			boolean hasAlreadyExecuteFirstAction = false;
//			int i = 0;
			int g = 0;
			do {
//				i++;
				// android.util.Log.v("kiyo", "_aaa1=" + f.length() + "," + w +
				// ","+ f);
				// breakLinePoint = getBreakText().breakText(f, 0, f.length(),
				// w);

				// todo
				//
				// �ｽﾆりあ�ｽ�ｽ�ｽ�ｽ�ｽA�ｽ�ｽﾒ通ゑｿｽﾉ難ｿｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽm�ｽF�ｽ�ｽ�ｽ驍ｱ�ｽ�ｽ!!
				//
				breakLinePoint = BreakText.breakText(getBreakText(), f, 0,
						f.length());
				// android.util.Log.v("kiyo",
				// "f0 len="+breakLinePoint+","+f.length());
				currentLineLength = f.length();
				// android.util.Log.v("kiyo", "_aaa0=" +
				// i+",row="+mCursorRow+",col="+mCursorLine);
				// android.util.Log.v("kiyo", "_aaa1=" +
				// i+",row="+(currentLineLength+g)+">"+breakLinePoint+","+g);
				// android.util.Log.v("kiyo", "_aaa0=" + i+","+f);
				if (f.length() > 0 && f.charAt(f.length() - 1) == '\n') {
					if (mIsCrlfMode && f.length() > 1
							&& f.charAt(f.length() - 2) == '\r') {
						g = -2;
					} else {
						g = -1;
					}
				} else {
					g = 0;
				}
				// if (!hasAlreadyExecuteFirstAction) {

				if (g >= 0) {
					g = 0;
					// .util.Log.v("kiyo",
					// "f0 line="+cursorLine+","+getNumberOfStockedElement());
					if (cursorLine < getNumberOfStockedElement()) {
						mDiffer.setLine(cursorLine,
								f.subSequence(0, breakLinePoint));
					} else {
						mDiffer.addLine(cursorLine,
								f.subSequence(0, breakLinePoint));
					}
					// if(true){
					// break;
					// }
					if ((currentLineLength + g) > breakLinePoint) {
						cursorLine += 1;
						cursorRow = 0;
						// android.util.Log.v("kiyo", "_aaa2 break=" +
						// breakLinePoint +",curre="+currentLineLength);
						f = f.newKyoroString(breakLinePoint, currentLineLength);
						if (f.length() > 0 && f.charAt(f.length() - 1) != '\n'
								&& cursorLine < getNumberOfStockedElement()) {
							f = new KyoroString("" + f + get(cursorLine));
						}
					} else {
						cursorRow += text.length();
						break;
					}
				} else {
					g = 0;
					if (currentLineLength > breakLinePoint) {
						mDiffer.addLine(cursorLine,
								f.subSequence(0, breakLinePoint));
						cursorLine += 1;
						cursorRow += text.length();
						// android.util.Log.v("kiyo", "_aaa2 break=" +
						// breakLinePoint +",curre="+currentLineLength);
						f = f.newKyoroString(breakLinePoint, currentLineLength);
					} else {
						if (!hasAlreadyExecuteFirstAction) {
							mDiffer.setLine(cursorLine,
									f.subSequence(0, breakLinePoint));
						} else {
							mDiffer.addLine(cursorLine,
									f.subSequence(0, breakLinePoint));
							hasAlreadyExecuteFirstAction = true;
						}
						cursorRow = breakLinePoint;
						break;
					}
				}
			} while ((currentLineLength + g) > breakLinePoint && f.length() > 0);
		} finally {
			moveCursor(cursorMove);
		}
	}

	public static KyoroString composeString(CharSequence b, int i,
			CharSequence s) {
		if (i >= b.length()) {
			i = b.length();
		}
		if (i == 0) {
			return new KyoroString("" + s + b);
		} else if (i == b.length()) {
			return new KyoroString("" + b + s);
		} else {
			CharSequence f = b.subSequence(0, i);
			CharSequence e = b.subSequence(i, b.length());
			return new KyoroString("" + f + s + e);
		}
	}

	@Override
	public synchronized void dispose() {
		mOwner.dispose();
	}

	@Override
	public boolean isLoading() {
		return mOwner.isLoading();
	}

}