package info.kyorohiro.helloworld.display.widget.lineview;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.text.KyoroString;

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
	private CharSequence mMessage = "";

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

	public void setMessage(CharSequence message) {
		mMessage = message;
	}

	public void setCursorCol(int col) {
		if(col<0){col = 0;}//todo
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

	private void lock() {
		mParent.get().lock();
	}

	private void releaseLock(){
		mParent.get().releaseLock();	
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		if (!mEnable) {
			return;
		}
		if (!focus) {
			graphics.setColor(CursorableLineView.__CURSOR__COLOR);
		} else {
			graphics.setColor(Color.parseColor("#AA00FFFF"));
			drawCursor(graphics, mX - px, mY - py);
			graphics.setColor(Color.parseColor("#AA00FFFF"));
		}
		drawCursor(graphics, 0, 0);

		graphics.setTextSize(26);
		graphics.drawText("x=" + cursorRow + ",y=" + cursorCol.getPoint(), 10, 100);
		if(mMessage != null){
			graphics.setColor(Color.parseColor("#AA005555"));
			graphics.drawText(mMessage, 20, 20);
		}
		graphics.drawLine(0, 0, 0, -20);
		graphics.setColor(CursorableLineView.__CURSOR__COLOR2);
		graphics.drawLine(0, 0, 0, -1*6);
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
				try {
					lock();
					cursorCol.setPoint(getYToPosY(y - py + getY()));
					cursorRow = getXToPosX(cursorCol.getPoint(), x - px + getX(), cursorRow);
				} 
				catch(Exception e){
					e.printStackTrace();
				}finally {
					releaseLock();
				}
			}
		}
		return focus;
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
	public void updateCursor() {
//		android.util.Log.v("kiyo","cursor:c="+getCursorCol()+",r="+getCursorRow());
		int y = 0;
		float x = 0.0f;
		int l = 0;
		if (this.getCursorCol() < mParent.get().getShowingTextStartPosition()
				|| this.getCursorCol() > mParent.get().getShowingTextEndPosition()) {
			// TextViewerとのキャッシュの取り合いで、画面が点滅してしまう。
			// todo 後で対策を考える。
			// LineView側がバッファの中身について知らなくても良いようにする。、
		} else {
			KyoroString d = null;
			try {
				lock();
				int yy = this.getCursorCol();
				if(mParent.get().isOver()){
					yy-=mParent.get().getLineViewBuffer().getNumOfAdd();
				}
				d = mParent.get().getKyoroString(yy);
			} finally {
				releaseLock();
			}
	//		android.util.Log.v("kiyo","cursor:d="+d+","+d.length());
	//		android.util.Log.v("kiyo","cursor:d_l="+d.length());

			try {
				if (d != null) {
					l = mParent.get().getBreakText()
							.getTextWidths(d, 0,
							this.getCursorRow(), mParent.get().widths,
							mParent.get().getShowingTextSize());

					//	android.util.Log.v("kiyo","cursor:l="+l);
					for (int i = 0; i < l; i++) {
						x += mParent.get().widths[i];
					}
					//android.util.Log.v("kiyo","cursor:x="+x);
				}
			} catch(Throwable t){
				// todo refactaring BreakTextは他者が定義するので、念のため
			}
		}

		try {
			lock();
			y = mParent.get().getYForStartDrawLine(this.getCursorCol()-mParent.get().getShowingTextStartPosition());
			this.setPoint((int) x + mParent.get().getLeftForStartDrawLine(), y);
		} finally {
			releaseLock();
		}
	}
}