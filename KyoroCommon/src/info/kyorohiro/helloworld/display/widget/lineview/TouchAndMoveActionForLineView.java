package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import android.view.MotionEvent;

public class TouchAndMoveActionForLineView extends SimpleDisplayObject {

	private int mPrevX = 0;
	private int mPrevY = 0;
	private int mMovingX = 0;
	private double mMovingY = 0;
	private int mHeavyX = 0;
	private int mHeavyY = 0;
	private int mPowerX = 0;
	private int mPowerY = 0;
	private int mPower_prevX = 0;
	private int mPower_prevY = 0;
	private long mPower_time = 0;
	private LineView mViewer = null;

	public TouchAndMoveActionForLineView(LineView viewer) {
		mViewer = viewer;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		if (mHeavyX < -5 || mHeavyX > 5) {
			mHeavyX /= 1.2;
			// todo refactaring
			mViewer.setPositionX(mViewer.getPositionX() + mHeavyX/* /textSize */);
		}
		if (mHeavyY < -5 || mHeavyY > 5) {
			mHeavyY /= 1.2;
			int textSize = (int) (mViewer.getTextSize() * mViewer.getScale());// todo																				// 2.5f
			// todo refactaring
			mViewer.setPositionY(mViewer.getPositionY()+mHeavyY/textSize);
			//mViewer.setTodoExtra((mViewer.getTodoExtra()+mHeavyY/textSize)%textSize);
		}
	}

	private double mMoveYY =0;
	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (mPrevY == -999 || mPrevX == -999) {
			mPrevY = y;
			mPrevX = x;
			return false;
		}

		if (action == MotionEvent.ACTION_MOVE) {
			mHeavyX = 0;
			mHeavyY = 0;
			mMovingY += y - mPrevY;
			mMoveYY  += y - mPrevY;
			mMovingX += x - mPrevX;
			int todoY = y - mPrevY;
			mPrevY = y;
			mPrevX = x;

			updateMovePower(x, y);
			int textSize = (int) (mViewer.getTextSize() * mViewer.getScale());// todo
																				// 2.5f
			boolean ret = false;
			{
				double tS = textSize*1.2;
				if((mMovingY/tS)>=1 || (mMovingY/tS)<=-1) {
					mViewer.setPositionY(mViewer.getPositionY()+ (int)(mMovingY/tS));
					mMovingY = mMovingY%(int)tS;
				} else {
					mMoveYY =(mMoveYY+todoY)%tS;
				}
			}

			if (mMovingX < textSize || textSize < mMovingX) {
				int notMuchValue = mMovingX % textSize;
				mViewer.setPositionX(mViewer.getPositionX()
						+ (mMovingX - notMuchValue));
				mMovingX = notMuchValue;
				ret = true;
			}
			return ret;
		} else if (action == MotionEvent.ACTION_DOWN) {
			mHeavyX = 0;
			mHeavyY = 0;
			mMoveYY = mMovingY; 
			mMovingX = 0;
			mPrevY = -999;
			mPrevX = -999;
		} else if (action == MotionEvent.ACTION_UP) {
			mHeavyX = mPowerX * 8;
			mHeavyY = mPowerY * 8;
			mPrevY = -999;
			mPrevX = -999;
			mMovingY = 0;
			mMovingX = 0;
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