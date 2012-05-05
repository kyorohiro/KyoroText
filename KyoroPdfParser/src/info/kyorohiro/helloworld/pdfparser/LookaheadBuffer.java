package info.kyorohiro.helloworld.pdfparser;

import java.util.ArrayList;

public class LookaheadBuffer {
	ArrayList<Integer> mark = new ArrayList<Integer>();
	ArrayList<Token> token = new ArrayList<Token>();
	int mCurrentPosition = 0;

	public void next() {
		mCurrentPosition++;
	}

	public int getCurrentPosition() {
		return mCurrentPosition; 
	}

	public Token getCurrentToken() {
		return token.get(mCurrentPosition);
	}

	public int getMarkPosition() {
		if(0>=mark.size()){
			return 0;
		} else {
			return mark.get(mark.size()-1);
		}
	}

	public int numOfLookahead() {
		return token.size();
	}

	public boolean lookaheaded(int position) {
		int numOfLookahead = numOfLookahead();
		int markPosition = getMarkPosition();
		if(position<(numOfLookahead-markPosition)){
			return true;
		} else {
			return false;
		}
	}

	public void mark() {
		mark.add(mCurrentPosition);		
	}

	public void release() {
		int index = mark.size()-1;
		if(index<0){
			return;
		}
		mark.remove(mark.size()-1);	
	}

	public void addToken(Token t) {
		token.add(t);
	}

	public void clear() {
		mark.clear();
		token.clear();
		mCurrentPosition = 0;
	}
}
