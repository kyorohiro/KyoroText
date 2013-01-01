package info.kyorohiro.helloworld.display.widget.editview;

//
//todo don't fix
public interface IMEClient {
	public void setCursor(int row, int col);
	public void pushCommit(CharSequence text, int cursor) ;
}
