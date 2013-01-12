package info.kyorohiro.helloworld.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
public class VirtualFile implements KyoroFile {
//	private File mBase = null;
	private RandomAccessFile mRAFile = null;
	private byte[] mWriteCash = null;
	private long mCashStartPointPerFile = 0;
	private int mCashLength = 0;
	public final static int CHUNK_SIZE = 100;
	private long mSeek = 0;

	public VirtualFile(File base, int writeCashSize) throws FileNotFoundException {
		int numOfchunk = writeCashSize/CHUNK_SIZE;
		mWriteCash = new byte[CHUNK_SIZE*numOfchunk];
		mRAFile = new RandomAccessFile(base, "rw");
	}
	public VirtualFile(RandomAccessFile base, int writeCashSize) {
		int numOfchunk = writeCashSize/CHUNK_SIZE;
		mWriteCash = new byte[CHUNK_SIZE*numOfchunk];
		mRAFile = base;
	}

	@Override
	public synchronized void seek(long point) throws IOException {
		if(mRAFile == null) {
			throw new IOException();
		}
		mSeek = point;
		if(mSeek <0) {
			mSeek = 0;
		}
		if(mSeek<mRAFile.length()&&point!=mRAFile.getFilePointer()) {
			mRAFile.seek(point);
		}
	}

	public long getFilePointer() {
		return mSeek;
	}

	@Override
	public synchronized long length() throws IOException {
		seek(mSeek);
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
		seek(mSeek);
		if(mRAFile == null) {
			throw new IOException();
		}
		long current = getFilePointer();
		long raLen = mRAFile.length();
		long caLen = mCashStartPointPerFile+mCashLength;

		android.util.Log.v("kiyo","cu="+current+"ra="+raLen+",ca="+caLen);
		if(current >= raLen&& current >= caLen) {
			return -1;
		}
		int ret = 0;
		if(mCashStartPointPerFile <= current && current< (mCashStartPointPerFile+mCashLength)) {
			int srcPos = (int)(current-mCashStartPointPerFile);
			System.arraycopy(mWriteCash, srcPos, buffer, 0, mCashLength-srcPos);
			if((mCashLength-srcPos)<buffer.length&&raLen>caLen) {
				mRAFile.seek(current + mCashLength-srcPos);
				ret = mRAFile.read(buffer, mCashLength-srcPos, buffer.length-(mCashLength-srcPos));
			}
			if(ret == -1) {
				ret = 0;
			}
			ret = mCashLength-srcPos + ret;
		} else {
			ret = mRAFile.read(buffer);
			if(ret!=-1&&raLen<caLen) {
				int srcPos = (int)(current-mCashStartPointPerFile+ret);
				int len = buffer.length-ret;
				if(len >mCashLength-srcPos) {
					len = mCashLength-srcPos;
				}
				System.arraycopy(mWriteCash, srcPos, buffer, ret, len);
				ret += len;
			}
			
		}
		
		seek(current+ret);
		return ret;
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

	public long getStartChunk() {
		return mCashStartPointPerFile;
	}
	public long getChunkSize() {
		return mCashLength;
	}

	public long getChunkCash(int i) {
		if(i<mCashLength) {
			return mWriteCash[i];
		} else {
			return 0;
		}
	}

	@Override
	public synchronized void addChunk(byte[] buffer, int begin, int end) throws IOException{
		int s = 0;
		int e = 0;
		for(int i=begin;i<end;i+=CHUNK_SIZE) {
			s = i;
			e = s+CHUNK_SIZE; 
			if(e>end) {
				e = end;
			}
			_addChunk(buffer, s, e);
		}
	}
	
	public synchronized void _addChunk(byte[] buffer, int begin, int end) throws IOException{
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

		System.arraycopy(buffer, begin, mWriteCash, mCashLength, len);
		mCashLength +=len;
	}

	@Override
	public synchronized void syncWrite() throws IOException {
		seek(mSeek);
		if(mRAFile == null) {
			throw new IOException();
		}
		if(mWriteCash.length == 0) {
			throw new IOException();
		}
		long cp = mRAFile.getFilePointer();
		mRAFile.seek(mCashStartPointPerFile);
		mRAFile.write(mWriteCash, 0, mCashLength);
		mCashStartPointPerFile += mCashLength;
		mCashLength = 0;
		seek(cp);
	}
}
