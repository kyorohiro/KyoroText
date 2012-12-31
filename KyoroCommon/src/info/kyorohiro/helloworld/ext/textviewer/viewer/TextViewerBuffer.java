package info.kyorohiro.helloworld.ext.textviewer.viewer;

import info.kyorohiro.helloworld.display.simple.BreakText;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.BigLineData;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.LockableCyclingList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class TextViewerBuffer extends LockableCyclingList implements LineViewBufferSpec {
	private BigLineData mLineManagerFromFile = null;
	private int mCurrentBufferStartLinePosition = 0;
	private int mCurrentBufferEndLinePosition = 0;
	private int mCurrentPosition = 0;
	private KyoroString mErrorLineMessage = new KyoroString("error..\n", SimpleGraphicUtil.parseColor("#FFFF0000"));
	private KyoroString mLoadingLineMessage = new KyoroString("loading..\n", SimpleGraphicUtil.parseColor("#33FFFF00"));
	private LookAheadCaching mCashing = null;
	private long mNumberOfStockedElement = 0;
	private boolean mIsSync = false;

	public TextViewerBuffer(int listSize, BreakText breakText, File path, String charset) throws FileNotFoundException {
		super(listSize);
		mErrorLineMessage.isNowLoading(true);
		mLoadingLineMessage.isNowLoading(true);
		mLineManagerFromFile = new BigLineData(path, charset, breakText);
		mCashing = new LookAheadCaching(this);
	}

	public BigLineData getBigLineData() {
		return mLineManagerFromFile;
	}

	public synchronized int getNumberOfStockedElement() {
		//todo
		return (int)mNumberOfStockedElement;
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
	public synchronized void head(KyoroString element) {
		int num = getNumOfAdd();
		super.head(element);
		if (element instanceof KyoroString) {
			setNumOfAdd(num);
		}
	}

	@Override
	public synchronized void add(KyoroString element) {
		int num = getNumOfAdd();
		super.add(element);
		if (element instanceof KyoroString) {
			KyoroString b = (KyoroString)element;
			long pos = b.getLinePosition();
			if(mNumberOfStockedElement<(pos+1)) {
				mNumberOfStockedElement = pos+1;
				setNumOfAdd(num+1);
			}
			else {
				setNumOfAdd(num);				
			}
		}
	}

	// so bad performance!! must to improve
	private KyoroString getSync(int i) {
		if (i < 0) {
			return mErrorLineMessage;
		}
		else if(i>=getNumberOfStockedElement()&&!isLoading()) {
			return mErrorLineMessage;
		}
		try {
			mLineManagerFromFile.moveLine(i);
			mNumberOfStockedElement = mLineManagerFromFile.getLastLinePosition();
			if(!mLineManagerFromFile.wasEOF()){
				mNumberOfStockedElement++;
			}
			return mLineManagerFromFile.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return mErrorLineMessage;
		}
	}
	private KyoroString getAsync(int i) {
		if (i < 0) {
			return mErrorLineMessage;
		}
		try {
			resetBufferedStartEndPosition(i);
			if(!lineIsLoaded(i)||super.getNumberOfStockedElement()<=lineNumberToBufferedNumber(i)) {
				return mLoadingLineMessage;
			}

			KyoroString bufferedDataForReturn = super.get(lineNumberToBufferedNumber(i));
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

	public synchronized KyoroString get(int i) {
		if(mIsSync) {
			return getSync(i);			
		} else {
			return getAsync(i);
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
			KyoroString startLineWithPosition = (KyoroString) startLine;
			KyoroString endLineWithPosition = (KyoroString) endLine;
			mCurrentBufferStartLinePosition = (int) startLineWithPosition.getLinePosition();
			mCurrentBufferEndLinePosition = (int) endLineWithPosition.getLinePosition();
//			android.util.Log.v("kiyo","AA="+startLine+","+endLine);
		} else {
			mCurrentBufferStartLinePosition = 0;
			mCurrentBufferEndLinePosition = 0;
//			android.util.Log.v("kiyo","AA= clear");
		}
	}

	@Override
	public BreakText getBreakText() {
		return mLineManagerFromFile.getBreakText();
	}

	@Override
	public void isSync(boolean isSync) {
		 mIsSync = isSync;
		 if(isSync){
			 mCashing.stopTask();
		 }
	}
	@Override
	public boolean isSync() {
		return mIsSync;
	}

	@Override
	public boolean isLoading() {
		return !mLineManagerFromFile.wasEOF();
	}
}
