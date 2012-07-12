package info.kyorohiro.helloworld.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.graphics.Paint;
import android.graphics.Typeface;

//@Deprecated
public class BigLineData {
	public static int FILE_LIME = 100;

	private File mPath;
	private String mCharset = "utf8";
	private VirtualMemory mReader = null;
	private SimpleTextDecoder mDecoder = null;
	
	// todo
//	private Paint mPaint = null;
//	private int mWidth =800;

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
		mReader = new VirtualMemory(mPath, 1024*4);
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
		TODOCRLFString tmp = new TODOCRLFString(new char[]{}, 0,TODOCRLFString.MODE_INCLUDE_LF);
		int lineNumber = (int) mLinePosition;
		try {
			tmp = (TODOCRLFString)mDecoder.decodeLine();
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

	@Deprecated
	private long getDataSize() {
		return mPath.length();
	}

	@Deprecated
	public long getLastLinePosition() {
		return mLastLinePosition;
	}

	@Deprecated
	private int getStackedLinePer100() {
		return mPositionPer100Line.size();
	}
}
