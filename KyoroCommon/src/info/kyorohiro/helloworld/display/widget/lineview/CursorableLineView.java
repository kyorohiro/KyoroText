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
	private MyCursor mRight = new MyCursor();

	public BreakText getBreakText() {
		return getLineViewBuffer().getBreakText();
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (super.onTouchTest(x, y, action)) {
			return true;
		} else {
			if (action == MotionEvent.ACTION_DOWN) {
			} else if (action == MotionEvent.ACTION_UP) {
				mRight.setCursorCol(getYToPosY(y));
				mRight.setCursorRow(getXToPosX(mRight.getCursorCol(), x,
						mRight.getCursorRow()));
			}
			return false;
		}
	}

	public CursorableLineView(LineViewBufferSpec inputtedText, int textSize,
			int cashSize) {
		super(inputtedText, textSize, cashSize);
		addChild(mRight);
		mRight.setRect(20, 120);
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		super.paint(graphics);
		if (null == getBreakText()) {
			return;
		}
		int y = 0;
		float x = 0.0f;

		String a = "posY=" + this.getPositionY();
		a += ",sPos=" + this.getShowingTextStartPosition();
		a += ",ePos=" + this.getShowingTextEndPosition();
		a += ",blink=" + this.getBlinkY();
		// graphics.drawText("" + a, 10, 500);
		// getBreakText().setTextSize(getTextSize());

		x = getXForShowLine(0, 0);
		int l = 0;
		try {
			LineViewData d = getLineViewData(mRight.getCursorCol());
			graphics.setColor(Color.YELLOW);
			graphics.drawText("" + a + "," + d.toString(), 10, 500);
			if (d != null) {
				float[] widths = new float[1000];
				l = getBreakText().getTextWidths(
						d, 0, d.length(), widths);
				// mPaint.
//				for (int i = d.length()-1; i >= (d.length()-mRight.getCursorRow()); i--) {
				for (int i = 0; i <=mRight.getCursorRow(); i++) {
					x += widths[i];
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}

		y = getYForShowLine(getTextSize(), mRight.getCursorRow(),
				mRight.getCursorCol() - getShowingTextStartPosition());
		// android.util.Log.v("mkj","xxx="+x+","+y+","+l+","+cursorRow);
		mRight.setPoint((int) x, y);
	}

	public class MyCursor extends SimpleDisplayObject {
		private int cursorRow = 0;
		private int cursorCol = 0;
		private int mX = 0;
		private int mY = 0;
		private int px = 0;
		private int py = 0;
		private boolean focus = false;

		public void setCursorCol(int col) {
			cursorCol = col;
		}

		public void setCursorRow(int row) {
			cursorRow = row;
		}

		public int getCursorRow() {
			return cursorRow;
		}

		public int getCursorCol() {
			return cursorCol;
		}

		@Override
		public void paint(SimpleGraphics graphics) {
			// setPoint(100, 100);
			if (!focus) {
				graphics.setColor(Color.parseColor("#66FFFF00"));
			} else {
				graphics.setColor(Color.parseColor("#3300FFFF"));
				drawCursor(graphics, mX - px, mY - py);
				graphics.setColor(Color.parseColor("#6600FFFF"));
			}
			drawCursor(graphics, 0, 0);
			LineViewData d = CursorableLineView.this.getLineViewData(cursorCol);
			String a = "";
			if (d != null) {
				a += d.toString();
			}
			graphics.drawText(
					"mo,x=" + cursorRow + ",y=" + cursorCol + "," + a, 100, 100);
			android.util.Log.v("kiyo", "zzz=" + "mo,x=" + cursorRow + ",y="
					+ cursorCol + "," + a);
		}

		private void drawCursor(SimpleGraphics graphics, int x, int y) {
			graphics.startPath();
			graphics.moveTo(x, y);
			graphics.lineTo(x + getWidth() / 2, y + getHeight() * 2 / 3);
			graphics.lineTo(x + getWidth() / 2, y + getHeight());
			graphics.lineTo(x + -getWidth() / 2, y + getHeight());
			graphics.lineTo(x + -getWidth() / 2, y + getHeight() * 2 / 3);
			graphics.lineTo(x + 0, y + 0);
			graphics.endPath();
		}

		@Override
		public boolean onTouchTest(int x, int y, int action) {
			mX = x;
			mY = y;
			if (action == MotionEvent.ACTION_DOWN) {
				if (-getWidth() < x && x < getWidth() && 0 < y
						&& y < getHeight()) {
					focus = true;
					px = x;
					py = y;
				} else {
					focus = false;
				}
			}

			if (action == MotionEvent.ACTION_UP) {
				if(focus == true){
					focus = false;
					return true;
				}
			} else if (action == MotionEvent.ACTION_MOVE) {
				if (focus == true) {
					cursorCol = getYToPosY(y - py + getY());
					cursorRow = getXToPosX(cursorCol, x - px + getX(),
							cursorRow);
					android.util.Log.v("kiyo", "ggg=" + cursorRow + ","
							+ cursorCol + "," + x + "," + px + "," + getX());

				}
			}
			return focus;// super.onTouchTest(x, y, action);
		}

		@Override
		public boolean includeParentRect() {
			return false;
		}
	}

	public int getYToPosY(int y) {
		int n = (int) (y / (getTextSize() * 1.2))+1;
		int yy = n - super.getBlinkY()-1;
		return yy + getShowingTextStartPosition();
	}

	@Deprecated
	public int getXToPosX(int cursorCol, int x, int cur) {
		LineViewBufferSpec mInputtedText = super.getLineViewBuffer();
		if (mInputtedText == null || null == mInputtedText.getBreakText()) {
			return cur;
		} else if (cursorCol >= mInputtedText.getNumberOfStockedElement()
				|| cursorCol < 0) {
			return cur;
		}

		LineViewData data = mInputtedText.get(cursorCol);
		if (data == null) {
			return cur;
		}
		int ret = mInputtedText.getBreakText().breakText(data, 0,
				data.length(), x - getXForShowLine(0, cursorCol));

		return ret;
	}

}
