package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.io.BigLineData;
import info.kyorohiro.helloworld.io.BigLineData.LineWithPosition;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewerBuffer.MyBufferDatam;

import java.lang.ref.WeakReference;

import android.graphics.Color;

public class LookAheadCaching {
	private WeakReference<TextViewerBuffer> mBuffer = null;
	private ReadBackBuilder mBackBuilder = new ReadBackBuilder(); 
	private ReadForwardBuilder mForwardBuilder = new ReadForwardBuilder(); 
	private Thread mTaskRunnter = null;


	public LookAheadCaching(TextViewerBuffer buffer) {
		mBuffer = new WeakReference<TextViewerBuffer>(buffer);
	}


	public TextViewerBuffer getTextViewerBuffer() {
		if(mBuffer!=null){
			return mBuffer.get();
		}
		else {
			return null;
		}
	}

	public void updateBufferedStatus() {
		TextViewerBuffer buffer = getTextViewerBuffer();
		if(buffer == null){
			return;
		}
		int sp = buffer.getCurrentBufferStartLinePosition();
		int ep = buffer.getCurrentBufferEndLinePosition();
        int cp = buffer.getCurrentPosition();
        int mx = buffer.getMaxOfStackedElement();
        int chunkSize = mx/10;

        if((sp-chunkSize*2)<=cp&&cp<=(ep+chunkSize*2)) {
        	// 
        	boolean forward = false;
        	boolean back = false;
        	if(ep<(cp+chunkSize*3)){
        		forward = true;
        	} 
        	if(sp>0&&sp>(cp-chunkSize*3)){
        		back = true;
        	}
        	if(forward==true&&back==true){
        		if((ep-cp)<cp-sp){
            		startReadForward(ep);	
        		}else {
            		startReadBack(sp);        			
        		}
        	}
        	else if(forward==true){
        		startReadForward(ep);
        	}
        	else {
        		startReadBack(sp);
        	}
        } else {
        	//buffer.clear();
//        	int pos = BigLineData.FILE_LIME;
        	startReadForwardAndClear(cp);
       }
	}
	public void startTask(Builder builder) {
		if (mTaskRunnter == null || !mTaskRunnter.isAlive()) {
			mTaskRunnter = new Thread(builder.create());
			mTaskRunnter.start();
		}
	}

	public void startReadForwardAndClear(int position) {
		mForwardBuilder.position = position;
		mForwardBuilder.clear = true;
		startTask(mForwardBuilder);
	}
	public void startReadForward(int position) {
		mForwardBuilder.position = position;
		mForwardBuilder.clear = false;
		startTask(mForwardBuilder);
	}

	public void startReadBack(int position) {
		TextViewerBuffer buffer = mBuffer.get();
		mBackBuilder.position = position;
		mBackBuilder.cashedStartPosition = buffer.getCurrentBufferStartLinePosition();
		startTask(mBackBuilder);
	}

	interface Builder {
		Runnable create();
	}

	public class ReadBackBuilder implements Builder{
		public int position = 0;
		public int cashedStartPosition = 0;
		public Runnable create() {
			TextViewerBuffer buffer = mBuffer.get();
			if(buffer== null) {
				//todo
				return null;
			}

			return new ReadBackFileTask(buffer, position, cashedStartPosition);
		}
	}

	public class ReadForwardBuilder implements Builder {
		public int position = 0;
		public boolean clear;
		public Runnable create() {
			TextViewerBuffer buffer = mBuffer.get();
			if(buffer== null) {
				//todo
				return null;
			}
			return new ReadForwardFileTask(buffer,position,clear);
		}
	}

	public static class ReadBackFileTask implements Runnable {
		private int mStartPosition = 0;
		private int mCashedStartPosition = 0;
		private BigLineData mLineManagerFromFile = null;
		private TextViewerBuffer mTextViewer = null;

		public ReadBackFileTask(
				TextViewerBuffer textViewer,
				int startPosition, int cashedStartPosition) {
			mLineManagerFromFile = textViewer.getBigLineData();
			mTextViewer = textViewer;
			startPosition--;

			if(startPosition > 1) {
				mStartPosition = cashedStartPosition-1;
			} else {
				mStartPosition = startPosition;
			}
			mCashedStartPosition = cashedStartPosition;
		}

		public void run() {
			try {
				int index = mStartPosition/BigLineData.FILE_LIME;
				// todo ‚ ‚Ü‚è•ª‚ðŒvŽZ‚·‚é‚±‚Æ
				MyBufferDatam[] builder = new MyBufferDatam[BigLineData.FILE_LIME];
				mLineManagerFromFile.moveLinePer100(index);
				int j=0;
				//				android.util.Log.v("aaa","=---- START");
				for (int i = 0;i<BigLineData.FILE_LIME&&!mLineManagerFromFile.isEOF();i++) {
					CharSequence line = mLineManagerFromFile.readLine();
					LineWithPosition lineWP = (LineWithPosition)line;
					int crlf = FlowingLineDatam.INCLUDE_END_OF_LINE;
					if(!lineWP.includeLF()) {
						crlf = FlowingLineDatam.EXCLUDE_END_OF_LINE;
					}
					MyBufferDatam t = new MyBufferDatam(
							//							"=----"+lineWP.getLinePosition()+"-----"+
							line.toString(),
							Color.WHITE,
							crlf,
							(int)lineWP.getLinePosition());
					if(mCashedStartPosition>(int)lineWP.getLinePosition()){
						builder[j++]=t;
						//						android.util.Log.v("aaa","=----"+lineWP.getLinePosition());
					}
					Thread.yield();
				}
				//				android.util.Log.v("aaa","=---- END");
				for(int i=j-1;0<=i;i--) {
					mTextViewer.head(builder[i]);					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class ReadForwardFileTask implements Runnable {
		private int mStartPosition = 0;
		private BigLineData mLineManagerFromFile = null;
		private TextViewerBuffer mTextViewer = null;
		private boolean mClear = false;
		public ReadForwardFileTask(TextViewerBuffer textViewer, int startPosition) {
			mLineManagerFromFile = textViewer.getBigLineData();
			mTextViewer = textViewer;
			mStartPosition = startPosition;
		}
		public ReadForwardFileTask(TextViewerBuffer textViewer, int startPosition,boolean clear) {
			mLineManagerFromFile = textViewer.getBigLineData();
			mTextViewer = textViewer;
			mStartPosition = startPosition;
			mClear = clear;
		}
		public void run() {
			try {
				if(mClear){
					mTextViewer.clear();
				}
				int index = mStartPosition/BigLineData.FILE_LIME+1;
				mLineManagerFromFile.moveLinePer100(index);
				for (int i = 0;
				i<BigLineData.FILE_LIME&&!mLineManagerFromFile.isEOF();
				i++) {

					CharSequence line = mLineManagerFromFile.readLine();
					LineWithPosition lineWP = (LineWithPosition)line;
					int crlf = FlowingLineDatam.INCLUDE_END_OF_LINE;
					if(!lineWP.includeLF()) {
						crlf = FlowingLineDatam.EXCLUDE_END_OF_LINE;
					}							
					MyBufferDatam t = new MyBufferDatam(
							//							"+----"+lineWP.getLinePosition()+"-----"+
							line.toString(),
							Color.WHITE,
							crlf,
							(int)((LineWithPosition)line).getLinePosition());
					if(lineWP.getLinePosition() >mStartPosition) {
						mTextViewer.add(t);
					}
					Thread.yield();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
