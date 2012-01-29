package info.kyorohiro.helloworld.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class BigLineData {

	private final File mPath;

	public static int FILE_LIME = 1000;
	private RandomAccessFile mReader = null;
	private int mCurrentPosition = 0;
	private ArrayList<Long> mPositionPer1000Line = new ArrayList<Long>();

	public BigLineData(File path) throws FileNotFoundException {
		mPath = path;
		mReader = new RandomAccessFile(mPath, "r");
	}

	public void moveLinePer1000(int index) throws IOException {
		long filePointer = mPositionPer1000Line.get(index);
		mReader.seek(filePointer);
	}

	public boolean isEOF() {
		try {
			android.util.Log.v("kiyohiro",""+mReader.length()+ ":"+mReader.getFilePointer());
			if(mReader.length() <= mReader.getFilePointer()) {
				return true;
			}
			else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return false;
	}

	public String readLine() throws IOException {
		String tmp = mReader.readLine();
		mCurrentPosition += 1;
		if(mCurrentPosition%FILE_LIME==0) {
			mPositionPer1000Line.add(mReader.getFilePointer());
		}
		return tmp;
	}
	
	public void close() throws IOException {
		mReader.close();
	}
}
