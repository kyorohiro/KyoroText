package info.kyorohiro.helloworld.logcat.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndMove;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndZoom;


public class LogcatViewer extends SimpleFilterableLineView {

	private MyCircleControllerEvent mCircleControllerAction = null;

	public static int COLOR_BG = Color.parseColor("#FF101030");	
	public static int COLOR_D = Color.parseColor("#ff80c9f4");
 	public static int COLOR_I = Color.parseColor("#ff80f4c9");
    public static int COLOR_V = Color.parseColor("#ffc9f480");
    public static int COLOR_W = Color.parseColor("#fff4f480");
    public static int COLOR_E = Color.parseColor("#fff48080");
    public static int COLOR_F = Color.parseColor("#ffff8080");
    public static int COLOR_S = Color.parseColor("#ffff8080");
	
	public LogcatViewer(int baseWidth) {
		super(new LogcatViewerBuffer(5000, 1000, 16), baseWidth);
		mCircleControllerAction = new MyCircleControllerEvent();
		getLineView().setBgColor(Color.parseColor("#FF101030"));
		addChild(new MyTouchAndMove(this.getLineView()));
		addChild(new MyTouchAndZoom(this.getLineView()));		

	}

	@Override
	protected void onAddViewer() {
	}

	public CircleControllerAction getCircleControllerAction() {
		return mCircleControllerAction;
	}

	private class MyCircleControllerEvent implements SimpleCircleController.CircleControllerAction {
		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				getLineView().setPositionY(getLineView().getPositionY() + rateDegree*2);
			}
		}

		public void upButton(int action) {
			getLineView().setPositionY(getLineView().getPositionY() + 5);
		}

		public void downButton(int action) {
			getLineView().setPositionY(getLineView().getPositionY() - 5);
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
					String mode = m.group(1);
					if ("D".equals(mode)) {
						setCurrentLineColor(COLOR_D);
					} else if ("I".equals(mode)) {
						setCurrentLineColor(COLOR_I);
					} else if ("V".equals(mode)) {
						setCurrentLineColor(COLOR_V);
					} else if ("W".equals(mode)) {
						setCurrentLineColor(COLOR_W);
					} else if ("E".equals(mode)) {
						setCurrentLineColor(COLOR_E);
					} else if ("F".equals(mode)) {
						setCurrentLineColor(COLOR_F);
					} else if ("S".equals(mode)) {
						setCurrentLineColor(COLOR_S);
					}
				}
			} catch (Throwable e) {

			}
		}
	}
}
