package info.kyorohiro.helloworld.logcat.logcat;

import java.util.regex.Pattern;
import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.CyclingFlowingLineData;
import info.kyorohiro.helloworld.display.widget.FlowingLineData;
import info.kyorohiro.helloworld.display.widget.FlowingLineViewer;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class LogcatViewer extends FlowingLineViewer {

	private MyCircleControllerEvent mCircleControllerAction = null;

	public LogcatViewer(int numOfStringList) {
		super(numOfStringList);
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
			setPosition(getPosition() + 1);
		}

		public void downButton(int action) {
			setPosition(getPosition() + 1);
		}
	}

}
