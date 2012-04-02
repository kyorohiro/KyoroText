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
	
	public int getTextSize() {
		return viewer.getTextSize();
	}

	public void setTextSize(int textSize) {
		viewer.setTextSize(textSize);
	}

	public void setPositionY(int position) {
		viewer.setPositionY(position);
	}

	public void setPositionX(int x) {
		viewer.setPositionX(x);
	}
	public int getPositionX() {
		return viewer.getPositionX();
	}

	public int getPositionY() {
		return viewer.getPositionY();
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
