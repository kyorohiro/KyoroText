package info.kyorohiro.helloworld.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.ArrayList;
import java.util.SortedMap;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

public class BigLineData {
	public static int FILE_LIME = 1000;

	private File mPath;
	private String mCharset = "utf8";
	private RandomAccessFile mReader = null;

	private long mCurrentPosition = 0;
	private long mLastPosition = 0;
	private long mLinePosition = 0;
	private ArrayList<Long> mPositionPer1000Line = new ArrayList<Long>();

	public BigLineData(File path) throws FileNotFoundException {
		mPath = path;
		mReader = new RandomAccessFile(mPath, "r");
		detectCharset();
	}

	public BigLineData(File path, String charset) throws FileNotFoundException {
		mPath = path;
		mCharset = charset;
		mReader = new RandomAccessFile(mPath, "r");
	}

	public boolean stackedLinePosition() {
		if(mLastPosition >= getDataSize()){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean moveLinePer1000(int index) throws IOException {
		if(index < mPositionPer1000Line.size()) {
			long filePointer = mPositionPer1000Line.get(index);
			mReader.seek(filePointer);
			mReader.seek(0);
			return true;
		}
		else {
			return false;
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

	public long getDataSize() {
		return mPath.length();
	}

	public CharSequence readLine() throws IOException {
		String tmp = "";
		try {
			tmp = decode(Charset.forName(mCharset));
			mCurrentPosition = mReader.getFilePointer();
			mLinePosition += 1;
			if (mLinePosition%1000 == 0) {
				int index = mPositionPer1000Line.size()-1;
				long stackedPosition = 0;
				if(index>0){
					stackedPosition = mPositionPer1000Line.get(mPositionPer1000Line.size()-1);
				}
				if(stackedPosition < mCurrentPosition) {
					mPositionPer1000Line.add(mCurrentPosition);
				}
			}
			if(mLastPosition < mCurrentPosition){
				mLastPosition = mCurrentPosition;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return new LineWithPosition(tmp, mCurrentPosition);
	}

	public void close() throws IOException {
		mReader.close();
	}

	public String decode(Charset cs) throws IOException {
		StringBuilder b = new StringBuilder();
		CharsetDecoder decoder = cs.newDecoder();
		boolean end = false;
		ByteBuffer bb = ByteBuffer.allocateDirect(32); // bbは書き込める状態
		CharBuffer cb = CharBuffer.allocate(16); // cbは書き込める状態

		do {
			int d = mReader.read();
			if (d >= 0) {
				bb.put((byte) d); // bbへの書き込み
			} else {
				// todo
				break;
			}
			bb.flip();
			CoderResult cr = decoder.decode(bb, cb, end);

			cb.flip();
			char c = ' ';
			boolean added = false;
			while (cb.hasRemaining()) {
				added = true;
				bb.clear();
				c = cb.get();
				b.append(c);
			}
			if (c == '\n') {
				break;
			}
			if (!added) {
				bb.compact();
			}
			cb.clear(); // cbを書き込み状態に変更
		} while (!end);
		return b.toString();
	}

	public void detectCharset() {
		byte[] buffer = new byte[1000];
		int len = 0;
		try {
			long fp = mReader.getFilePointer();
			len = mReader.read(buffer);
			mReader.seek(fp);
			boolean isAscii = false;
			nsDetector detector = new nsDetector(nsPSMDetector.ALL);
			detector.Init(new nsICharsetDetectionObserver() {
				@Override
				public void Notify(String charset) {

				}
			});

			isAscii = detector.isAscii(buffer, len);
			if (!isAscii) {
				detector.DoIt(buffer, len, false);
			}
			detector.DataEnd();
			String[] prob = detector.getProbableCharsets();

			if (prob != null) {
				for (String s : prob) {
					android.util.Log.v("kiyo", "prob=" + s);
				}
				OUT: {
					for (String s : prob) {
						SortedMap<String, Charset> m = Charset
								.availableCharsets();
						for (Charset c : m.values()) {
							// エイリアス
							if (c.isSupported(s)) {
								mCharset = s;
								android.util.Log.v("kiyo", "charset="
										+ mCharset);
								android.util.Log.v("kiyo",
										"===============================");
								return;
							}
						}
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static class LineWithPosition implements CharSequence{
		private CharSequence mLine = "";
		private long mPosition = 0;

		public LineWithPosition(CharSequence line, long position) {
			// todo add endposition startposition                  
			mLine = line;
			mPosition = position;
		}

		public char charAt(int index) {
			return mLine.charAt(index);
		}

		public int length() {
			return mLine.length();
		}

		public CharSequence subSequence(int start, int end) {
			return new LineWithPosition(
					mLine.subSequence(start, end), mPosition);
		}

		@Override
		public String toString() {
			if(mLine == null){
				return "";
			}
			return mLine.toString();
		}

		public long getColor() {
			return mPosition;
		}

	}
}
