package info.kyorohiro.helloworld.io;

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
	public static int FILE_LIME = 100;

	private File mPath;
	private String mCharset = "utf8";
	private MarkableFileReader mReader = null;
	private SimpleTextDecoder mDecoder = null;
	

	private long mCurrentPosition = 0;
	private long mLinePosition = 0;
	private long mLastLinePosition = 0;
	private ArrayList<Long> mPositionPer100Line = new ArrayList<Long>();
	private BreakText mBreakText = null;

	public BigLineData(File path) throws FileNotFoundException {
		init(path, mCharset);
	}

	public BigLineData(File path, String charset, BreakText breakText) throws FileNotFoundException {
		mBreakText = breakText;
		init(path, charset);
	}

	private void init(File path, String charset) throws FileNotFoundException {
		mPath = path;
		mCharset = charset;
		mReader = new MarkableFileReader(mPath, 1024*2);
		mPositionPer100Line.add(0l);
		mDecoder = new SimpleTextDecoder(Charset.forName(charset), mReader, mBreakText);
	}

	public BreakText getBreakText(){
		return mBreakText;
	}

	public void moveLine(long lineNumber) throws IOException {
		long index = lineNumber/FILE_LIME;
		long number = lineNumber%FILE_LIME;
		moveLinePer100((int)index);
		for(int i=0;i<number;i++){
			readLine();
		}
	}

	public boolean isEOF() {
		try {
			if (mReader.length() <= mReader.getFilePointer()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}


	public CharSequence readLine() throws IOException {
		KyoroString tmp = new KyoroString(new char[]{}, 0);
		int lineNumber = (int) mLinePosition;
		try {
			tmp = (KyoroString)mDecoder.decodeLine();
			mCurrentPosition = mReader.getFilePointer();
			mLinePosition += 1;
			if (mLastLinePosition < mLinePosition) {
				mLastLinePosition = mLinePosition;
			}
			updateIndex();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tmp.setLinePosition(lineNumber);
		return tmp;
	}

	public void close() throws IOException {
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


//	@Deprecated
	public long getLastLinePosition() {
		return mLastLinePosition;
	}

}
