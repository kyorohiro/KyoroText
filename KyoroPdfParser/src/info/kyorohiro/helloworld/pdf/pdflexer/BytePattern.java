package info.kyorohiro.helloworld.pdf.pdflexer;

import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.IOException;

public class BytePattern extends SourcePattern {
	// pattern data
	private byte[][] mList = null;
	private byte[] mPattern = null;

	// prev file pointer;
	private long mPoint = 0;

	//[]+
	//+をあらわす
	private boolean mIsLine = false;

	//[^]
	//^をあらわす。
	private boolean mExclude = false;
	
	//
	// タグの開始位置(if filepointer)
	private long mStart = 0;
	
	//
	// タグの終了位置 (if filepointer)
	private long mEnd = 0;

	// 管理番号
	// Lexer.ID_.*
	private int mId = 0;

	public BytePattern(int id, final byte[][] pattern) {
		mList = pattern;
		mId = id; 
	}

	public BytePattern(int id, final byte[][] pattern, boolean isLine, boolean exclude) {
		mList = pattern;
		mIsLine = isLine;
		mId = id;
		mExclude = exclude;
	}

	public Token newToken() {
		return new Token(mId, mStart, mEnd);
	}


	@Override
	public boolean matchHead(VirtualMemory source) {
		try {
			long nextEnd = 0;
			mStart = mPoint = mEnd = source.getFilePointer();
			do {
				nextEnd = matchBytePattern(source, mList);
				if (nextEnd<0) {
					if (mIsLine) {
						if(mEnd != mStart) {
							source.seek(mEnd);
							return true;
						} else { 
							return false;
						}
					} else {
						return false;
					}
				} else {
					mEnd = nextEnd;
					source.seek(mEnd);
					if (mIsLine) {
						
					} else {
						return true;
					}
				}
			} while(true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void back(VirtualMemory source) {
		try {
			if (mPoint < 0) {
				return;
			}
			source.seek(mPoint);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] matchedData(VirtualMemory source) {
		byte[] ret = new byte[mPattern.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = mPattern[i];
		}
		return ret;
	}
	
	public long start() {
		return mStart;
	}

	public long end() {
		return mEnd;
	}



	// 
	private long matchBytePattern(VirtualMemory source, byte[][] pattern) {
		if(mExclude){
			//すべてがマッチしないこと
			try {

				for(int i=0;i<pattern.length;i++) {
					try {
						source.pushMark();
						if(matchBytes(source, pattern[i])){
							return -1;
						}
					} finally {
						source.backToMark();
						source.popMark();
					}
				}
				// must check per 1byte.
				return source.getFilePointer()+1;
			} catch (IOException e) {
				e.printStackTrace();
				// ここにくることはない
				return -1;
			}
		} else {
			//どれかひとつマッチしていること
			boolean matched = false;
			for(int i=0;i<pattern.length;i++) {
				try {
					source.pushMark();
					matched = matchBytes(source, pattern[i]);
					if(matched){
						return source.getFilePointer();
					}
				} catch(IOException e) {
					e.printStackTrace();
					// ここにくることはない
				} finally {
					source.backToMark();
					source.popMark();
				}
			}
			return -1;
		}
	}

	private boolean matchBytes(VirtualMemory source, byte[] pattern) throws IOException {
			for(int i=0;i<pattern.length;i++){
				int buffer = source.read();
				if(buffer == -1) {
					throw new IOException();
				}
				if(pattern[i] != buffer){
					return false;
				}
			}
			return true;
	}

}