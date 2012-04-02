package info.kyorohiro.helloworld.logcat.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
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
		mTouchAndMove = new MyTouchAndMove();
		this.addChild(mTouchAndMove);
	}

	public CircleControllerAction getCircleControllerAction() {
		return mCircleControllerAction;
	}

	private class MyTouchAndMove extends SimpleDisplayObject {
		
		private int mPrevY = 0;
		private long mPrevTime = 0;
		private int mMovingY = 0;
		private int mHeavy = 0;
		private int mPower = 0;
		private int mPower_y = 0;
		private long mPower_time = 0;

		@Override
		public void paint(SimpleGraphics graphics) {
			if(mHeavy<-5||mHeavy>5) {
				mHeavy /=1.2;
				int textSize = LogcatViewer.this.getTextSize();
				setPosition(getPosition() + mHeavy/textSize);
			}
		}

		@Override
		public boolean onTouchTest(int x, int y, int action) {
			if(mPrevY == -999) {
				mPrevY = y;
				mPrevTime = System.currentTimeMillis();
				return false;
			}

			if(action == MotionEvent.ACTION_MOVE) {
				mHeavy = 0;
				mMovingY += y-mPrevY;
				mPrevY =  y;
				mPrevTime = System.currentTimeMillis();
				updateMovePower(y);
				int textSize = LogcatViewer.this.getTextSize();
				if(mMovingY<textSize || textSize<mMovingY) {
					int notMuchValue = mMovingY%textSize;
					setPosition(getPosition() + (mMovingY-notMuchValue)/textSize);
					mMovingY = notMuchValue;
					return true;
				}
			}
			else if(action == MotionEvent.ACTION_DOWN) {
				mHeavy = 0;
				mMovingY = 0;
				mPrevTime = 0;
			}
			else if(action == MotionEvent.ACTION_UP) {
				mHeavy = mPower*8;
				mPrevY = -999;
				mMovingY = 0;
				mPrevTime = 0;
			}
			return false;
		}

		public void updateMovePower(int y) {
			if(mPower_time <0) {
				mPower_time = System.currentTimeMillis();
				mPower_y = y;
			} else {
				int tmp = (int)(System.currentTimeMillis()-mPower_time);
				if(tmp<100) {
					return;
				}
				mPower_time = System.currentTimeMillis();
				mPower_time = y;
				mPower = y -mPower_y;
				mPower_y = y;
			}
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
