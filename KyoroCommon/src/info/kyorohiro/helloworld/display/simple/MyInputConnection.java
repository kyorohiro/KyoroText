package info.kyorohiro.helloworld.display.simple;

public interface  MyInputConnection {
	CharSequence getComposingText();
	void addCommitText(CommitText text);
	//void addCommitText(CharSequence text, int cursorPositionOnCommitedText);
	//void addKeyEvent(int keyEvent);
	void setIMEController(IMEController controller);
	CommitText popFirst();
}
