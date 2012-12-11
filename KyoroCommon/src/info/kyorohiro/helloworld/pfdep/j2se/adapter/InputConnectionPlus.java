package info.kyorohiro.helloworld.pfdep.j2se.adapter;

import java.util.LinkedList;

import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;

public class InputConnectionPlus implements MyInputConnection{

	private CharSequence mComposingText = "";
	private LinkedList<CommitText> mCommitTextList = new LinkedList<CommitText>();
	public void setComposingText(CharSequence composingText) {
		mComposingText = composingText;
	}

	@Override
	public CharSequence getComposingText() {
		return mComposingText;
	}

	public void addCommitText(CommitText commited) {
		mCommitTextList.add(commited);
	}

	// CommitText を受け取るものだけ残す
	@Override
	public void addCommitText(CharSequence text, int cursorPositionOnCommitedText) {
		CommitText c = new CommitText(text, 1);
		c.pushingCtrl(pushingCtr());
		mCommitTextList.add(c);
	}

	// CommitText を受け取るものだけ残す
	@Override
	public void addKeyEvent(int keyEvent) {
		mCommitTextList.add(new CommitText(keyEvent));
	}

	@Override
	public CommitText popFirst() {
		if(mCommitTextList.size()>0){
			return mCommitTextList.removeFirst();
		} else {
			return null;
		}
	}


	// いらなくなる予定
	@Override
	public boolean pushingCtr() {
		return false;
	}

}
