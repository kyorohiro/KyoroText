package info.kyorohiro.helloworld.logcat.util;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import android.view.MotionEvent;

public class MyTouchAndMove extends SimpleDisplayObject {

	private int mPrevX = 0;
	private int mPrevY = 0;
	private long mPrevTime = 0;
	private int mMovingX = 0;
	private int mMovingY = 0;
	private int mHeavyX = 0;
	private int mHeavyY = 0;
	private int mPowerX = 0;
	private int mPowerY = 0;
	private int mPower_prevX = 0;
	private int mPower_prevY = 0;
	private long mPower_time = 0;
	private LineView mViewer = null;

	public MyTouchAndMove(LogcatViewer viewer) {
		mViewer = viewer.getLineView();
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		if (mHeavyX < -5 || mHeavyX > 5) {
			mHeavyX /= 1.2;
			// todo refactaring
			mViewer.setPositionX(mViewer.getPositionX() + mHeavyX/* /textSize */);
//			mViewer.addPositionY(mViewer.getPositionX() + mHeavyX/* /textSize */);
		}
		if (mHeavyY < -5 || mHeavyY > 5) {
			mHeavyY /= 1.2;
			int textSize = (int) (mViewer.getTextSize() * mViewer.getScale());// todo
																				// 2.5f
			// todo refactaring
			// android.util.Log.v("kiyohiro","r="+(mViewer.getPositionY() +"+"+
			// mHeavyY/textSize));
			mViewer.setPositionY(mViewer.getPositionY() + mHeavyY / textSize);
//			mViewer.addPositionY(mViewer.getPositionY() + mHeavyY / textSize);

		}
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (mPrevY == -999 || mPrevX == -999) {
			mPrevY = y;
			mPrevX = x;
			mPrevTime = System.currentTimeMillis();
			return false;
		}

		if (action == MotionEvent.ACTION_MOVE) {
			mHeavyX = 0;
			mHeavyY = 0;

			mMovingY += y - mPrevY;
			mPrevY = y;
			mMovingX += x - mPrevX;
			mPrevX = x;
			mPrevTime = System.currentTimeMillis();
			updateMovePower(x, y);
			int textSize = (int) (mViewer.getTextSize() * mViewer.getScale());// todo
																				// 2.5f
			boolean ret = false;
			if (mMovingY < textSize || textSize < mMovingY) {
				int notMuchValue = mMovingY % textSize;
				mViewer.setPositionY(mViewer.getPositionY()
						+ (mMovingY - notMuchValue) / textSize);
				mMovingY = notMuchValue;
				ret = true;
			}

			if (mMovingX < textSize || textSize < mMovingX) {
				int notMuchValue = mMovingY % textSize;
				mViewer.setPositionX(mViewer.getPositionX()
						+ (mMovingX - notMuchValue));
				mMovingX = notMuchValue;
				ret = true;
			}
			return ret;
		} else if (action == MotionEvent.ACTION_DOWN) {
			mHeavyX = 0;
			mHeavyY = 0;
			mMovingY = 0;
			mMovingX = 0;
			mPrevTime = 0;
			mPrevY = -999;
			mPrevX = -999;
		} else if (action == MotionEvent.ACTION_UP) {
			//android.util.Log.v("kiyohiro", "power=" + mPower_prevY);
			mHeavyX = mPowerX * 8;
			mHeavyY = mPowerY * 8;
			mPrevY = -999;
			mPrevX = -999;
			mMovingY = 0;
			mMovingX = 0;
			mPrevTime = 0;
		}

		return false;
	}

	public void updateMovePower(int x, int y) {
		if (mPower_time < 0) {
			mPower_time = System.currentTimeMillis();
			mPower_prevY = y;
			mPower_prevX = x;
		} else {
			mPowerY = y - mPower_prevY;
			mPower_prevY = y;
			mPowerX = x - mPower_prevX;
			mPower_prevX = x;
		}
	}

}