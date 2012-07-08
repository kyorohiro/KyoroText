package info.kyorohiro.helloworld.display.widget.lineview;

import android.graphics.Color;
import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.util.LineViewBufferSpec;

public class CursorableLineView extends LineView {
	private MyCursor mLeft = new MyCursor();
	private MyCursor mRight = new MyCursor();
	private CharSequence mMode = MODE_VIEW;

	public final static String MODE_SELECT = "MODE SELECT";
	public final static String MODE_VIEW = "MODE VIEW";
	public final static String MODE_EDIT = "MODE EDIT: NOW CREATING!!";
	private int mTestTextColor = Color.parseColor("#66FFFF00");

	public String copy() {
		if (mLeft.enable() && mRight.enable()) {
			MyCursor b = mLeft;
			MyCursor e = mRight;
			int textSize = (int) (getTextSize() * getScale());
			if (b.getCursorCol() > e.getCursorCol()
					|| (b.getCursorCol() == e.getCursorCol() && b
							.getCursorRow() > e.getCursorRow())) {
				b = mRight;
				e = mLeft;
			}
			if (b.getCursorCol() < 0) {
				b.setCursorCol(0);
				b.setCursorRow(0);
			}
			if (e.getCursorCol() < 0) {
				e.setCursorCol(0);
				e.setCursorRow(0);
			}

			StringBuilder bb = new StringBuilder();
			LineViewBufferSpec buffer = getLineViewBuffer();

			try {
				if (b.getY() == e.getY()) {
					CharSequence c = buffer.get(b.getCursorCol());
					if (c == null) {
						c = "";
					}
					bb.append(c.subSequence(b.getCursorRow(), e.getCursorRow()));
				} else {
					CharSequence c = buffer.get(b.getCursorCol());
					bb.append(""
							+ c.subSequence(b.getCursorRow(),
									c.length() - b.getCursorRow()));
					for (int i = b.getCursorCol() + 1; i < e.getCursorCol() - 1; i++) {
						bb.append(buffer.get(i));
					}
					CharSequence cc = buffer.get(e.getCursorCol());
					bb.append("" + cc.subSequence(0, e.getCursorRow()));
				}
			} catch (Throwable t) {
				;
			}
			return bb.toString();
		} else {
			return "";
		}
	}

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
		if (mMode.equals(MODE_VIEW)) {
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
		// addChild(mCopy);
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
		// mCopy.setPoint(20, getHeight() - 50);

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
			int textSize = (int) (getTextSize() * getScale());
			if (b.getY() > e.getY()
					|| (b.getY() == e.getY() && b.getX() > e.getX())) {
				b = mRight;
				e = mLeft;
			}

			graphics.setColor(mTestTextColor);
			graphics.setStrokeWidth(10);
			if (b.getY() != e.getY()) {
				graphics.drawLine(b.getX(), b.getY(),
						(int) (getWidth() * 0.95), b.getY());
				graphics.drawLine(this.getXForShowLine(0, 0), e.getY(),
						e.getX(), e.getY());
				graphics.startPath();
				graphics.moveTo((int) (getWidth() * 0.05), b.getY() + textSize);// +getTextSize());
				graphics.lineTo((int) (getWidth() * 0.95), b.getY());
				graphics.lineTo((int) (getWidth() * 0.95), e.getY() - textSize);
				graphics.lineTo((int) (getWidth() * 0.05), e.getY());// +getTextSize());
				graphics.moveTo((int) (getWidth() * 0.05), b.getY());// +getTextSize());
				graphics.endPath();
			} else {
				graphics.drawLine(b.getX(), b.getY(), e.getX(), e.getY());
			}
		}
	}

	//
	private float[] widths = new float[1024];

	private void updateCursor(SimpleGraphics graphics, MyCursor cursor) {
		{
			int y = 0;
			float x = 0.0f;
			int l = 0;
			if(cursor.getCursorCol()<getShowingTextStartPosition()|| cursor.getCursorCol()>getShowingTextEndPosition()){
				// TextViewerとのキャッシュの取り合いで、画面が点滅してしまう。
				// todo 後で対策を考える。
			}else {
				try {
					LineViewData d = getLineViewData(cursor.getCursorCol());
					graphics.setColor(Color.YELLOW);
					if (d != null) {
						l = getBreakText().getTextWidths(d, 0, d.length(), widths);
						// mPaint.
						// for (int i = d.length()-1; i >=
						// (d.length()-mRight.getCursorRow()); i--) {
						for (int i = 0; i < cursor.getCursorRow(); i++) {
							x += widths[i];
						}
					}
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
			y = getYForShowLine((int) (getTextSize() * getScale()),
					cursor.getCursorRow(), cursor.getCursorCol()
							- getShowingTextStartPosition());
			// android.util.Log.v("mkj","xxx="+x+","+y+","+getScale());
			float scale = getSclaeFromTextSize();
			// android.util.Log.v("mkj", "xxx=" + x + "," + y + "," + getScale()
			// + "," + getTextSize() + "/" + getBreakText().getTextSize());

			cursor.setPoint((int) (x * scale) + getXForShowLine(0, 0), y);
		}
	}

	//
	// setScaleとsetTextSize()で拡大率を設定している。
	// 後でどちらかにsetScaleに統一する？
	private float getSclaeFromTextSize() {
		float scale = getScale()
				* (getTextSize() / getBreakText().getTextSize());
		return scale;
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
			if(getCursorCol()<getShowingTextStartPosition()|| getCursorCol()>getShowingTextEndPosition()){
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
			/*
			 * LineViewData d =
			 * CursorableLineView.this.getLineViewData(cursorCol); String a =
			 * ""; if (d != null) { a += d.toString(); }
			 */
			graphics.setTextSize(26);
			graphics.drawText(
					"x=" + cursorRow + ",y=" + cursorCol/* + "," + a */, 100,
					100);
			// android.util.Log.v("kiyo", "zzz=" + "mo,x=" + cursorRow + ",y="
			// + cursorCol);// + "," + a);
		}

		private void drawCursor(SimpleGraphics graphics, int x, int y) {
			graphics.startPath();
			x *= getScale();
			y *= getScale();
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
					// android.util.Log.v("kiyo", "ggg=" + cursorRow + ","
					// + cursorCol + "," + x + "," + px + "," + getX());

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
		int n = (int) (y / (getTextSize()*1.2));
		int yy = n - super.getBlinkY()-1;
		//
		return yy + (getShowingTextStartPosition())+1;
	}

	@Deprecated
	public int getXToPosX(int cursorCol, int x, int cur) {
		x /= getSclaeFromTextSize();
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
		int ret = mInputtedText.getBreakText().breakText(data,
				0,
				data.length(),// x);
				(int) (x - getXForShowLine(0, cursorCol)
						/ getSclaeFromTextSize()));

		return ret;
	}

}
