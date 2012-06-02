package info.kyorohiro.helloworld.pdf.pdflexer;

import info.kyorohiro.helloworld.io.VirtualMemory;

import java.io.IOException;
import java.util.LinkedList;

public class Token {
	private int mType = 0;
	private LinkedList<Token> mChild = new LinkedList<Token>();
	private long mStart = 0;
	private long mEnd = 0;

	public Token(int type, long start, long end) {
		mType = type;
		mStart = start;
		mEnd = end;
	}

	@Override
	public String toString() {
		return "Token "+mType+","+mStart+","+mEnd;
	}

	public int getType() {
		return mType;
	}

	public byte[] getBuffer(VirtualMemory memory)  {
		int len = (int)(mEnd-mStart);
		if(len<0){
			return new byte[0];
		}
		byte[] buffer = new byte[len];
		try {
			memory.seek(mStart);
			for(int i=0;i<len;i++){
				buffer[i] = (byte)memory.read();
			}
		} catch(IOException e) {
			return new byte[0];			
		}
		return buffer;
	}

	public void addFirst(Token child) {
		mChild.addFirst(child);
	}

	public void add(Token child) {
		mChild.add(child);
	}

	public Token getChild(int index) {
		return mChild.get(index);
	}

	public int numOfChild() {
		return mChild.size();
	}
}
