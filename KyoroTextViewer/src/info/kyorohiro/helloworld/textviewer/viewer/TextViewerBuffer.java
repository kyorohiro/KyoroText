package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.util.BigLineData;
import info.kyorohiro.helloworld.util.BigLineData.LineWithPosition;
import info.kyorohiro.helloworld.util.CyclingList;

import java.io.File;
import java.io.FileNotFoundException;
import android.graphics.Color;

public class TextViewerBuffer 

// todo following code:
//   mod from extends to implements 
// example)
// >>implements CyclingList<Flow..?>
// >> private  CyclingList<FlowingLineDatam> mCash = ...;
//
extends CyclingList<FlowingLineDatam> {

	private BigLineData mLineManagerFromFile = null;
	private int mCurrentBufferStartLinePosition = 0;
	private int mCurrentBufferEndLinePosition = 0;
	private FlowingLineDatam mReturnUnexpectedValue = new FlowingLineDatam("..", Color.RED, FlowingLineDatam.INCLUDE_END_OF_LINE);
	private FlowingLineDatam mReturnLoadingValue = new FlowingLineDatam("loading..", Color.GREEN, FlowingLineDatam.INCLUDE_END_OF_LINE);
	private Thread mTaskRunnter = null;
	private ReadBackBuilder mBackBuilder = new ReadBackBuilder(); 
	private ReadForwardBuilder mForwardBuilder = new ReadForwardBuilder(); 
	private LookAheadCaching mCashing = null;
	
	public TextViewerBuffer(int listSize, int textSize, int screenWidth, File path, String charset) {
		super(listSize);
		try {
			mLineManagerFromFile = new BigLineData(path, charset, textSize, screenWidth);
			mCashing = new LookAheadCaching(this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getNumberOfStockedElement() {
		return (int) mLineManagerFromFile.getLastLinePosition();
	}

	public int getCurrentBufferStartLinePosition() {
		return mCurrentBufferStartLinePosition;
	}
	
	public int getCurrentBufferEndLinePosition() {
		return mCurrentBufferEndLinePosition;
	}

	@Override
	public synchronized void head(FlowingLineDatam element) {
		int num = getNumOfAdd();
		super.head(element);
		if(element instanceof MyBufferDatam) {
			MyBufferDatam datam = (MyBufferDatam)element;
			setNumOfAdd(num);
		}
	}

	@Override
	public synchronized void add(FlowingLineDatam element) {
		int num = getNumOfAdd();
		super.add(element);
		if(element instanceof MyBufferDatam) {
			if(mLineManagerFromFile.wasEOF()) {
				MyBufferDatam datam = (MyBufferDatam)element;
				if(datam.getLinePosition() <= mLineManagerFromFile.getLastLinePosition() && !mLineManagerFromFile.wasEOF()) {
					setNumOfAdd(num);
				}
			}
		}
	}

	// todo : this method is so big
	public FlowingLineDatam get(int i) {
		// 読み込むホジションを調べる。
		if(i<0){
			return mReturnUnexpectedValue;
		}
		mCurrentBufferStartLinePosition = 0; 
		mCurrentBufferEndLinePosition = 0;
		
		try {
			int bufferSize = super.getNumberOfStockedElement();
			
			doSetCurrentBufferedPosition();
			// must to call following code after mCurrentBufferStartLinePosition.
			int bufferedPoaition = i - mCurrentBufferStartLinePosition;

			if(bufferedPoaition < 0 || bufferSize <bufferedPoaition){
				return mReturnLoadingValue;
			} else {
				FlowingLineDatam bufferedDataForReturn= super.get(bufferedPoaition);
				if(bufferedDataForReturn == null){
					return mReturnUnexpectedValue;
				}
				return bufferedDataForReturn;
			}
		} catch(Throwable t) {
			t.printStackTrace();
			return mReturnUnexpectedValue;				
		} finally {
			if(mCashing != null) {
				mCashing.updateBufferedStatus();
			}

			if (mCurrentBufferEndLinePosition < (i + BigLineData.FILE_LIME*3)) {
				if(0==super.getNumberOfStockedElement()){
					startReadForward(-1);					
				} else {
					startReadForward(mCurrentBufferEndLinePosition);
				}
			} 
			else if (mCurrentBufferStartLinePosition > (i - BigLineData.FILE_LIME*3)) {
				int tmp = mCurrentBufferStartLinePosition;
				if(mCurrentBufferStartLinePosition == 0) {

				} else if(tmp <0&&mCurrentBufferStartLinePosition<0){
					startReadBack(0);				
				} else if(tmp > 0) {
					startReadBack(tmp);
				}
			}
		}
	}

	private void doSetCurrentBufferedPosition() {
		int bufferSize = super.getNumberOfStockedElement();
		if(0<bufferSize){
			CharSequence startLine = super.get(0);
			CharSequence endLine = super.get(bufferSize-1);
			MyBufferDatam startLineWithPosition = (MyBufferDatam)startLine;
			MyBufferDatam endLineWithPosition = (MyBufferDatam)endLine;
			mCurrentBufferStartLinePosition = (int)startLineWithPosition.getLinePosition();
			mCurrentBufferEndLinePosition = (int)endLineWithPosition.getLinePosition();
		} else {
			mCurrentBufferStartLinePosition = 0;
			mCurrentBufferEndLinePosition = 0;
		}
	}

	public void startTask(Builder builder) {
		if (mTaskRunnter == null || !mTaskRunnter.isAlive()) {
			mTaskRunnter = new Thread(builder.create());
			mTaskRunnter.start();
		}
	}

	public void startReadForward(int position) {
		mForwardBuilder.position = position;
		startTask(mForwardBuilder);
	}

	public void startReadBack(int position) {
		mBackBuilder.position = position;
		mBackBuilder.cashedStartPosition = mCurrentBufferStartLinePosition;
		startTask(mBackBuilder);
	}

	interface Builder {
		Runnable create();
	}

	public class ReadBackBuilder implements Builder{
		public int position = 0;
		public int cashedStartPosition = 0;
		public Runnable create() {
			return new LookAheadCaching.ReadBackFileTask(
					TextViewerBuffer.this.mLineManagerFromFile, TextViewerBuffer.this,
					position, cashedStartPosition);
		}
	}
	public class ReadForwardBuilder implements Builder {
		public int position = 0;
		public Runnable create() {
			return new LookAheadCaching.ReadForwardFileTask(
					TextViewerBuffer.this.mLineManagerFromFile, TextViewerBuffer.this,
					position);
		}
	}

	public static class MyBufferDatam extends FlowingLineDatam {
		private int mLinePosition = 0;
		public MyBufferDatam(CharSequence line, int color, int status, int linePosition) {
			super(line, color, status);
			mLinePosition = linePosition;
		}

		public int getLinePosition() {
			return mLinePosition;
		}
	}


}
