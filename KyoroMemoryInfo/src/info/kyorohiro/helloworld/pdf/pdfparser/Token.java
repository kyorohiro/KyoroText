package info.kyorohiro.helloworld.pdf.pdfparser;

import java.util.LinkedList;

public class Token {
	private int mType = 0;
	private byte[] mBuffer = null;
	private LinkedList<Token> mChild = new LinkedList<Token>();

	public Token(int type, byte[] buffer) {
		mType = type;
		mBuffer = buffer;
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
