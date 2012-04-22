package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewerBuffer.MyBufferDatam;
import info.kyorohiro.helloworld.util.BigLineData;
import info.kyorohiro.helloworld.util.BigLineData.LineWithPosition;

import java.lang.ref.WeakReference;

import android.graphics.Color;

public class LookAheadCaching {
	private WeakReference<TextViewerBuffer> mBuffer = null;

	public  LookAheadCaching(TextViewerBuffer buffer) {
		mBuffer = new WeakReference<TextViewerBuffer>(buffer);
	}


	public void updateBufferedStatus() {
	}

	public static class ReadBackFileTask implements Runnable {
		private int mStartPosition = 0;
		private int mCashedStartPosition = 0;
		private BigLineData mLineManagerFromFile = null;
		private TextViewerBuffer mTextViewer = null;

		public ReadBackFileTask(
				BigLineData lineManagerFromFile, TextViewerBuffer textViewer,
				int startPosition, int cashedStartPosition) {
			mLineManagerFromFile = lineManagerFromFile;
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


		public ReadForwardFileTask(
				BigLineData lineManagerFromFile, TextViewerBuffer textViewer,
				int startPosition) {
			mLineManagerFromFile = lineManagerFromFile;
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
