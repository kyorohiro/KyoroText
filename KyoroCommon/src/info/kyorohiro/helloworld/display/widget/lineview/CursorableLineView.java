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
	private MyCursor mLeft = new MyCursor();
	private MyCursor mRight = new MyCursor();
	private CharSequence mMode = MODE_VIEW;

	public final static String MODE_SELECT = "MODE SELECT";
	public final static String MODE_VIEW = "MODE VIEW";
	public final static String MODE_EDIT = "MODE EDIT: NOW CREATING!!";
	private int mTestTextColor = Color.parseColor("#66FFFF00");

	public void setMode(String mode) {
		mMode = mode;
		if (MODE_SELECT.equals(mode)) {
			mLeft.enable(true);
			mRight.enable(true);
			int col = getShowingTextStartPosition();
			mLeft.setCursorCol(col);
			mLeft.setCursorRow(0);
			mRight.setCursorCol(col);
			mRight.setCursorRow(3);
			setScale(1.0f);
		}
		if (MODE_VIEW.equals(mode)) {
			mLeft.enable(false);
			mRight.enable(false);
		}
		if (MODE_EDIT.equals(mode)) {
			mLeft.enable(false);
			mRight.enable(true);
		}
	}
	@Override
	public void setScale(float scale) {
		if(mMode.equals(MODE_VIEW)){
			super.setScale(scale);
		} else {
			super.setScale(1.0f);
		}
	}
	public BreakText getBreakText() {
		return getLineViewBuffer().getBreakText();
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (super.onTouchTest(x, y, action)) {
			return true;
		} else {
			if (mMode == MODE_EDIT && action == MotionEvent.ACTION_UP) {
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
		addChild(mLeft);
		mRight.setRect(20, 120);
		mLeft.setRect(20, 120);
		mLeft.setPoint(100, 100);
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		super.paint(graphics);
		if (null == getBreakText()) {
			return;
		}

		updateCursor(graphics, mRight);
		updateCursor(graphics, mLeft);
		// String a = "posY=" + this.getPositionY();
		// a += ",sPos=" + this.getShowingTextStartPosition();
		// a += ",ePos=" + this.getShowingTextEndPosition();
		// a += ",blink=" + this.getBlinkY();
		// graphics.drawText("" + a, 10, 500);
		graphics.setColor(mTestTextColor);
		graphics.setTextSize(30);
		graphics.drawText(mMode, 20, this.getHeight() - 50);
		drawBGForSelect(graphics);
	}

	private void drawBGForSelect(SimpleGraphics graphics) {
		if (mLeft.enable() && mRight.enable()) {
			MyCursor b = mLeft;
			MyCursor e = mRight;
			int textSize = (int)(getTextSize()*getScale());
			if (b.getY()>e.getY()|| (b.getY() == e.getY() && b.getX()> e.getX())) {
				b = mRight;
				e = mLeft;
			}
			
			graphics.setColor(mTestTextColor);
			graphics.setStrokeWidth(10);
			if(b.getY() != e.getY()){
				graphics.drawLine(b.getX(), b.getY(), (int)(getWidth()*0.95), b.getY());
				graphics.drawLine(this.getXForShowLine(0, 0), e.getY(), e.getX(), e.getY());
				graphics.startPath();
				graphics.moveTo((int)(getWidth()*0.05), b.getY()+textSize);//+getTextSize());
				graphics.lineTo((int)(getWidth()*0.95), b.getY());
				graphics.lineTo((int)(getWidth()*0.95), e.getY()-textSize);
				graphics.lineTo((int)(getWidth()*0.05), e.getY());//+getTextSize());
				graphics.moveTo((int)(getWidth()*0.05), b.getY());//+getTextSize());
				graphics.endPath();
			} else {
				graphics.drawLine(b.getX(), b.getY(), e.getX(), e.getY());
			}
		}
	}

	private void updateCursor(SimpleGraphics graphics, MyCursor cursor) {
		{
			int y = 0;
			float x = 0.0f;
			x = getXForShowLine(0, 0);
			int l = 0;
			try {
				LineViewData d = getLineViewData(cursor.getCursorCol());
				graphics.setColor(Color.YELLOW);
				if (d != null) {
					float[] widths = new float[1000];
					l = getBreakText().getTextWidths(d, 0, d.length(), widths);
					// mPaint.
					// for (int i = d.length()-1; i >=
					// (d.length()-mRight.getCursorRow()); i--) {
					for (int i = 0; i <= cursor.getCursorRow(); i++) {
						x += widths[i];
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}

			y = getYForShowLine((int)(getTextSize()*getScale()), cursor.getCursorRow(),
					cursor.getCursorCol() - getShowingTextStartPosition());
			// android.util.Log.v("mkj","xxx="+x+","+y+","+l+","+cursorRow);
			cursor.setPoint((int) x, y);
		}
	}

	public class MyCursor extends SimpleDisplayObject {
		private int cursorRow = 0;
		private int cursorCol = 0;
		private int mX = 0;
		private int mY = 0;
		private int px = 0;
		private int py = 0;
		private boolean focus = false;
		private boolean mEnable = false;

		public boolean enable() {
			return mEnable;
		}

		public void enable(boolean enable) {
			mEnable = enable;
		}

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
			if (!mEnable) {
				return;
			}
			// setPoint(100, 100);
			if (!focus) {
				graphics.setColor(Color.parseColor("#66FFFF00"));
			} else {
				graphics.setColor(Color.parseColor("#3300FFFF"));
				drawCursor(graphics, mX - px, mY - py);
				graphics.setColor(Color.parseColor("#6600FFFF"));
			}
			drawCursor(graphics, 0, 0);
			// LineViewData d =
			// CursorableLineView.this.getLineViewData(cursorCol);
			// String a = "";
			// if (d != null) {
			// a += d.toString();
			// }
			// graphics.drawText(
			// "mo,x=" + cursorRow + ",y=" + cursorCol + "," + a, 100, 100);
			// android.util.Log.v("kiyo", "zzz=" + "mo,x=" + cursorRow + ",y="
			// + cursorCol + "," + a);
		}

		private void drawCursor(SimpleGraphics graphics, int x, int y) {
			graphics.startPath();
			x*=getScale();
			y*=getScale();
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
			if (!mEnable) {
				return false;
			}
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
				if (focus == true) {
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
		int n = (int) (y / (getTextSize() * 1.2*getScale())) + 1;
		int yy = n - super.getBlinkY() - 1;
		return yy + getShowingTextStartPosition();
	}

	@Deprecated
	public int getXToPosX(int cursorCol, int x, int cur) {
		LineViewBufferSpec mInputtedText = super.getLineViewBuffer();
		if (mInputtedText == null || null == mInputtedText.getBreakText()) {
			return cur;
		} else if (cursorCol >= mInputtedText.getNumberOfStockedElement()|| cursorCol < 0) {
			return cur;
		}

		LineViewData data = mInputtedText.get(cursorCol);
		if (data == null) {return cur;}
		int ret = mInputtedText.getBreakText().breakText(data, 0,
				data.length(), (int)(x/getScale()-getXForShowLine(0, cursorCol)));

		return ret;
	}

}
