package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewerBuffer.MyBufferDatam;
import info.kyorohiro.helloworld.util.BigLineData;
import info.kyorohiro.helloworld.util.BigLineData.LineWithPosition;

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

     //   if(sp<=cp&&cp>=ep) {
        	// 
        if(ep<(cp+chunkSize*3)){
        	startReadForward(ep);
        } else 
        if(sp>(cp-chunkSize*3)){
        	startReadBack(sp);
        }
      //  } else {
        	// 
        	
      //  }
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
		public Runnable create() {
			TextViewerBuffer buffer = mBuffer.get();
			if(buffer== null) {
				//todo
				return null;
			}
			return new ReadForwardFileTask(
					buffer,
					position);
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

		public ReadForwardFileTask(TextViewerBuffer textViewer, int startPosition) {
			mLineManagerFromFile = textViewer.getBigLineData();
			mTextViewer = textViewer;
			mStartPosition = startPosition;
		}

		public void run() {
			try {
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
