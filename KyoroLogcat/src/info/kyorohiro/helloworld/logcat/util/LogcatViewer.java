package info.kyorohiro.helloworld.logcat.util;


import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.SimpleFilterableLineView;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;


public class LogcatViewer extends SimpleFilterableLineView {

	private MyCircleControllerEvent mCircleControllerAction = null;
	private MyTouchAndMove mTouchAndMove = null;
	

	public LogcatViewer() {
		super(null);
		mCircleControllerAction = new MyCircleControllerEvent();
		mTouchAndMove = new MyTouchAndMove();
		this.addChild(mTouchAndMove);
	}

	public CircleControllerAction getCircleControllerAction() {
		return mCircleControllerAction;
	}

	private class MyTouchAndMove extends SimpleDisplayObject {
		
		private int prevY = 0;
		private int movingY = 0;
		@Override
		public void paint(SimpleGraphics graphics) {
		}
		@Override
		public boolean onTouchTest(int x, int y, int action) {
			if(prevY == -999) {
				prevY = y;
				return false;
			}
			if(action == MotionEvent.ACTION_MOVE) {
				movingY += y-prevY;
				prevY =  y;
				int textSize = LogcatViewer.this.getTextSize();
				if(movingY<textSize || textSize<movingY) {
					int notMuchValue = movingY%textSize;
					setPosition(getPosition() + (movingY-notMuchValue)/textSize);
					movingY = notMuchValue;
					return true;
				}
			}
			if(action == MotionEvent.ACTION_UP) {
				prevY = -999;
				movingY = 0;
			}
			return false;
		}
		
	}

	private class MyCircleControllerEvent implements SimpleCircleController.CircleControllerAction {
		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				setPosition(getPosition() + rateDegree*2);
			}
		}

		public void upButton(int action) {
			setPosition(getPosition() + 5);
		}

		public void downButton(int action) {
			setPosition(getPosition() - 5);
		}
	}

}
