package info.kyorohiro.helloworld.display.widget.lineview;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimplePoint;
import info.kyorohiro.helloworld.display.simple.SimpleStage;

public class TouchAndZoomForLineView extends SimpleDisplayObject {
	private LineView mLineViewer = null;
	private boolean mStartZoom = false;
	private float mStartScale = 0;
	private float mNextScale = 0;
	private int mStartCenterY = 0;
	private int mStartCenterX = 0;
	private int mStartPosY = 0;
	private int mStartPosX = 0;
	private int mStartGetX = 0;
	private LineViewData mStartLine = null;
	private int mStartLineBaseColor = 0;
	private float mStartLength = 0;
	private float mCurrentScale = 0;

	public TouchAndZoomForLineView(LineView viewer) {
		mLineViewer = viewer;
	}

	private int getCenterX() {
		SimpleStage stage = SimpleDisplayObject.getStage(this);
		SimplePoint[] p = stage.getMultiTouchEvent();
		int ret = (p[0].getX() + p[1].getX()) / 2;
		return ret;
	}

	private int getCenterY() {
		SimpleStage stage = SimpleDisplayObject.getStage(this);
		SimplePoint[] p = stage.getMultiTouchEvent();
		int ret = (p[0].getY() + p[1].getY()) / 2;
		return ret;
	}

	private int getLength() {
		SimpleStage stage = SimpleDisplayObject.getStage(this);
		SimplePoint[] p = stage.getMultiTouchEvent();
		int xx = p[0].getX() - p[1].getX();
		int yy = p[0].getY() - p[1].getY();
		return xx * xx + yy * yy;
	}

	private boolean doubleTouched() {
		SimpleStage stage = SimpleDisplayObject.getStage(this);
		SimplePoint[] p = stage.getMultiTouchEvent();
		if (p[0].isVisible() && p[1].isVisible()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public synchronized boolean onTouchTest(int x, int y, int action) {
		SimpleStage stage = SimpleDisplayObject.getStage(this);

		if (stage != null && stage.isSupportMultiTouch()) {
			if (!doubleTouched()) {
				mStartLength = 0;
				if (mStartZoom) {
					mStartZoom = false;
					if (mStartLine != null) {
						mStartLine.setColor(mStartLineBaseColor);
					}
					return true;
				} else {
					return false;
				}
			}
			if (mStartZoom == false) {
				mStartZoom = true;
				mStartScale = mNextScale = mLineViewer.getScale();
				mStartLength = getLength();
				mStartCenterX = getCenterX();
				mStartCenterY = getCenterY();
				mStartPosY = mLineViewer.getYToPosY(mStartCenterY);
				mStartPosX = mLineViewer.getXToPosX(mStartPosY, mStartCenterX, 0);
				mStartLine = mLineViewer.getLineViewData(mStartPosY);
				mStartGetX = mLineViewer.getPositionX();
				if (mStartLine != null) {
					mStartLineBaseColor = mStartLine.getColor();
					mStartLine.setColor(Color.YELLOW);
				}
			}

			int currentLength = getLength();// xx*xx + yy*yy;
			if (mStartLength != 0) {
				float nextScale = mNextScale += (currentLength-mStartLength)/(400 * 400);
				if (nextScale < 1.0) {
					nextScale = 1.0f;
				} else if (nextScale > 6) {
					nextScale = 6.0f;
				}
				mCurrentScale = nextScale;
			}
			mStartLength = currentLength;
			return true;
		}
		return false;
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		if (mStartZoom) {
			mLineViewer.setScale(mCurrentScale, mStartScale, mStartGetX,mStartPosX, mStartPosY, mStartCenterX, mStartCenterY);
		}
	}

}