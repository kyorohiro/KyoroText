package info.kyorohiro.helloworld.display.widget.editview;

public interface IMEClient {
	public void setCursor(int row, int col);
	public void pushCommit(CharSequence text, int cursor) ;
}
