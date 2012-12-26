package info.kyorohiro.samples.android.surfaceview_editor;

public interface  MyInputConnection {
	CharSequence getComposingText();
	void addCommitText(CommitText text);
	CommitText popFirst();
}
