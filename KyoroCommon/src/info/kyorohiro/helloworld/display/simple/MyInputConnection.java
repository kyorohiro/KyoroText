package info.kyorohiro.helloworld.display.simple;

public interface  MyInputConnection {
	CharSequence getComposingText();
	void addCommitText(CharSequence text, int cursorPositionOnCommitedText);
	void addKeyEvent(int keyEvent);
	CommitText popFirst();
	boolean pushingCtr();
}
