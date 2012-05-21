package info.kyorohiro.helloworld.pdf.pdflexer;

import info.kyorohiro.helloworld.io.VirtualMemory;
import info.kyorohiro.helloworld.pdf.pdfparser.Token;

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
		return new Token(mId, null, mStart, mEnd);
	}

	@Override
	public boolean matchHead(VirtualMemory source) {
		mPoint = -1;
		try {
			mStart = mPoint = source.getFilePointer();
			C:for (int i=0; i < mList.length; i++) {
				mPattern = mList[i];
				for (byte b : mPattern) {
					if ((!mExclude&&b!=source.read())
						||(mExclude&&b==source.read())) {
						back(source);
						continue C;
					}
				}
				if (!mIsLine) {
					break;
				}
			}
			mEnd = source.getFilePointer();
			if (mStart == mEnd) {
				return true;
			} else {
				return false;
			}
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
			mPoint = -1;
			mStart = 0;
			mEnd = 0;
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
}