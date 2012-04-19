package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.CyclingListInter;
import android.graphics.Color;

public class FlowingLineView extends SimpleDisplayObjectContainer {
	private ScrollBar scrollBar = null;
	private LineView viewer = null;

	public FlowingLineView(CyclingListInter<FlowingLineDatam> inputtedText, int textSize) {
		scrollBar = new ScrollBar(this);
		viewer = new LineView(inputtedText, textSize);
		this.addChild(viewer);
		this.addChild(new Layout());
		this.addChild(scrollBar);
	}

	public LineView getLineView() {
		return viewer;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		super.paint(graphics);
	}

	private class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			CyclingListInter<?> list = viewer.getCyclingList();
			int size = list.getNumberOfStockedElement(); 
			scrollBar.setStatus(viewer.getShowingTextStartPosition(), viewer.getShowingTextEndPosition(), size);
			viewer.setRect(graphics.getWidth(), graphics.getHeight());
		}
	}
}
