package info.kyorohiro.helloworld.display.simple;

public interface  MyInputConnection {
	CharSequence getComposingText();
	void addCommitText(CommitText text);
	void setIMEController(IMEController controller);
	CommitText popFirst();
}
