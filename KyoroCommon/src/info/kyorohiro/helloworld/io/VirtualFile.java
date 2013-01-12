package info.kyorohiro.helloworld.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
public class VirtualFile implements KyoroFile {
//	private File mBase = null;
	private RandomAccessFile mRAFile = null;
	private byte[] mWriteCash = null;
	private long mCashStartPointPerFile = 0;
	private int mCashLength = 0;
	public final static int CHUNK_SIZE = 100;

	public VirtualFile(File base, int writeCashSize) throws FileNotFoundException {
		int numOfchunk = writeCashSize%CHUNK_SIZE;
		mWriteCash = new byte[CHUNK_SIZE*numOfchunk];
		mRAFile = new RandomAccessFile(base, "rw");
	}
	public VirtualFile(RandomAccessFile base, int writeCashSize) {
		int numOfchunk = writeCashSize%CHUNK_SIZE;
		mWriteCash = new byte[CHUNK_SIZE*numOfchunk];
		mRAFile = base;
	}

	@Override
	public synchronized void seek(long point) throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		mRAFile.seek(point);
	}

	@Override
	public synchronized long length() throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		long raLen = mRAFile.length();
		long caLen = mCashStartPointPerFile+mCashLength;
		if(raLen > caLen) {
			return raLen;
		} else {
			return caLen;
		}
	}
	
	@Override
	public synchronized int read(byte[] buffer) throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		long current = mRAFile.getFilePointer();
		if(mCashStartPointPerFile <= current && current < (mCashStartPointPerFile+mCashLength)) {
			int srcPos = (int)(current-mCashStartPointPerFile);
			System.arraycopy(mWriteCash, srcPos, buffer, 0, buffer.length);
			return buffer.length;
		} else {
			long raLen = mRAFile.length();
			long caLen = mCashStartPointPerFile+mCashLength;
			int ret = mRAFile.read(buffer);
			if(ret!=-1&&raLen<caLen) {
				int srcPos = (int)(current-mCashStartPointPerFile+ret);
				int len = buffer.length-ret;
				if(len >mCashLength-srcPos) {
					len = mCashLength-srcPos;
				}
				System.arraycopy(mWriteCash, srcPos, buffer, ret, len);
				return len;
			} else {
				return ret;
			}
		}
	}

	@Override
	public synchronized void close() throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		mRAFile.close();
		mRAFile = null;
		mWriteCash = null;
	}

	@Override
	public synchronized void addChunk(byte[] buffer, int begin, int end) throws IOException{
		if(mRAFile == null) {
			throw new IOException();
		}
		if(mWriteCash.length == 0) {
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

	@Override
	public synchronized void syncWrite() throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		if(mWriteCash.length == 0) {
			throw new IOException();
		}
		mRAFile.seek(mCashStartPointPerFile);
		mRAFile.write(mWriteCash, 0, mCashLength);
		mCashStartPointPerFile += mCashLength;
		mCashLength = 0;
	}
}
