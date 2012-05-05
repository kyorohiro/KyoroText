package info.kyorohiro.helloworld.pdfparser;

import java.util.ArrayList;
import java.util.LinkedList;

public class LookaheadParser {

	protected LookaheadBuffer mLookahead = new LookaheadBuffer();
	protected Lexer mLexer;

	public LookaheadParser(Lexer lexer) {
		mLexer = lexer;
	}

	public Token next(int IdOfSet) throws GotoException{
		Token t = _get(IdOfSet);
		if(t.mType != IdOfSet) {
			throw new GotoException();
		}
		mLookahead.next();
		return t;
	}

	public boolean isHead(int IdOfSet) {
		Token t = _get(IdOfSet);
		if(t.mType==IdOfSet){
			return true;
		} else {
			return false;
		}
	}

	private Token _get(int IdOfSet) {
		lookahead(mLookahead.getCurrentPosition());
		Token t = mLookahead.getCurrentToken();
		return t;
	}

	public void mark() {
		mLookahead.mark();
	}

	public void lookahead(int position) {
		if(!mLookahead.lookaheaded(position))
		{
			int size = mLookahead.numOfLookahead();
			for(int i=size;i<=position;i++){
				Token t = mLexer.nextToken();
				//todo this code throw error;
				mLookahead.addToken(t);
			}
		}
	}

	public void release() {
		mLookahead.release();
	}
}
