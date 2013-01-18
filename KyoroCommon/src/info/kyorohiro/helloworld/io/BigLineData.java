package info.kyorohiro.helloworld.io;

import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

//
//
//@Deprecated
public class BigLineData {
	public static int FILE_LIME = 50;

	private VirtualFile mPath;
	private String mCharset = "utf8";
	private MarkableReader mReader = null;
	private SimpleTextDecoder mDecoder = null;

	private long mCurrentPosition = 0;
	private long mLinePosition = 0;
	private long mLastLinePosition = 0;
	private ArrayList<Long> mPositionPer100Line = new ArrayList<Long>();
	private BreakText mBreakText = null;
	private KyoroString mLastString = null;

	private FindFileFormatter msFormater = null;
	public void ffformatterOn() {
		msFormater = new FindFileFormatter();
	}

	public BigLineData(VirtualFile path) throws FileNotFoundException {
		init(path, mCharset);
	}

	public BigLineData(VirtualFile path, String charset, BreakText breakText) throws FileNotFoundException {
		mBreakText = breakText;
		init(path, charset);
	}

	public VirtualFile getVFile() {
		return mPath;
	}

	public synchronized void asisChangePath(VirtualFile path) throws FileNotFoundException {
		mReader = new MarkableFileReader(mPath=path, (int)(512*1.5));
	}

	public File getPath() {
		return mPath.getBase();
	}

	private void init(VirtualFile path, String charset) throws FileNotFoundException {
		mPath = path;
		mCharset = charset;
		mReader = new MarkableFileReader(path, (int)(512));
		mPositionPer100Line.add(0l);
		mDecoder = new SimpleTextDecoder(Charset.forName(charset), mReader, mBreakText);
	}

	public BreakText getBreakText(){
		return mBreakText;
	}

	public MarkableReader  getMarkableReader() {
		return mReader;
	}
	public synchronized void moveLine(long lineNumber) throws IOException {
		//
		if(mLinePosition == lineNumber) {
//			android.util.Log.v("kiyo","--lineNumber="+lineNumber);
			return;
		}
		//
		//
		if(lineNumber == getNextLinePosition()){
			return;
		}
		long index = lineNumber/FILE_LIME;
		long number = lineNumber%FILE_LIME;
		long t = lineNumber-mLastLinePosition;
		if(t<0||t>number) {
			moveLinePer100((int)index);
		} else {
			number = Math.abs(mLastLinePosition-lineNumber);
		}
		for(int i=0;i<number;i++){
			readLine();
		}
	}

	public boolean wasEOF() {
		try {
//			android.util.Log.v("kiyo","wasEOF="+mReader.length()+","+mLastFilePointer);
			if (mReader.length() > mLastFilePointer) {
				return false;
			} else {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	public boolean isEOF() {
		try {
			if (mReader.length() <= mReader.getFilePointer()) {
//			if (mReader.length() <= mLastFilePointer) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private long mLastFilePointer = 0;
	public synchronized KyoroString readLine() throws IOException {
		KyoroString tmp = new KyoroString(new char[]{}, 0);
		int lineNumber = (int) mLinePosition;
		long begin = 0;
		long end = 0;
		try {
			begin = mReader.getFilePointer();
			if(msFormater == null) {
				tmp = (KyoroString)mDecoder.decodeLine();
			} else {
				tmp = msFormater.read(mDecoder);
			}
			end = mCurrentPosition = mReader.getFilePointer();

			mLinePosition += 1;
			if (mLastLinePosition < mLinePosition) {
				mLastLinePosition = mLinePosition;
			}
			if (mLastFilePointer < end) {
				mLastFilePointer = end;
				mLastString = tmp;
			}			
			updateIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tmp.setLinePosition(lineNumber);
		tmp.setBeginPointer(begin);
		tmp.setEndPointer(end);
		return tmp;
	}

	public synchronized void close() throws IOException {
		mReader.close();
	}

	private void updateIndex() {
		if (mLinePosition % FILE_LIME == 0) {
			int index = mPositionPer100Line.size() - 1;
			long stackedPosition = 0;
			if (index > 0) {
				stackedPosition = mPositionPer100Line
						.get(mPositionPer100Line.size() - 1);
			}
			if (stackedPosition < mCurrentPosition) {
				mPositionPer100Line.add(mCurrentPosition);
			}
		}		
	}



	private boolean moveLinePer100(int index) throws IOException {
		if(index <0) {
			return true;
		}
		if (index < mPositionPer100Line.size()) {
			long filePointer = mPositionPer100Line.get(index);
			// mReader.seek(0);
			mReader.seek(filePointer);
			mLinePosition = index * BigLineData.FILE_LIME;
			return true;
		} else {
			return false;
		}
	}


	public long getNextLinePosition() {
		return mLinePosition;
	}

	public long getLastLinePosition() {
		return mLastLinePosition;
	}

	public KyoroString getLastString() {
		return mLastString;
	}
}
