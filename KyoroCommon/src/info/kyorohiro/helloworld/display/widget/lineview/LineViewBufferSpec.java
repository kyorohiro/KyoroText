package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.io.BreakText;

public interface LineViewBufferSpec {
	public int getNumOfAdd();
	public void clearNumOfAdd();
	public LineViewData get(int i);
	public int getNumberOfStockedElement();
	public int getMaxOfStackedElement();
	public LineViewData[] getElements(LineViewData[] ret, int start, int end) ;
	public BreakText getBreakText();
}
