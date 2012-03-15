package info.kyorohiro.helloworld.logcat.util;


import info.kyorohiro.helloworld.display.widget.SimpleFilterableLineView;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;


public class LogcatViewer extends SimpleFilterableLineView {

	private MyCircleControllerEvent mCircleControllerAction = null;

	public LogcatViewer() {
		super(null);
		mCircleControllerAction = new MyCircleControllerEvent();
	}

	public CircleControllerAction getCircleControllerAction() {
		return mCircleControllerAction;
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
