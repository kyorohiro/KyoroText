package info.kyorohiro.helloworld.ext.textviewer.viewer;

import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.io.BigLineData;
import info.kyorohiro.helloworld.text.KyoroString;
import java.lang.ref.WeakReference;


//
// todo now TextViewerBuffer have cashing data. next version This class have cashing data
//
public class NeiborhoodCashing {
	public static int COLOR_FONT1 = SimpleGraphicUtil.parseColor("#dd0044ff");
	public static int COLOR_FONT2 = SimpleGraphicUtil.parseColor("#ddff0044");

	private boolean mIsDispose = false;
	private WeakReference<TextViewerBuffer> mBuffer = null;
	private ReadBackBuilder mBackBuilder = new ReadBackBuilder();
	private ReadForwardBuilder mForwardBuilder = new ReadForwardBuilder();
	private Thread mTaskRunnter = null;
	public static final int LOOKAGEAD_lentgth = 2;
	public static final int CHANK_SIZE = BigLineData.FILE_LIME;
	public static final int MOVE_FORWARD = 1;
	public static final int CLEAR_AND_MOVE_FORWARD = 2;
	public static final int MOVE_BACK = 3;
	public static final int MOVE_KEEP = 4;

	public NeiborhoodCashing(TextViewerBuffer buffer) {
		mBuffer = new WeakReference<TextViewerBuffer>(buffer);
	}

	public TextViewerBuffer getTextViewerBuffer() {
		if (mBuffer != null) {
			return mBuffer.get();
		} else {
			return null;
		}
	}

	public void updateBufferedStatus() {
		TextViewerBuffer buffer = getTextViewerBuffer();
		if (buffer == null) {
			return;
		}
		int action = nextAction(buffer);
		int sp = buffer.getCurrentBufferStartLinePosition();
		int ep = buffer.getCurrentBufferEndLinePosition();
		int cp = buffer.getCurrentPosition();
		switch(action) {
		case MOVE_FORWARD:
			startReadForward(ep);
			break;
		case MOVE_BACK:
			startReadBack(sp);
			break;
		case CLEAR_AND_MOVE_FORWARD:
			startReadForwardAndClear(cp);
			break;
		}
	}

	private int nextAction(TextViewerBuffer buffer){
		int sp = buffer.getCurrentBufferStartLinePosition();
		int ep = buffer.getCurrentBufferEndLinePosition();
		int cp = buffer.getCurrentPosition();
		int mx = buffer.getMaxOfStackedElement();
		int chunkSize = mx/8;
		if (bufferIsKeep(buffer)) {
			boolean forward = false;
			boolean back = false;

			if (ep<cp+chunkSize*LOOKAGEAD_lentgth) {
				forward = true;
			}
			if (sp > 0 && sp > cp-chunkSize*LOOKAGEAD_lentgth) {
				back = true;
			}

			if (forward == true&&back == true) {
				if(Math.abs(cp-ep)<Math.abs(cp-sp)){
					forward= true;
					back= false;
				} else  {
					forward = false;
					back = true;
				}
			}

			if (forward) {
//				android.util.Log.v("kiyo","ED:::Forward");
//				startReadForward(ep);
				return MOVE_FORWARD;
			} 
			else if(back){
//				android.util.Log.v("kiyo","ED:::Back");
//				startReadBack(sp);
				return MOVE_BACK;
			}
			else {
//				android.util.Log.v("kiyo","ED:::Kepp");
				return MOVE_KEEP;				
			}
		} else {
//			startReadForwardAndClear(cp);
//			android.util.Log.v("kiyo","ED:::M FOWARD");
			return CLEAR_AND_MOVE_FORWARD;
		}
	}

	private boolean bufferIsKeep(TextViewerBuffer buffer) {
		int sp = buffer.getCurrentBufferStartLinePosition();
		int ep = buffer.getCurrentBufferEndLinePosition();
		int cp = buffer.getCurrentPosition();
		int chunkSize = CHANK_SIZE;//mx / 10;
		if(cp <0){
			return true;
		}
		if ((sp - chunkSize * LOOKAGEAD_lentgth) <= cp && cp <= (ep + chunkSize * LOOKAGEAD_lentgth)) {
//			android.util.Log.v("kiyo",":::TRUE("+sp+"- "+chunkSize+" * 3) <= "+cp+" && "+cp +"<= ("+ep +"+"+ chunkSize+" * 3)");
			return true;
		} else {
//			Debug.waitForDebugger();
//			android.util.Log.v("kiyo",":::FALSE("+sp+"- "+chunkSize+" * 3) <= "+cp+" && "+cp +"<= ("+ep +"+"+ chunkSize+" * 3)");
			return false;
		}
	}

	public synchronized void dispose() {
		mIsDispose = true;
		stopTask();
	}

	public synchronized void stopTask() {
//		android.util.Log.v("kiyo","buffer =stop()");
		if (mTaskRunnter != null && mTaskRunnter.isAlive()) {
			mTaskRunnter.interrupt();
			mTaskRunnter = null;
		}
	}

	private synchronized void startTask(Builder builder) {
		if(mIsDispose){
			return ;
		}
		if (mTaskRunnter == null || !mTaskRunnter.isAlive()) {
			mTaskRunnter = new Thread(builder.create());
			mTaskRunnter.start();
		}
	}

	private void startReadBack(int position) {
		if(position>=0) {
			mBackBuilder.position = position;
			startTask(mBackBuilder);
		}
	}

	private void startReadForwardAndClear(int position) {
		mForwardBuilder.position = position;
		mForwardBuilder.clear = true;
		startTask(mForwardBuilder);
	}

	public void startReadForward(long position) {
		TextViewerBuffer buffer = getTextViewerBuffer();
		if (buffer == null) {
			return;
		}
		mForwardBuilder.position = position;
		mForwardBuilder.clear = false;
//		if(!buffer.getBigLineData().isEOF()||(position+1)<buffer.getBigLineData().getLastLinePosition()){
//			android.util.Log.v("kiyo","="+position+"<"+buffer.getBigLineData().getLinePosition());
			startTask(mForwardBuilder);
//		} else {
//			startTask(mForwardBuilder);
//			android.util.Log.v("kiyo","="+position+"NN"+buffer.getBigLineData().getLinePosition()+","+buffer.getBigLineData().isEOF());			
//		}
	}


	interface Builder {
		Runnable create();
	}

	public class ReadBackBuilder implements Builder {
		public int position = 0;

		public Runnable create() {
			TextViewerBuffer buffer = mBuffer.get();
			if (buffer == null) {
				return null;
			}
			return new ReadBackFileTask(buffer, position);
		}
	}

	public class ReadForwardBuilder implements Builder {
		public long position = 0;
		public boolean clear;

		public Runnable create() {
			TextViewerBuffer buffer = mBuffer.get();
			if (buffer == null) {
				// todo
				return null;
			}
			return new ReadForwardFileTask(buffer, position, clear);
		}
	}

	public static KyoroString[] builder = new KyoroString[BigLineData.FILE_LIME];
	public class ReadBackFileTask implements Runnable {
		private int mStartWithoutOwn = 0;
		private BigLineData mBigLineData = null;
		private TextViewerBuffer mTextViewer = null;

		public ReadBackFileTask(TextViewerBuffer textViewer, int startWithoutOwn) {
			mBigLineData = textViewer.getBigLineData();
			mTextViewer = textViewer;
			mStartWithoutOwn = startWithoutOwn;
		}

		public void run() {
//			android.util.Log.v("kiyo","start rbt");
//			android.util.Log.v("kiyo","SD:::B"+mStartWithoutOwn);
			CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
			int c = cp.getProperty(TextViewer.KEY_TEXTVIEWER_FONT_COLOR2, COLOR_FONT2);
			try {
				mBigLineData.moveLine(mStartWithoutOwn - BigLineData.FILE_LIME);

				int j = 0;
				for (int i = 0;  mIsDispose == false&&
						!Thread.interrupted()&& i < BigLineData.FILE_LIME  && !mBigLineData.isEOF()
						&&mTaskRunnter != null&&mTaskRunnter == Thread.currentThread();
						i++) {
					CharSequence line = mBigLineData.readLine();
					KyoroString lineWP = (KyoroString) line;
					lineWP.setColor(c);
					if (mStartWithoutOwn > (int) lineWP.getLinePosition()) {
						builder[j++] = lineWP;
					}
					Thread.sleep(0);
					Thread.yield();
				}
				for (int i = j - 1; 0 <= i; i--) {
					Thread.sleep(0);
					mTextViewer.head(builder[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//			android.util.Log.v("kiyo","end rbt");
		}
	}

	public class ReadForwardFileTask implements Runnable {
		private long mStartWithoutOwn = 0;
		private BigLineData mBigLineData = null;
		private TextViewerBuffer mTextViewer = null;
		private boolean mClear = false;

		public ReadForwardFileTask(TextViewerBuffer textViewer,long startWithoutOwn) {
			if(startWithoutOwn <0){
				startWithoutOwn = 0;
			}
			mBigLineData = textViewer.getBigLineData();
			mTextViewer = textViewer;
			mStartWithoutOwn = startWithoutOwn;
		}

		public ReadForwardFileTask(TextViewerBuffer textViewer,long startPosition, boolean clear) {
			mBigLineData = textViewer.getBigLineData();
			mTextViewer = textViewer;
			mStartWithoutOwn = startPosition;
			mClear = clear;
		}

		public void run() {
			CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
			int c = cp.getProperty(TextViewer.KEY_TEXTVIEWER_FONT_COLOR2, COLOR_FONT2);
//			android.util.Log.v("kiyo","start rft");
			try {
				if (mClear) {
					mTextViewer.clear();
				}
				mBigLineData.moveLine(mStartWithoutOwn);
				int doAct = 0;
				do{
				for (int i = 0; mIsDispose == false&&
						!Thread.interrupted() && 
						i < BigLineData.FILE_LIME && !mBigLineData.isEOF()&&
						mTaskRunnter != null&&mTaskRunnter == Thread.currentThread();
						i++) {
					KyoroString lineWP = (KyoroString) mBigLineData.readLine();
					lineWP.setColor(c);
					if (lineWP.getLinePosition() > mStartWithoutOwn) {
						mTextViewer.add(lineWP);
					}
					Thread.sleep(0);
					Thread.yield();
				}
				doAct++;
				if(mBigLineData.isEOF()) {
					break;
				} else if(MOVE_FORWARD == nextAction(mTextViewer)){
					mStartWithoutOwn = mTextViewer.getCurrentBufferEndLinePosition();
					Thread.sleep(0);
					Thread.yield();
				} else {
					Thread.sleep(0);
					break;
				}
				if(doAct >5) {
					break;
				}
//				android.util.Log.v("kiyo","dorft --"+doAct);
				}
				while(true);
			} catch(InterruptedException e) {
			} catch (Exception e) {
				e.printStackTrace();
			}
//			android.util.Log.v("kiyo","end rft");
		}
	}
}
