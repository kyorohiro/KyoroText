package info.kyorohiro.helloworld.display.widget.lineview;

import java.lang.ref.WeakReference;

public class Point {
	WeakReference<LineView> mR;

	public Point(int point, LineView v) {
		mPoint = point;
		mR = new WeakReference<LineView>(v);
	}

	public int getPoint() {
		return mPoint;
	}

	public void setPoint(int point) {
		mPoint = point;
	}

	private int mPoint = 0;
}