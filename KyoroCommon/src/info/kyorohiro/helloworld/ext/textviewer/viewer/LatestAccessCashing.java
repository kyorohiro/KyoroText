package info.kyorohiro.helloworld.ext.textviewer.viewer;

import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CyclingList;

public class LatestAccessCashing {

	private CyclingList<KyoroString> mCash = null;

	public LatestAccessCashing(int num) {
		mCash = new CyclingList<KyoroString>(num);
	}

	public synchronized void addLine(KyoroString line) {
		int num = mCash.getNumberOfStockedElement();
		for(int i=num-1;i>=0;i--) {
			if(line.getLinePosition() == mCash.get(i).getLinePosition()) {
				return;
			}
		}
		mCash.add(line);
	}

	public synchronized KyoroString get(int index) {
		int num = mCash.getNumberOfStockedElement();
		for(int i=num-1;i>=0;i--) {
			if(index == mCash.get(i).getLinePosition()) {
				KyoroString c =  mCash.get(i);
				c.setColor(SimpleGraphicUtil.GREEN);
				return c;
			}
		}
		return null;
	}
}