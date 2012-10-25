package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;

public interface LineViewBufferSpec {
	public int getNumOfAdd();
	public void clearNumOfAdd();
	public void isSync(boolean isSync);
	public KyoroString get(int i);
	public int getNumberOfStockedElement();
	public int getMaxOfStackedElement();
	public KyoroString[] getElements(KyoroString[] ret, int start, int end) ;
	public BreakText getBreakText();
}
