package info.kyorohiro.helloworld.util;

public interface CyclingListInter<N> extends LineViewBufferSpec<N>{
	public N get(int i);
	public int getNumOfAdd();
	public void clearNumOfAdd();
	public void add(N element);
	public int getNumberOfStockedElement();
	public void head(N element);
	public void clear();
	public N[] getLast(N[] ret, int numberOfRetutnArrayElement);
	public N[] getElements(N[] ret, int start, int end) ;
}
