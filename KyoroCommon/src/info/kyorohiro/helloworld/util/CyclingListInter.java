package info.kyorohiro.helloworld.util;

public interface CyclingListInter<N> {
	public N get(int i);
	public void add(N element);
	public int getNumberOfStockedElement();
	public void clear();
	public N[] getLast(N[] ret, int numberOfRetutnArrayElement);
	public N[] getElements(N[] ret, int start, int end) ;
}
