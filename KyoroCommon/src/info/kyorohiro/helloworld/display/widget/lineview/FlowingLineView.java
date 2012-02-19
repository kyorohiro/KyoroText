package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.CyclingListInter;
import android.graphics.Color;

public class FlowingLineView extends SimpleDisplayObjectContainer {
	private ScrollBar scrollBar = null;
	private LineView viewer = null;
	
	public FlowingLineView(CyclingListInter<FlowingLineDatam> inputtedText) {
		scrollBar = new ScrollBar(this);
		viewer = new LineView(inputtedText);
		this.addChild(viewer);
		this.addChild(new Layout());
		this.addChild(scrollBar);
	}

	public void setPosition(int position) {
		viewer.setPosition(position);
	}

	public int getPosition() {
		return viewer.getPosition();
	}

	public void setCyclingList(CyclingListInter<FlowingLineDatam> inputtedText) {
		viewer.setCyclingList(inputtedText);
	}

	public CyclingListInter<FlowingLineDatam> getCyclingList() {
		return viewer.getCyclingList();
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
