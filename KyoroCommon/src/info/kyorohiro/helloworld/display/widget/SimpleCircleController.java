package info.kyorohiro.helloworld.display.widget;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class SimpleCircleController extends SimpleDisplayObjectContainer {

	private CircleControllerAction mEvent = new NullCircleControllerEvent();
	private int mMaxRadius = 100;
	private int mMinRadius = 50;

	public SimpleCircleController() {
		BG bg = new BG();
		this.addChild(bg);
	}

	public int getWidth(){
		return mMaxRadius;
	}

	public int getHeight(){
		return mMaxRadius;
	}

	public void setEventListener(CircleControllerAction event) {
		if (event == null) {
			mEvent = new NullCircleControllerEvent();
		} else {
			mEvent = event;
		}
	}

	@Override
	public boolean onKeyDown(int keycode) {
		switch(keycode){
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_VOLUME_UP:
			mEvent.upButton(CircleControllerAction.ACTION_PRESSED);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			mEvent.downButton(CircleControllerAction.ACTION_PRESSED);
			break;
		}
		return super.onKeyDown(keycode);
	}

	@Override
	public boolean onKeyUp(int keycode) {
		switch(keycode){
		case KeyEvent.KEYCODE_DPAD_UP:
		case KeyEvent.KEYCODE_VOLUME_UP:
			mEvent.upButton(CircleControllerAction.ACTION_RELEASED);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			mEvent.downButton(CircleControllerAction.ACTION_RELEASED);
			break;
		}
		return super.onKeyUp(keycode);
	}

	private class BG extends SimpleDisplayObject {
//		private int mMinSize = 70;
//		private int mSize = 90;
//		private int mMaxSize = 110;
		private boolean isTouched = false;
		private int mTouchX = 0;
		private int mTouchY = 0;

		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.setColor(Color.parseColor("#99ffff86"));
			graphics.setStyle(SimpleGraphics.STYLE_STROKE);
			graphics.setStrokeWidth(4);

			int interSpace = (mMaxRadius-mMinRadius)/10;
			int centerRadius = mMinRadius +(mMaxRadius-mMinRadius)/2;

			for (int i = 0; i < 10; i++) {
				graphics.drawCircle(0, 0, mMaxRadius - i * interSpace);
			}
			graphics.setStrokeWidth(6);
			graphics.setColor(Color.parseColor("#99ffff86"));
			if (isTouched) {
				graphics.setColor(Color.parseColor("#99ffc9f4"));
				double pi = 0;
				if (mTouchX != 0) {
					pi = Math.atan2(mTouchY, mTouchX);
				}
				int x = (int) (centerRadius * Math.cos(pi));
				int y = (int) (centerRadius * Math.sin(pi));
				graphics.drawCircle(x, y, centerRadius / 3);
				graphics.drawCircle(x, y, centerRadius / 4);
				graphics.drawCircle(x, y, centerRadius / 5);
			}

			graphics.drawCircle(0, 0, mMaxRadius);
			graphics.drawCircle(0, 0, mMinRadius);
			graphics.drawCircle(0, 0, mMinRadius);

			graphics.drawLine(mMinRadius, -10, centerRadius, 0);
			graphics.drawLine(mMaxRadius, -10, centerRadius, 0);
			graphics.drawLine(-mMinRadius, -10, -centerRadius, 0);
			graphics.drawLine(-mMaxRadius, -10, -centerRadius, 0);

		}

		@Override
		public boolean onTouchTest(int x, int y, int action) {
			super.onTouchTest(x, y, action);
			mTouchX = x;
			mTouchY = y;
			int size = x * x + y * y;
			int a = 0;
			boolean ret;
			if(!isTouched){
				mPrevDegree = -999;
			}

			if (mMinRadius * mMinRadius < size && size < mMaxRadius * mMaxRadius) {
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					isTouched = true;
					a = CircleControllerAction.ACTION_PRESSED;
					break;
				case MotionEvent.ACTION_UP:
					isTouched = false;
					a = CircleControllerAction.ACTION_RELEASED;
					break;
				case MotionEvent.ACTION_MOVE:
					if(isTouched){
					a = CircleControllerAction.ACTION_MOVE;
					}
					else {
						a = CircleControllerAction.ACTION_IN;
					}
					isTouched = true;
					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_OUTSIDE:
				default:
					isTouched = false;
					a = CircleControllerAction.ACTION_RELEASED;
					break;
				}
				ret = true;
				double p = Math.atan2(y, x);
				int curDegree = (int) Math.toDegrees(p);
				if (mPrevDegree == -999) {
					mPrevDegree = curDegree;
				}
				mEvent.moveCircle(a, (int) Math.toDegrees(p), rate(curDegree,mPrevDegree));
				mPrevDegree = curDegree;
			} else {
				isTouched = false;
				a = CircleControllerAction.ACTION_OUT;
				ret = false;
				if(!isTouched) {
					mEvent.moveCircle(a, mPrevDegree, 0);
				}
			}

			return ret;
		}

		private int rate(int pre, int cur){
			int rate = cur-pre;
			if((cur>90&&pre<-90)){
				rate = cur-(360-pre);
			}
			else if(pre>90&&cur<-90){
				rate = (360-cur)-pre;
			}
			if(rate < -180 || rate > 180){
				rate = 0;
			}
			return rate;
		}
		private int mPrevDegree = -999;
	}


	public static interface CircleControllerAction {
		public static int ACTION_PRESSED = 0;
		public static int ACTION_RELEASED = 2;
		public static int ACTION_IN = 4;
		public static int ACTION_OUT = 8;
		public static int ACTION_MOVE = 16;

		void upButton(int action);

		void downButton(int action);

		void moveCircle(int action, int degree, int rateDegree);
	}

	class NullCircleControllerEvent implements CircleControllerAction {
		public void upButton(int action) {
		}

		public void downButton(int action) {
		}

		public void moveCircle(int action, int degree, int rateDegree) {
		}
	}

}
