package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.android.adapter.MyBreakText;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;

public class EmptyLineViewBufferSpecImpl implements LineViewBufferSpec{

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
	public KyoroString get(int i) {
		return new KyoroString(""+i, i);
	}

	@Override
	public int getNumberOfStockedElement() {
		return 0;
	}

	@Override
	public int getMaxOfStackedElement() {
		return 0;
	}

	@Override
	public KyoroString[] getElements(KyoroString[] ret, int start, int end) {
		return ret;
	}

	@Override
	public BreakText getBreakText() {
		return new MyBreakText();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
