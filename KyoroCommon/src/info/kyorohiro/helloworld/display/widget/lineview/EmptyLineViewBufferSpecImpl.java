package info.kyorohiro.helloworld.display.widget.lineview;

import java.util.LinkedList;

import info.kyorohiro.helloworld.display.simple.BreakText;
import info.kyorohiro.helloworld.display.simple.EmptyBreakText;
import info.kyorohiro.helloworld.display.simple.EmptySimpleFont;
import info.kyorohiro.helloworld.text.KyoroString;

public class EmptyLineViewBufferSpecImpl implements LineViewBufferSpec{

	private LinkedList<KyoroString> mList = new LinkedList<KyoroString>();
	private EmptyBreakText mBreakText = null;

	public EmptyLineViewBufferSpecImpl(int maxLineLength) {
		EmptySimpleFont font = new EmptySimpleFont();
		mBreakText = new EmptyBreakText(font,(int)(font.getFontSize()*maxLineLength)+(int)font.getFontSize()/2);
	}

	public void append(KyoroString text) {
		mList.add(text);
	}

	@Override
	public int getNumOfAdd() {
		return 0;
	}

	@Override
	public void clearNumOfAdd() {
	}

	@Override
	public void isSync(boolean isSync) {
	}

	@Override
	public boolean isSync() {
		return true;
	}

	@Override
	public KyoroString get(int i) {
		return mList.get(i);
	}

	@Override
	public int getNumberOfStockedElement() {
		return mList.size();
	}

	@Override
	public int getMaxOfStackedElement() {
		return mList.size();
	}

	@Override
	public KyoroString[] getElements(KyoroString[] ret, int start, int end) {
		int len = end-start;
		for(int i=0;i<len;i++){
			ret[i] = mList.get(i+start);
		}
		return ret;
	}

	@Override
	public BreakText getBreakText() {
		return mBreakText;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isLoading() {
		// TODO Auto-generated method stub
		return false;
	}

}
