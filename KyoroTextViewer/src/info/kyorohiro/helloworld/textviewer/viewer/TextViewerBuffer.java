package info.kyorohiro.helloworld.textviewer.viewer;

import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.util.BigLineData;
import info.kyorohiro.helloworld.util.BigLineData.LineWithPosition;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.CyclingListInter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mozilla.intl.chardet.nsCP1252Verifier;

import android.graphics.Color;
import android.os.Environment;

public class TextViewerBuffer extends CyclingList<FlowingLineDatam> {

	private File mTestDataPath = null;
	private BigLineData mLineManager = null;
	private int mCashedStart = 0;
	private int mCashedEnd = 0;
	private String mCharset = "utf8";


	public TextViewerBuffer(int listSize, int textSize, int screenWidth,
			File path, String charset) {
		super(listSize);
		mCharset = charset;
		mTestDataPath = path;
		try {
			mLineManager = new BigLineData(mTestDataPath, charset, textSize, screenWidth);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int getNumberOfStockedElement() {
		// ファイルから読み込み済みのライン数を返す。
		return (int) mLineManager.getLastLinePosition();
	}


	public FlowingLineDatam get(int i) {
		// 読み込むホジションを調べる。
		if(i<0){
			return new FlowingLineDatam("0:"+i+","+"i<0", Color.GREEN,
					FlowingLineDatam.INCLUDE_END_OF_LINE);
		}
		mCashedStart = 0; 
		mCashedEnd = 0;
		if(0<super.getNumberOfStockedElement()){
			CharSequence startLine = super.get(0);
			CharSequence endLine = super.get(super.getNumberOfStockedElement()-1);
			MyBufferDatam startLineWithPosition = (MyBufferDatam)startLine;
			MyBufferDatam endLineWithPosition = (MyBufferDatam)endLine;
			mCashedStart = (int)startLineWithPosition.getLinePosition();
			mCashedEnd = (int)endLineWithPosition.getLinePosition();
		}
		int d = i - mCashedStart;

		try {

			// todo マイナスへの移動は、後で実装する。
			if (d < 0) {
				return new FlowingLineDatam(""/*+d+":"*/+i+","+"loading", Color.GREEN,
						FlowingLineDatam.INCLUDE_END_OF_LINE);
			}

			if (super.getNumberOfStockedElement() >= d) {
//				return super.get(i);
				FlowingLineDatam t= super.get(d);
				if(t!=null){
					return t;//new FlowingLineDatam(""+d+":"+i+","+t.toString(), Color.RED, t.getStatus());
				} else {
					return new FlowingLineDatam(""/*+d+":"*/+i+","+"null..", Color.GREEN,
							FlowingLineDatam.INCLUDE_END_OF_LINE);
				}
			} else {
				// キャッシュがないらなば、Cashの取得を開始する。
				// とりあえず "loading..." と文字列を返す。
				return new FlowingLineDatam(""+d+":"+i+","+"loading..", Color.GREEN,
						FlowingLineDatam.INCLUDE_END_OF_LINE);
			}
		} catch(Throwable t) {
			t.printStackTrace();
			return new FlowingLineDatam(""+d+":"+i+","+"error..", Color.GREEN,
					FlowingLineDatam.INCLUDE_END_OF_LINE);
		} 
		finally {
			if (mCashedEnd < (i + BigLineData.FILE_LIME*3)) {
				if(0==super.getNumberOfStockedElement()){
					startReadForward(-1);					
				} else {
					startReadForward(mCashedEnd);
				}
			} 
			else if (mCashedStart > (i - BigLineData.FILE_LIME*3)) {
				int tmp = mCashedStart;
				if(mCashedStart == 0) {
				
				}
				else if(tmp <0&&mCashedStart<0){
					startReadBack(0);				
				} else if(tmp > 0) {
					startReadBack(tmp);
				}
			}
		}
	}

	Thread mTaskRunnter = null;

	public void startReadForward(int position) {
		if (mTaskRunnter == null || !mTaskRunnter.isAlive()) {
			mTaskRunnter = new Thread(new ReadForwardFileTask(position));
			mTaskRunnter.start();
		}
	}

	public void startReadBack(int position) {
		if (mTaskRunnter == null || !mTaskRunnter.isAlive()) {
			mTaskRunnter = new Thread(new ReadBackFileTask(position,mCashedStart));
			mTaskRunnter.start();
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

	
	class ReadForwardFileTask implements Runnable {
		private int mStartPosition = 0;

		public ReadForwardFileTask(int startPosition) {
			mStartPosition = startPosition;
		}

		public void run() {
			try {
				int index = mStartPosition/BigLineData.FILE_LIME+1;
				mLineManager.moveLinePer100(index);
				for (int i = 0;
				i<BigLineData.FILE_LIME&&!mLineManager.isEOF();
				i++) {

					CharSequence line = mLineManager.readLine();
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
						TextViewerBuffer.this.add(t);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class ReadBackFileTask implements Runnable {
		private int mStartPosition = 0;
		private int mCashedStartPosition = 0;

		public ReadBackFileTask(int startPosition, int cashedStartPosition) {
		//	android.util.Log.v("aaa","=---- CREATE"+startPosition+","+cashedStartPosition);
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
				// todo あまり分を計算すること
				MyBufferDatam[] builder = new MyBufferDatam[BigLineData.FILE_LIME];
				mLineManager.moveLinePer100(index);
				int j=0;
//				android.util.Log.v("aaa","=---- START");
				for (int i = 0;i<BigLineData.FILE_LIME&&!mLineManager.isEOF();i++) {
					CharSequence line = mLineManager.readLine();
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
					TextViewerBuffer.this.head(builder[i]);					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			if(mLineManager.wasEOF()) {
				MyBufferDatam datam = (MyBufferDatam)element;
				if(datam.getLinePosition() <= mLineManager.getLastLinePosition() && !mLineManager.wasEOF()) {
					setNumOfAdd(num);
				}
			}
		}
	}
}
