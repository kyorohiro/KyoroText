package info.kyorohiro.helloworld.display.widget.lineview;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBuilder;
import info.kyorohiro.helloworld.util.LineViewBufferSpec;

public class CursorableLineView extends LineView {

	private int cursorRow = 0;
	private int cursorCol = 0;
    private MyCursor mRight = new MyCursor();

    public BreakText getBreakText(){
		return getLineViewBuffer().getBreakText();
	}

	public CursorableLineView(LineViewBufferSpec inputtedText,
			int textSize, int cashSize) {
		super(inputtedText, textSize, cashSize);
		addChild(mRight);
		mRight.setRect(50, 100);
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		super.paint(graphics);
		String a = "posY="+this.getPositionY();
		a += ",sPos=" + this.getShowingTextStartPosition();
		a += ",ePos="+this.getShowingTextEndPosition();
		a += ",blink="+this.getBlinkY();
		graphics.drawText(""+a, 10, 500);
		getBreakText().setTextSize(getTextSize());
		
		
		float x = getXForShowLine(0, 0);
		int l = 0;
		try {
			LineViewData d = getLineViewData(cursorCol);
			if(d!=null){
				float[] widths = new float[1000];
				l = getBreakText().getTextWidths(d, 0, cursorRow, widths);
			//	mPaint.
				for(int i=0;i<l;i++){
				//if(0<l){
					x += widths[l-1];
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		int y = getYForShowLine(getTextSize(), cursorRow, cursorCol);
		android.util.Log.v("mkj",""+x+","+y+","+l+","+cursorRow);
		mRight.setPoint((int)x, y);
	}


	public class MyCursor extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			setPoint(100, 100);
			graphics.setColor(Color.parseColor("#66FFFF00"));
			graphics.startPath();
			graphics.moveTo(0, 0);
			graphics.lineTo(getWidth()/2, getHeight()*2/3);
			graphics.lineTo(getWidth()/2, getHeight());
			graphics.lineTo(-getWidth()/2, getHeight());
			graphics.lineTo(-getWidth()/2, getHeight()*2/3);
			graphics.lineTo(0, 0);			
			graphics.endPath();
			graphics.drawText("goooo", 100, 100);
		}

		private int x = 0;
		private int y = 0;
		private int px = 0;
		private int py = 0;		
		private boolean focus = false;
		@Override
		public boolean onTouchTest(int x, int y, int action) {
			if(action ==MotionEvent.ACTION_DOWN) {
				if(-getWidth()/2<x&&x<getWidth()/2&&
				0<y&&y<getHeight()){
					focus = true;
					px = x;
					py = y;
				}else {
					focus = false;
				}
			}

			if(action == MotionEvent.ACTION_UP){
				focus = false;				
			}
			else if(action == MotionEvent.ACTION_MOVE){
				if(focus == true){
					cursorCol = getYToPosY(y-py+getY());
					//cursorRow = getXToPosX(mPaint,cursorCol,x-px+getX(),cursorRow);
				}
			}
			return focus;//super.onTouchTest(x, y, action);
		}
	}

}
