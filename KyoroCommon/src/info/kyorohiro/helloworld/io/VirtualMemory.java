package info.kyorohiro.helloworld.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;

public class VirtualMemory {
	private int mBufferLength = 1024;
	private RandomAccessFile mFile = null;
	private long mFilePointer = 0;
	private CyclingByteArray mBuffer = null;
	
	private LinkedList<Long> mMark = new LinkedList<Long>();
	public void pushMark() {
		try {
			mMark.addLast(getFilePointer());
		} catch (IOException e) {
			e.printStackTrace();
			// ‚±‚±‚É‚Í‚±‚È‚¢
		}
	}

	public void backToMark() {
		try {
			seek(mMark.getLast());
		} catch (IOException e) {
			e.printStackTrace();
			// ‚±‚±‚É‚Í‚±‚È‚¢
		}
	}

	public long popMark() {
		return mMark.removeLast();
	}

	public VirtualMemory(File base, int cashSize) throws FileNotFoundException {
		mFile = new RandomAccessFile(base, "r");
		mBufferLength = cashSize;
		mBuffer = new CyclingByteArray(mBufferLength);
	}

	public int read() throws IOException {
		try {
			if(!mBuffer.isBuffered(mFilePointer)){
				mFile.seek(mFilePointer);
				byte[] b= new byte[mBufferLength/4]; 
				int len = mFile.read(b);
				if(len==-1){
					return -1; 
				}
				mBuffer.set(mFilePointer, b, 0, len);
			}
			int ret = 0xFF&(int)mBuffer.read(mFilePointer);
			mFilePointer++;
			return ret;
		} catch(CyclingByteArrayException e){
			e.printStackTrace();
			throw new IOException("--"+e.toString());
		}

	}

	public void seek(long pos) throws IOException{
		mFilePointer = pos;
	}
	
	public long getFilePointer() throws IOException {
		return mFilePointer;//mFile.getFilePointer();
	}

	public long length() throws IOException {
		return mFile.length();
	}

	public void close() throws IOException {
		mFile.close();
	}

	public static class CyclingByteArray {
		private byte[] mBuffer = null;
		private int mBufferedLength = 0;
		private long mCurrentFilePointer = 0;

		public CyclingByteArray(int length) {
			mBuffer = new byte[length];
		}


		public int getMaxOfBufferLength(){
			return mBuffer.length;
		}


		public long getPointer(){
			return mCurrentFilePointer;
		}

		public void set(long fpStartIndex, byte[] buffer, int s, int e) throws CyclingByteArrayException {
			if((e-s)>getMaxOfBufferLength()){
				throw new CyclingByteArrayException("you can set only buffer getBufferLength ,"+(e-s)+">"+getMaxOfBufferLength());
			}
			if(isBuffered(fpStartIndex-1)){
				int index = getLocalBufferIndex(fpStartIndex);
				int recycledIndexEnd = (index-1);
				// you use current buffer as cash by index-1
				int inputLen = e-s;
				//int currentBufferStart = 
				int requireSize = inputLen+recycledIndexEnd+1;// require last index +1
				if(requireSize<=getMaxOfBufferLength()){
					// only add
					for(int fp=0,i=s;i<e;i++,fp++){
						mBuffer[index+fp] = buffer[i];
					}
					mBufferedLength = (e-s)+recycledIndexEnd+1;
					//mCurrentFilePointer is nochange
				} else {
					// consume
					int consumeSize = requireSize-getMaxOfBufferLength();
					int fp = 0;//
					int start = recycledIndexEnd - consumeSize+1;
					for(int i=start;i<=recycledIndexEnd;i++,fp++){
						mBuffer[fp] = mBuffer[i];
					}
					
					for(int i=s;i<e;i++,fp++){
						mBuffer[fp] = buffer[i];
					}
					mBufferedLength = fp;
				}

				//
				//
			} else {
//			    mCurrentFilePointer = fpStartIndex;
				mBufferedLength = e-s;
				for(int j=0,i=s;i<e;i++,j++){
					mBuffer[j] = buffer[i];
				}
			}
			mCurrentFilePointer = (e-s)+fpStartIndex-(mBufferedLength);
		}

		private int getLocalBufferIndex(long fp){
			int index = (int)(fp-mCurrentFilePointer);
			return index;
		}

		public byte read(long fp) throws CyclingByteArrayException {
			try {
				int index = getLocalBufferIndex(fp);
				if(index<0||mBuffer.length<=index||mBufferedLength<=index) {
					throw new CyclingByteArrayException("outofbounds ref"+index);					
				}
				return mBuffer[index];
			}catch(IndexOutOfBoundsException e){
				throw new CyclingByteArrayException("outofbounds", e);
			}
		}

		public boolean isBuffered(long fp) {
			int index = getLocalBufferIndex(fp);
			if(index<0||mBufferedLength<=index){
				return false;
			} else {
				return true;
			}
					
		}
	}

	public static class CyclingByteArrayException extends Exception {
		public CyclingByteArrayException(String message) {
			super(message);
		}
		public CyclingByteArrayException(String message, Throwable t) {
			super(message, t);
		}
	}
}
