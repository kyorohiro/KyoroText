package info.kyorohiro.helloworld.ext.textviewer.viewer;

import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CyclingList;

public class LatestAccessCashing {

	private CyclingList<KyoroString> mCash = null;

	public LatestAccessCashing(int num) {
		mCash = new CyclingList<KyoroString>(num);
	}

	public synchronized void addLine(KyoroString line, boolean check) {
		int num = mCash.getNumberOfStockedElement();
		if(check) {
			for(int i=num-1;i>=0;i--) {
				if(line.getLinePosition() == mCash.get(i).getLinePosition()) {
					return;
				}
			}
		}
		mCash.add(line);
	}

	public static int COlOR_RECASH2 = SimpleGraphicUtil.parseColor("#FF112299");
	public synchronized KyoroString get(int index) {
		int num = mCash.getNumberOfStockedElement();
		for(int i=num-1;i>=0;i--) {
			if(index == mCash.get(i).getLinePosition()) {
				KyoroString c =  mCash.get(i);
				{
					CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
					int co = cp.getProperty(TextViewer.KEY_TEXTVIEWER_FONT_COLOR3, COlOR_RECASH2);
					c.setColor(co);
				}
				mCash.moveLast(c, i);
				return c;
			}
		}
		return null;
	}
}