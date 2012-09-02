package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BigLineData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.util.LockableCyclingList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Color;

public class TextViewerBuffer extends LockableCyclingList implements LineViewBufferSpec {
	private BigLineData mLineManagerFromFile = null;
	private int mCurrentBufferStartLinePosition = 0;
	private int mCurrentBufferEndLinePosition = 0;
	private int mCurrentPosition = 0;
	private LineViewData mErrorLineMessage = new LineViewData("..", Color.RED, LineViewData.INCLUDE_END_OF_LINE);
	private LineViewData mLoadingLineMessage = new LineViewData("loading..", Color.parseColor("#33FFFF00"),LineViewData.INCLUDE_END_OF_LINE);
	private LookAheadCaching mCashing = null;
	private int mNumberOfStockedElement = 0;

	public TextViewerBuffer(int listSize, BreakText breakText, File path, String charset) throws FileNotFoundException {
		super(listSize);
		mLineManagerFromFile = new BigLineData(path, charset, breakText);
		mCashing = new LookAheadCaching(this);
	}

	public BigLineData getBigLineData() {
		return mLineManagerFromFile;
	}

	public synchronized int getNumberOfStockedElement() {
		return mNumberOfStockedElement;
	}

	public synchronized int getCurrentBufferStartLinePosition() {
		return mCurrentBufferStartLinePosition;
	}

	public synchronized int getCurrentBufferEndLinePosition() {
		return mCurrentBufferEndLinePosition;
	}

	public synchronized int getCurrentPosition() {
		return mCurrentPosition;
	}

	public void startReadFile() {
		mCashing.startReadForward(-1);
	}

	public void startReadFile(int pos) {
		mCashing.startReadForward(pos);
	}

	public void dispose() {
//		android.util.Log.v("kiyo","buffer =dispose()");
		if (null != mLineManagerFromFile) {
			try {
				if(mCashing != null){
					mCashing.dispose();
//					mCashing.stopTask();
					mCashing = null;
				}
				mLineManagerFromFile.close();
				mLineManagerFromFile = null;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void head(LineViewData element) {
		int num = getNumOfAdd();
		super.head(element);
		if (element instanceof MyBufferDatam) {
			setNumOfAdd(num);
		}
	}

	@Override
	public synchronized void add(LineViewData element) {
		int num = getNumOfAdd();
		super.add(element);
		if (element instanceof MyBufferDatam) {
			MyBufferDatam b = (MyBufferDatam)element;
			int pos = b.getLinePosition();
			if(mNumberOfStockedElement<(pos+1)) {
				mNumberOfStockedElement = pos+1;
				setNumOfAdd(num+1);
			}
			else {
				setNumOfAdd(num);				
			}
		}
	}

	public synchronized LineViewData get(int i) {
		if (i < 0) {
			return mErrorLineMessage;
		}
		try {
			resetBufferedStartEndPosition(i);
			if(!lineIsLoaded(i)||super.getNumberOfStockedElement()<=lineNumberToBufferedNumber(i)) {
				return mLoadingLineMessage;
			}

			LineViewData bufferedDataForReturn = super.get(lineNumberToBufferedNumber(i));
			if (bufferedDataForReturn == null) {
			//	android.util.Log.v("kiyo","ERROR --2--");
				return mErrorLineMessage;
			}
			return bufferedDataForReturn;
		} catch (Throwable t) {
		//	android.util.Log.v("kiyo","ERROR --1--");
			t.printStackTrace();
			return mErrorLineMessage;
		} finally {
			if (mCashing != null) {
				mCashing.updateBufferedStatus();
			}
		}
	}

	private int lineNumberToBufferedNumber(int lineNumber) {
		int ret = lineNumber - mCurrentBufferStartLinePosition;
		return ret;
	}

	private boolean lineIsLoaded(int lineNumber) {
		int bufferSize = super.getNumberOfStockedElement();
		int bufferedPoaition = lineNumberToBufferedNumber(lineNumber);
		if (bufferedPoaition < 0 || bufferSize < bufferedPoaition) {
			return false;
		} else {
			return true;
		}
	}

	private void resetBufferedStartEndPosition(int lineNumber) {
		mCurrentPosition = lineNumber;
		int bufferSize = super.getNumberOfStockedElement();
		if (0 < bufferSize) {
			CharSequence startLine = super.get(0);
			CharSequence endLine = super.get(bufferSize - 1);
			MyBufferDatam startLineWithPosition = (MyBufferDatam) startLine;
			MyBufferDatam endLineWithPosition = (MyBufferDatam) endLine;
			mCurrentBufferStartLinePosition = (int) startLineWithPosition.getLinePosition();
			mCurrentBufferEndLinePosition = (int) endLineWithPosition.getLinePosition();
//			android.util.Log.v("kiyo","AA="+startLine+","+endLine);
		} else {
			mCurrentBufferStartLinePosition = 0;
			mCurrentBufferEndLinePosition = 0;
//			android.util.Log.v("kiyo","AA= clear");
		}
	}

	public static class MyBufferDatam extends LineViewData {
		private int mLinePosition = 0;

		public MyBufferDatam(CharSequence line, int color, int status, int linePosition) {
			super(line, color, status);
			mLinePosition = linePosition;
		}

		public synchronized int getLinePosition() {
			return mLinePosition;
		}
	}

	@Override
	public BreakText getBreakText() {
		return mLineManagerFromFile.getBreakText();
	}
}
