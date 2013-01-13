package info.kyorohiro.helloworld.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
public class VirtualFile implements KyoroFile {
//	private File mBase = null;
	private RandomAccessFile mRAFile = null;
	private byte[] mWriteCash = null;
	private long mCashStartPointPerFile = 0;
	private int mCashLength = 0;
	public final static int CHUNK_SIZE = 100;
	private long mSeek = 0;
	private File mBase = null;

	public VirtualFile(File base, int writeCashSize) {
		mBase = base;
		int numOfchunk = writeCashSize/CHUNK_SIZE;
		mWriteCash = new byte[CHUNK_SIZE*numOfchunk];
//		android.util.Log.v("kiyo","base"+base+","+writeCashSize);
//		android.util.Log.v("kiyo","num"+numOfchunk+","+mWriteCash.length);
	}

	public File getBase() {
		return mBase;
	}

	private boolean mInit = false;
	private void init() throws IOException {
		if(mRAFile== null || !mInit) {
			mInit = true;
			mRAFile = new RandomAccessFile(mBase, "rw");
		}
		if(mRAFile == null) {
			throw new IOException();
		}
	}

	@Override
	public synchronized void seek(long point) throws IOException {
		//android.util.Log.v("kiyo", "---------seek---------" + point);
		init();
		mSeek = point;
		if(mSeek <0) {
			mSeek = 0;
		}
		if(mSeek<mRAFile.length()&&point!=mRAFile.getFilePointer()) {
			mRAFile.seek(mSeek);
		}
	}

	public synchronized long getFilePointer() {
		return mSeek;
	}

	@Override
	public synchronized long length() throws IOException {
		//android.util.Log.v("kiyo", "---------length---------");
		init();
		seek(mSeek);
		long raLen = mRAFile.length();
		long caLen = mCashStartPointPerFile+mCashLength;
//		android.util.Log.v("kiyo", "----reLen-" + raLen+",caLen="+caLen);
		if(raLen > caLen) {
			return raLen;
		} else {
			//android.util.Log.v("kiyo", "---------caLen---------" + caLen);
			return caLen;
		}
	}
	
	public synchronized void debugLightInfo() {
//		String str;
//		try {
//			str = new String(mWriteCash, 0, mWriteCash.length,"utf8");
//			String[] list = str.split("\n");
//			android.util.Log.v("kiyo","### ----------------------"+mWriteCash.length);
//			if(list == null){
//				return;
//			}
//			for(String s: list) {
//				android.util.Log.v("kiyo","### "+s.substring(0, 3));
//			}
//			android.util.Log.v("kiyo","### ----------------------"+str);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	@Override
	public synchronized int read(byte[] buffer) throws IOException {
//		android.util.Log.v("kiyo", "---------read---------"+buffer.length);
		//
		//


		init();
		seek(mSeek);


		long current = getFilePointer();
		long raLen = mRAFile.length();
		long caLen = mCashStartPointPerFile+mCashLength;

//		android.util.Log.v("kiyo","cu="+current+"ra="+raLen+",ca="+caLen);
		if(current >= raLen&& current >= caLen) {
			return -1;
		}
		int ret = 0;
		if(mCashStartPointPerFile <= current && current< (mCashStartPointPerFile+mCashLength)) {
			int srcPos = (int)(current-mCashStartPointPerFile);
			int len = mCashLength -srcPos;
			if(len>buffer.length) {
				len = buffer.length;
			}
			System.arraycopy(mWriteCash, srcPos, buffer, 0, len);
			if((mCashLength-srcPos)<buffer.length&&raLen>caLen) {
				mRAFile.seek(current + mCashLength-srcPos);
				ret = mRAFile.read(buffer, mCashLength-srcPos, buffer.length-(mCashLength-srcPos));
			}
			if(ret == -1) {
				ret = 0;
			}
			ret = len + ret;
		} else {
			ret = mRAFile.read(buffer);
			if(ret!=-1&&raLen<caLen) {
				int srcPos = (int)(current-mCashStartPointPerFile+ret);
				int len = buffer.length-ret;
				if(len >mCashLength-srcPos) {
					len = mCashLength-srcPos;
				}
				if(0<=srcPos&&srcPos+len<=mCashLength) {
					System.arraycopy(mWriteCash, srcPos, buffer, ret, len);
					ret += len;
				}
			}
			
		}
		
		seek(current+ret);
		return ret;
	}

	@Override
	public synchronized void close() throws IOException {
		init();
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

	public synchronized void addChunk(byte[] buffer) throws IOException{
		addChunk(buffer, 0, buffer.length);
	//	if(mWriteCash.length != 0) {
	//		debugLightInfo();
	//	}
	}
	@Override
	public synchronized void addChunk(byte[] buffer, int begin, int end) throws IOException{
		init();
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
		init();
		if(mRAFile == null) {
			throw new IOException();
		}
		if(mWriteCash == null) {
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
	//	if(len+mCashLength>=mWriteCash.length) {
	//		syncWrite();
	//	}

	}

	@Override
	public synchronized void syncWrite() throws IOException {
//		android.util.Log.v("kiyo", "-------------syncWrite() --A");
		init();
//		android.util.Log.v("kiyo", "-------------syncWrite() --B");
		seek(mSeek);
//		android.util.Log.v("kiyo", "-------------syncWrite() --C");
		if(mRAFile == null) {
			throw new IOException();
		}
		if(mWriteCash.length == 0) {
			throw new IOException();
		}
//		android.util.Log.v("kiyo", "-------------syncWrite() --D");
		long cp = mRAFile.getFilePointer();
		mRAFile.seek(mCashStartPointPerFile);
		mRAFile.write(mWriteCash, 0, mCashLength);
		mCashStartPointPerFile += mCashLength;
		mCashLength = 0;
		seek(cp);
//		android.util.Log.v("kiyo", "-------------syncWrite() --E "+cp);
	}
}
