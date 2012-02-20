package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;

public class ScrollBar extends SimpleDisplayObject {

	private SimpleDisplayObject mTargetObject = null;
	private int mStart;
	private int mEnd;
	private int mSize;

	public ScrollBar(SimpleDisplayObject target) {
		mTargetObject = target;
	}

	@Override
	public int getX() {
		return mTargetObject.getX();
	}

	@Override
	public int getY() {
		return mTargetObject.getY();
	}

	public void setStatus(int start, int end, int size) {
		mStart = start;
		mEnd = end;
		mSize = size;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		showScrollBar(graphics);
	}

	private void showScrollBar(SimpleGraphics graphics) {
		// set scrollbar color
		graphics.setStrokeWidth(3);
		graphics.setColor(0xAAFFFF00);

		// 
		int w = mTargetObject.getWidth();
		int h = mTargetObject.getHeight();
		int sp = mStart;
		int ep = mEnd;
		int s = mSize;
		if (s == 0) {
			s = 1;
		}

		// calc scrollbar rect
		int barWidth = w / 20;
		double barHeigh = h / (double) s;
		int barStartY = (int) (barHeigh * sp);
		int barEndY = (int) (barHeigh * ep);
		int barStartX = w - barWidth;
		int barEndX = w;

		// draw scrollbar
		graphics.drawLine(barStartX, barStartY, barEndX, barStartY);
		graphics.drawLine(barStartX, barEndY, barEndX, barEndY);

		graphics.drawLine(barStartX, barStartY , barStartX, barEndY);
		graphics.drawLine(barEndX, barStartY, barEndX, barEndY);

		graphics.drawLine(barStartX, barStartY , barEndX, barEndY);
		graphics.drawLine(barEndX, barStartY, barStartX, barEndY);
	}
}
