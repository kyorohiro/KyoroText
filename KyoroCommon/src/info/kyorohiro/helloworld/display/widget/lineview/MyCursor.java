package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.android.util.SimpleLockInter;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.lineview.LineView.Point;

import java.lang.ref.WeakReference;

import android.graphics.Color;
import android.view.MotionEvent;

public class MyCursor extends SimpleDisplayObject {
	private int cursorRow = 0;
	private Point cursorCol;
	private int mX = 0;
	private int mY = 0;
	private int px = 0;
	private int py = 0;
	private boolean focus = false;
	private boolean mEnable = false;
	private WeakReference<LineView> mParent;

	public MyCursor(LineView lineview) {
		mParent = new WeakReference<LineView>(lineview);
		cursorCol = lineview.getPoint(0);
	}

	public boolean enable() {
		return mEnable;
	}

	public void enable(boolean enable) {
		mEnable = enable;
	}

	public void setCursorCol(int col) {
		cursorCol.setPoint(col);
	}

	public void setCursorCol(Point col) {
		cursorCol = col;
	}

	public void setCursorRow(int row) {
		cursorRow = row;
	}

	public int getCursorRow() {
		return cursorRow;
	}

	public int getCursorCol() {
		return cursorCol.getPoint();
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		if (!mEnable) {
			return;
		}
		if (!focus) {
			graphics.setColor(Color.parseColor("#66FFFF00"));
		} else {
			graphics.setColor(Color.parseColor("#3300FFFF"));
			drawCursor(graphics, mX - px, mY - py);
			graphics.setColor(Color.parseColor("#6600FFFF"));
		}
		drawCursor(graphics, 0, 0);

		graphics.setTextSize(26);
		graphics.drawText("x=" + cursorRow + ",y=" + cursorCol.getPoint(), 10, 100);
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
		//try {
		//	mParent.get().lock();

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
				LineView l = mParent.get();
				try {
					l.lock();
					cursorCol.setPoint(getYToPosY(y - py + getY()));
					cursorRow = getXToPosX(cursorCol.getPoint(), x - px + getX(), cursorRow);
				} 
				catch(Exception e){
					// todo
				}finally {
					l.releaseLock();
				}
				//updateCursor(this);
			}
		}
		return focus;
		//}finally {
		//		mParent.get().releaseLock();
		//}
	}

	private int getYToPosY(int y) {
		LineView l = mParent.get();
		if(l != null){
			return l.getYToPosY(y);
		} else {
			return 0;
		}
	}

	private int getXToPosX(int y, int xx, int cur) {
		LineView l = mParent.get();
		if(l != null){
			return l.getXToPosX(y, xx, cur);
		} else {
			return 0;
		}
	}

	@Override
	public boolean includeParentRect() {
		return false;
	}
	public synchronized void updateCursor() {
		int y = 0;
		float x = 0.0f;
		int l = 0;
		if (this.getCursorCol() < mParent.get().getShowingTextStartPosition()
				|| this.getCursorCol() > mParent.get().getShowingTextEndPosition()) {
			// TextViewerとのキャッシュの取り合いで、画面が点滅してしまう。
			// todo 後で対策を考える。
			// LineView側がバッファの中身について知らなくても良いようにする。、
		} else {
			LineViewData d = null;
			try {
				mParent.get().lock();
				int yy = this.getCursorCol();
				if(mParent.get().isOver()){
					yy-=mParent.get().getLineViewBuffer().getNumOfAdd();
				}
				d = mParent.get().getLineViewData(yy);
			} finally {
				mParent.get().releaseLock();
			}

			try {
				if (d != null) {
					l = mParent.get().getBreakText().getTextWidths(d, 0,
							this.getCursorRow(), mParent.get().widths,
							mParent.get().getShowingTextSize());
					for (int i = 0; i < l; i++) {
						x += mParent.get().widths[i];
					}
				}
			} catch(Throwable t){
				// todo refactaring BreakTextは他者が定義するので、念のため
			}
		}

		try {
			mParent.get().lock();
			y = mParent.get().getYForStartDrawLine(this.getCursorCol()-mParent.get().getShowingTextStartPosition());
			this.setPoint((int) x + mParent.get().getLeftForStartDrawLine(), y);
		} finally {
			mParent.get().releaseLock();
		}
	}
}