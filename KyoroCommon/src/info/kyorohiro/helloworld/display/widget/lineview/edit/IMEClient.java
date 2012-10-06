package info.kyorohiro.helloworld.display.widget.lineview.edit;

public interface IMEClient {
	public void setCursor(int row, int col);
	public void pushCommit(CharSequence text, int cursor) ;
}
