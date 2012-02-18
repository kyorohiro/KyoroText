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
//		return super.getX();
		return mTargetObject.getX();
	}

	@Override
	public int getY() {
//		return super.getY();
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
		int w = mTargetObject.getWidth();
		int h = mTargetObject.getHeight();
		int sp = mStart;
		int ep = mEnd;
		int s = mSize;
		if (s == 0) {
			s = 1;
		}
		int barWidth = w / 20;
		double barHeigh = h / (double) s;
		graphics.drawLine(w - barWidth, (int) (barHeigh * sp), w, (int) (barHeigh * sp));
		graphics.drawLine(w - barWidth, (int) (barHeigh * ep), w, (int) (barHeigh * ep));
		graphics.drawLine(w - barWidth, (int) (barHeigh * sp), w - barWidth, (int) (barHeigh * ep));
		graphics.drawLine(w, (int) (barHeigh * ep), w, (int) (barHeigh * sp));
		graphics.drawLine(w - barWidth, (int) (barHeigh * sp), w, (int) (barHeigh * ep));
		graphics.drawLine(w - barWidth, (int) (barHeigh * ep), w, (int) (barHeigh * sp));
	}
}
