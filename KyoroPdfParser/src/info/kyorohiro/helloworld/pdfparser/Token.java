package info.kyorohiro.helloworld.pdfparser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Token {
	public int mType = 0;
	public String mText = "";
	public String mPattern = "";
	public CharSequence mValue = null;
	private LinkedList<Token> mChild = new LinkedList<Token>();

	public Token(int type, String text, String patterm) {
		mType = type;
		mText = text;
		mPattern = patterm;
	}
	
	public Token(Token source, CharSequence data) {
		mType = source.mType;
		mText = source.mText;
		mPattern = source.mPattern;
		mValue = data;
	}

	@Override
	public String toString() {
		return "Token "+mType+","+mText+","+mPattern+","+mValue+","+mChild.size();
	}

	public CharSequence getValue() {
		return mValue;
	}

	public CharSequence getIdentify() {
		return mText;
	}

	public int getType() {
		return mType;
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
