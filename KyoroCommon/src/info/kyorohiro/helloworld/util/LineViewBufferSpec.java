package info.kyorohiro.helloworld.util;

public interface LineViewBufferSpec<N> {
//	public N get(int i);
	public int getNumOfAdd();
	public void clearNumOfAdd();
	public int getNumberOfStockedElement();
	public N[] getElements(N[] ret, int start, int end) ;
}
