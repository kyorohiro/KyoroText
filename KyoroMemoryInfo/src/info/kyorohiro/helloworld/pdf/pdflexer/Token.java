package info.kyorohiro.helloworld.pdf.pdflexer;

import java.util.LinkedList;

public class Token {
	private int mType = 0;
	private byte[] mBuffer = null;
	private LinkedList<Token> mChild = new LinkedList<Token>();
	private long mStart = 0;
	private long mEnd = 0;

	public Token(int type, byte[] buffer, long start, long end) {
		mType = type;
		mBuffer = buffer;
		mStart = start;
		mEnd = end;
	}

	@Override
	public String toString() {
		return "Token "+mType+","+ new String(mBuffer);
	}

	public int getType() {
		return mType;
	}

	public byte[] getBuffer() {
		return mBuffer;
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
