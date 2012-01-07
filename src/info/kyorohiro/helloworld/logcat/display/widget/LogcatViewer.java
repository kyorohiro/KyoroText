package info.kyorohiro.helloworld.logcat.display.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.graphics.Paint;
import info.kyorohiro.helloworld.logcat.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.logcat.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.logcat.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.logcat.display.widget.CircleController.CircleControllerEvent;
import info.kyorohiro.helloworld.logcat.util.CyclingStringList;

public class LogcatViewer extends SimpleDisplayObjectContainer {

	private CyclingStringList mCyclingString = null;
	private Viewer viewer = new Viewer();
	private int mPosition = 0;
	private int mTextSize = 14;
	private MyCircleControllerEvent mCircleControllerEvent = null;

	public LogcatViewer(int numOfStringList) {
		this.addChild(viewer);
		mCyclingString = new CyclingStringList(numOfStringList, 1000, mTextSize);
		mCircleControllerEvent = new MyCircleControllerEvent();
	}

	public CircleControllerEvent getCircleControllerEvent() {
		return mCircleControllerEvent;
	}

	public CyclingStringList getCyclingStringList() {
		return mCyclingString;
	}

	private class Viewer extends SimpleDisplayObject {
		private int mWidth = 0;
		private int mHeight = 0;
		private int mNumOfLine = 100;

		@Override
		public void paint(SimpleGraphics graphics) {
			updateStatus(graphics);
			drawBG(graphics);

			int numOfStackedString = mCyclingString.getMax();
			int referPoint = numOfStackedString-(mPosition+mNumOfLine);
			int start = referPoint;
			int end = start + mNumOfLine;
			if(start < 0) {
				start = 0;	
			}
			if(end < 0){
				end = 0;
			}
			if(end >= numOfStackedString){
				end = numOfStackedString;
			}
			String[] list = mCyclingString.getlines(start, end);

			int blank = 0;//mNumOfLine - list.length;
			boolean uppserSideBlankisViewed = (referPoint)<0;
			if(uppserSideBlankisViewed){
				blank = -1*referPoint;
			}

			for (int i = 0; i < list.length; i++) {				
				 setColorPerLine(graphics, list[i]);
				graphics.drawText(
						"[" + (start + i) + "]:  " + list[i],
						0,
						graphics.getTextSize()*(blank+i + 1));
			}
		}


		Pattern mPatternForFontColorPerLine = Pattern.compile(":[\\t\\s0-9\\-:.,]*[\\t\\s]([VDIWEFS]{1})/");
		//
		// todo “K“–‚·‚¬‚é
		private void setColorPerLine(SimpleGraphics graphics, String line){
			try{
				Matcher m = mPatternForFontColorPerLine.matcher(line);
				if(m == null){
					graphics.setColor(Color.parseColor("#ccc9f486"));
				}
				if(m.find()){
					if("D".equals(m.group(1))){
						graphics.setColor(Color.parseColor("#cc86c9f4"));
					}
					else if("I".equals(m.group(1))){
						graphics.setColor(Color.parseColor("#cc86f4c9"));						
					}
					else if("V".equals(m.group(1))){
						graphics.setColor(Color.parseColor("#ccc9f486"));					
					}
					else if("W".equals(m.group(1))){
						graphics.setColor(Color.parseColor("#ccffff00"));					
					}
					else if("E".equals(m.group(1))){
						graphics.setColor(Color.parseColor("#ccff2222"));					
					}
					else if("F".equals(m.group(1))){
						graphics.setColor(Color.parseColor("#ccff2222"));					
					}
					else if("S".equals(m.group(1))){
						graphics.setColor(Color.parseColor("#ccff2222"));					
					}
				}
			} catch (Throwable e){
				
			}
		}

		private void drawBG(SimpleGraphics graphics) {
			graphics.drawBackGround(Color.parseColor("#cc795514"));
			graphics.setColor(Color.parseColor("#ccc9f486"));
		}

		private void updateStatus(SimpleGraphics graphics) {
			mWidth = (int) graphics.getWidth();
			mHeight = (int) graphics.getHeight();
			graphics.setTextSize(LogcatViewer.this.mTextSize);
			mNumOfLine = mHeight / mTextSize;

			int blankSpace = mNumOfLine / 3;
			if (mPosition < -(mNumOfLine - blankSpace)) {
				mPosition = -(mNumOfLine - blankSpace);
			} else if (mPosition > (mCyclingString.getMax() - blankSpace)) {
				mPosition = mCyclingString.getMax() - blankSpace;
			}

			int margine = graphics.getTextWidth("[9999]:  ");
			LogcatViewer.this.mCyclingString.setWidth(mWidth - margine);
		}
	}

	private class MyCircleControllerEvent implements
			CircleController.CircleControllerEvent {
		public void upButton(int action) {
			if (action == CircleControllerEvent.ACTION_RELEASED) {
				mPosition++;
			}
		}

		public void downButton(int action) {
			if (action == CircleControllerEvent.ACTION_RELEASED) {
				mPosition--;
			}
		}

		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerEvent.ACTION_MOVE) {
				mPosition += rateDegree*2;
			}

		}

	}

}
