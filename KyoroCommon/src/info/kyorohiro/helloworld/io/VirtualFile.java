package info.kyorohiro.helloworld.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class VirtualFile {
	private File mBase = null;
	private RandomAccessFile mRAFile = null;
	private byte[] mWriteCash = null;
	private long mCashStartPointPerFile = 0;
	private int mCashLength = 0;
	public final static int CHUNK_SIZE = 100;

	public VirtualFile(File base, int writeCashSize) {
		mBase = base;
		int numOfchunk = writeCashSize%CHUNK_SIZE;
		mWriteCash = new byte[CHUNK_SIZE*numOfchunk];
		mBase = base;
	}

	public synchronized void seek(long point) throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		mRAFile.seek(point);
	}

	public synchronized void read(byte[] buffer) throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		mRAFile.read(buffer);
	}

	public synchronized void close() throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		mRAFile.close();
		mWriteCash = null;
	}
	
	public synchronized void addChunk(byte[] buffer, int begin, int end) throws IOException{
		if(mRAFile == null) {
			throw new IOException();
		}
		int len = end-begin;
		if(len>CHUNK_SIZE) {
			throw new IOException();
		}
		if(len+mCashLength>=mWriteCash.length) {
			syncWrite();
		}

		System.arraycopy(buffer, begin, mWriteCash, mCashLength,len);
		mCashLength +=len;
	}

	public synchronized void syncWrite() throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		mRAFile.seek(mCashStartPointPerFile);
		mRAFile.write(mWriteCash, 0, mCashLength);
		mCashStartPointPerFile += mCashLength;
		mCashLength = 0;
	}
}
