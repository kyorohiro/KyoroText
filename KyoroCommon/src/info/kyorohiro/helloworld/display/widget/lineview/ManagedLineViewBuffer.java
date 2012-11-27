package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;

public class ManagedLineViewBuffer implements LineViewBufferSpec {
	private LineViewBufferSpec mBase = null; 
	private int mNumOfReserve = 0;
	private boolean disposed =false;

	public void reserve() {
		mNumOfReserve++;
	}

	public void release() {
		mNumOfReserve--;
		assert(mNumOfReserve>0);
		if(mNumOfReserve<0){
			mNumOfReserve = 0;
		}
		if(disposed){
			dispose();
		}
	}

	public LineViewBufferSpec getBase(){
		return mBase;
	}
	public ManagedLineViewBuffer(LineViewBufferSpec base) {
		mBase = base;
	}

	@Override
	public int getNumOfAdd() {
		return mBase.getNumOfAdd();
	}

	@Override
	public void clearNumOfAdd() {
		mBase.clearNumOfAdd();
	}

	@Override
	public void isSync(boolean isSync) {
		mBase.isSync(isSync);
	}

	@Override
	public boolean isSync() {
		return mBase.isSync();
	}

	@Override
	public KyoroString get(int i) {
		return mBase.get(i);
	}

	@Override
	public int getNumberOfStockedElement() {
		return mBase.getNumberOfStockedElement();
	}

	@Override
	public int getMaxOfStackedElement() {
		return mBase.getMaxOfStackedElement();
	}

	@Override
	public KyoroString[] getElements(KyoroString[] ret, int start, int end) {
		return mBase.getElements(ret, start, end);
	}

	@Override
	public BreakText getBreakText() {
		return mBase.getBreakText();
	}

	@Override
	public void dispose() {
		disposed = true;
		if(mNumOfReserve<=0){
			mBase.dispose();
		}
	}
	
}