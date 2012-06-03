package info.kyorohiro.helloworld.pdf.pdfparser;

import info.kyorohiro.helloworld.pdf.pdflexer.Token;

import java.util.LinkedList;

public class PdfStack {
	private LinkedList<Integer> mark = new LinkedList<Integer>();
	private LinkedList<Token> token = new LinkedList<Token>();

	public void mark() {
		int index = token.size() - 1;
		mark.addLast(index);
	}

	public int numOfMark() {
		return mark.size();
	}

	public int numOfToken() {
		return token.size();
	}

	public void releaseMarkOnly() {
		if(mark.size() > 0) {
			mark.removeLast();
		}
	}


	public void release() {
//		int num = mark.removeLast();
//		while ((num + 1) < mark.size() && mark.size() != 0) {
//			pop();
//		}
		while(isMarked()){
			pop();
		}
		if(mark.size() > 0) {
			mark.removeLast();
		}
	}

	public boolean isMarked() {
		int num = 0;
		if (0 < mark.size()) {
			num = mark.getLast();
		}
		if ((num + 1) < token.size()) {
			return true;
		} else {
			return false;
		}
	}

	public void push(Token t) {
		token.addLast(t);
	}

	public Token pop() {
		return token.removeLast();
	}

	public Token peek() {
		return token.getLast();
	}

	public void testPrint() {
		for (int i = 0; i < token.size(); i++) {
			android.util.Log.v("pdfparser", "t[" + i + "]="
					+ token.get(i).toString());
		}

		for (int i = 0; i < mark.size(); i++) {
			android.util.Log.v("pdfparser", "m[" + i + "]" + mark.get(i));
		}

	}
}

