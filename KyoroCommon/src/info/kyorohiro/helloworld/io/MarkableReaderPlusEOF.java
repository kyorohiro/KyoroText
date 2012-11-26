package info.kyorohiro.helloworld.io;

import java.io.IOException;

public class MarkableReaderPlusEOF implements MarkableReader {

	private MarkableReader mTarget = null;
	private long mPosition = 0;
	public static final char EOF = '\0';

	public MarkableReaderPlusEOF(MarkableReader target) throws IOException {
		mTarget = target;
		mPosition = mTarget.getFilePointer();
	}
	@Override
	public void pushMark() {
		mTarget.popMark();
	}

	@Override
	public void backToMark() {
		mTarget.backToMark();
	}

	@Override
	public long popMark() {
		return mTarget.popMark();
	}

	@Override
	public int read() throws IOException {
		try {
			if(mPosition < mTarget.length()){
				return mTarget.read();
			} else if(mPosition == mTarget.length()) {
				return EOF;
			} else {
				return -1;
			}
		} finally {
			mPosition++;
		}
	}

	@Override
	public void seek(long pos) throws IOException {
		mPosition = pos;
		if(mPosition < mTarget.length()){
			mTarget.seek(pos);
		}
	}

	@Override
	public long getFilePointer() throws IOException {
		return mPosition;
	}

	@Override
	public long length() throws IOException {
		return mTarget.length()+1;
	}

	@Override
	public void close() throws IOException {
		mTarget.close();
	}

}
