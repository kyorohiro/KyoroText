package info.kyorohiro.helloworld.display.widget.lineview;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.LineViewBufferSpec;

public class CursorableLineView extends LineView {

	private int cursorRow = 0;
	private int cursorCol = 0;
    private MyCursor mRight = new MyCursor();
	public CursorableLineView(LineViewBufferSpec<LineViewData> inputtedText,
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
	}


	public static class MyCursor extends SimpleDisplayObject {
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
	}
}
