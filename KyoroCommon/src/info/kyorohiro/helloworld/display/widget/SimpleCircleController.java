package info.kyorohiro.helloworld.display.widget;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class SimpleCircleController extends SimpleDisplayObjectContainer {

	private CircleControllerAction mEvent = new NullCircleControllerEvent();

	public SimpleCircleController() {
		BG bg = new BG();
		UP up = new UP();
		up.setPoint(-50, -50);
		DOWM down = new DOWM();
		down.setPoint(-50, 10);
		this.addChild(bg);
		this.addChild(up);
		this.addChild(down);
	}

	public int getWidth(){
		return 100;
	}

	public int getHeight(){
		return 100;
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
		private int mMinSize = 70;
		private int mSize = 90;
		private int mMaxSize = 110;
		private boolean isTouched = false;
		private int mTouchX = 0;
		private int mTouchY = 0;

		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.setColor(Color.parseColor("#99ffff86"));
			graphics.setStyle(SimpleGraphics.STYLE_STROKE);
			graphics.setStrokeWidth(4);
			for (int i = 0; i < 10; i++) {
				graphics.drawCircle(0, 0, 100 - i * 3);
			}
			graphics.setStrokeWidth(6);
			graphics.setColor(Color.parseColor("#99ffff86"));
			graphics.drawLine(-100, 0, +100, 0);
			graphics.drawCircle(0, 0, 100);
			graphics.drawCircle(0, 0, 80);

			if (isTouched) {
				graphics.setColor(Color.parseColor("#99ffc9f4"));
				double pi = 0;
				if (mTouchX != 0) {
					pi = Math.atan2(mTouchY, mTouchX);
				}

				int x = (int) (mSize * Math.cos(pi));
				int y = (int) (mSize * Math.sin(pi));
				graphics.drawCircle(x, y, mSize / 3);
				graphics.drawCircle(x, y, mSize / 4);
				graphics.drawCircle(x, y, mSize / 5);
			}
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

			if (mMinSize * mMinSize < size && size < mMaxSize * mMaxSize) {
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

	private class UP extends SimpleDisplayObject {
		private int mCenterX = 50;
		private int mCenterY = 20;
		private int mSize = 20;
		private boolean isTouched = false;

		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.setStrokeWidth(4);
			if (!isTouched) {
				graphics.setColor(Color.parseColor("#99c9f4ff"));
			} else {
				graphics.setColor(Color.parseColor("#99ffc9f4"));
			}
			graphics.moveTo(50, 0);
			graphics.lineTo(0, 40);
			graphics.lineTo(100, 40);
			graphics.lineTo(50, 0);
		}

		@Override
		public boolean onTouchTest(int x, int y, int action) {
			super.onTouchTest(x, y, action);

			int leftX = mCenterX - mSize;
			int leftY = mCenterY - mSize;
			int rightX = mCenterX + mSize;
			int rightY = mCenterY + mSize;

			if (leftX <= x && x <= rightX && leftY <= y && y <= rightY) {
				isTouched = notifyButtonEventAndReturnNextTouchedState(action,
						true, isTouched, upAction);
				return true;
			} else {
				isTouched = notifyButtonEventAndReturnNextTouchedState(action,
						false, isTouched, upAction);
				return false;
			}
		}

	}

	private class DOWM extends SimpleDisplayObject {
		private int mCenterX = 50;
		private int mCenterY = 20;
		private int mSize = 20;
		private boolean isTouched = false;

		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.setStrokeWidth(4);
			if (!isTouched) {
				graphics.setColor(Color.parseColor("#99c9f4ff"));
			} else {
				graphics.setColor(Color.parseColor("#99ffc9f4"));
			}
			graphics.moveTo(50, 40);
			graphics.lineTo(0, 0);
			graphics.lineTo(100, 0);
			graphics.lineTo(50, 40);
		}

		@Override
		public boolean onTouchTest(int x, int y, int action) {
			super.onTouchTest(x, y, action);
			int leftX = mCenterX - mSize;
			int leftY = mCenterY - mSize;
			int rightX = mCenterX + mSize;
			int rightY = mCenterY + mSize;

			if (leftX <= x && x <= rightX && leftY <= y && y <= rightY) {
				isTouched = notifyButtonEventAndReturnNextTouchedState(action,
						true, isTouched, downAction);
				return true;
			} else {
				isTouched = notifyButtonEventAndReturnNextTouchedState(action,
						false, isTouched, downAction);
				return false;
			}
		}
	}

	private boolean notifyButtonEventAndReturnNextTouchedState(int action,
			boolean isInside, boolean prevTouched, Action run) {
		if (isInside) {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (!prevTouched) {
					run.a(CircleControllerAction.ACTION_PRESSED);
				}
				return true;
			case MotionEvent.ACTION_UP:
				if (prevTouched) {
					run.a(CircleControllerAction.ACTION_RELEASED);
				}
				return false;
			case MotionEvent.ACTION_MOVE:
				if (prevTouched) {
					run.a(CircleControllerAction.ACTION_IN);
				} else {
					run.a(CircleControllerAction.ACTION_MOVE);
				}
				return true;
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
			default:
				if (prevTouched) {
					run.a(CircleControllerAction.ACTION_RELEASED);
				}
				return false;
			}
		} else {
			switch (action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_CANCEL:
			default:
				if (prevTouched) {
					run.a(CircleControllerAction.ACTION_OUT);
				}
				return false;
			}
		}
	}

	private interface Action {
		public void a(int action);
	}

	private Action downAction = new Action() {
		public void a(int action) {
			mEvent.downButton(action);
		}
	};
	private Action upAction = new Action() {
		public void a(int action) {
			mEvent.upButton(action);
		}
	};

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
