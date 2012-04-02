package info.kyorohiro.helloworld.logcat.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.widget.SimpleFilterableLineView;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;


public class LogcatViewer extends SimpleFilterableLineView {

	private MyCircleControllerEvent mCircleControllerAction = null;
	private MyTouchAndMove mTouchAndMove = null;
	

	public LogcatViewer() {
		super(new LogcatViewerBuffer(3000, 1000, 16));
		mCircleControllerAction = new MyCircleControllerEvent();
		mTouchAndMove = new MyTouchAndMove(this);
		this.addChild(mTouchAndMove);
	}

	public CircleControllerAction getCircleControllerAction() {
		return mCircleControllerAction;
	}

	private class MyCircleControllerEvent implements SimpleCircleController.CircleControllerAction {
		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				setPositionY(getPositionY() + rateDegree*2);
			}
		}

		public void upButton(int action) {
			setPositionY(getPositionY() + 5);
		}

		public void downButton(int action) {
			setPositionY(getPositionY() - 5);
		}
	}

	public static class LogcatViewerBuffer extends FlowingLineData {
		private Pattern mPatternForFontColorPerLine = 
			Pattern.compile("[\\t\\s0-9\\-:.,]*[\\t\\s]*([VDIWEFS]{1})/");

		public LogcatViewerBuffer(int listSize, int width, int textSize) {
			super(listSize, width, textSize);
		}

		public void setColorPerLine(CharSequence line) {
			try {
				Matcher m = mPatternForFontColorPerLine.matcher(line);
				if (m == null) {
					return;
				}
				if (m.find()) {
					if ("D".equals(m.group(1))) {
						setCurrentLineColor(Color.parseColor("#cc86c9f4"));
					} else if ("I".equals(m.group(1))) {
						setCurrentLineColor(Color.parseColor("#cc86f4c9"));
					} else if ("V".equals(m.group(1))) {
						setCurrentLineColor(Color.parseColor("#ccc9f486"));
					} else if ("W".equals(m.group(1))) {
						setCurrentLineColor(Color.parseColor("#ccffff00"));
					} else if ("E".equals(m.group(1))) {
						setCurrentLineColor(Color.parseColor("#ccff2222"));
					} else if ("F".equals(m.group(1))) {
						setCurrentLineColor(Color.parseColor("#ccff2222"));
					} else if ("S".equals(m.group(1))) {
						setCurrentLineColor(Color.parseColor("#ccff2222"));
					}
				}
			} catch (Throwable e) {

			}
		}
	}
}
