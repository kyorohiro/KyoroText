package info.kyorohiro.helloworld.display.widget.lineview.edit;

import android.view.KeyEvent;
import info.kyorohiro.helloworld.android.adapter.EditableSurfaceView.CommitText;
import info.kyorohiro.helloworld.android.adapter.EditableSurfaceView.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;

/**
 * This Class は CursorableLineViewにIMEからの編集機能を追加したものです。
 * 
 * [方針] LineViewへ渡しているテキストデータのDIFFデータをキャッシュする。 表示するときは、毎回DIFFデータをマージして、画面に表示する。
 * 
 */
public class EditableLineView extends CursorableLineView {

	private EditableLineViewBuffer mTextBuffer = null;

	public EditableLineView(EditableLineViewBuffer v, int textSize, int cashSize) {
		super(v, textSize, cashSize);
		mTextBuffer = (EditableLineViewBuffer) getLineViewBuffer();
	}

	public EditableLineView(LineViewBufferSpec v, int textSize, int cashSize) {
		super(new EditableLineViewBuffer(v), textSize, cashSize);
		mTextBuffer = (EditableLineViewBuffer) getLineViewBuffer();
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		if (editable()) {
			try {
				mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft()
						.getCursorCol());
				updateCommitTextFromIME();
				updateComposingTextFromIME();
				getLeft().setCursorRow(mTextBuffer.getRow());
				getLeft().setCursorCol(mTextBuffer.getCol());
				// android.util.Log.v("kiyo","abaP="+getLeft().getCursorCol()+","+getLeft().getCursorRow());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		super.paint(graphics);
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (editable() && inside(x, y)) {
			showIME();
		}
		return super.onTouchTest(x, y, action);
	}

	@Override
	public synchronized void setLineViewBufferSpec(
			LineViewBufferSpec inputtedText) {
		try {
			lock();
			super.setLineViewBufferSpec(mTextBuffer = new EditableLineViewBuffer(
					inputtedText));
		} finally {
			releaseLock();
		}
	}

	private boolean inside(int x, int y) {
		if (0 < x && x < this.getWidth() && 0 < y && y < this.getHeight()) {
			return true;
		} else {
			return false;
		}
	}

	private void showIME() {
		SimpleStage stage = getStage(this);
		stage.showInputConnection();
	}

	private boolean editable() {
		if (getMode() == CursorableLineView.MODE_EDIT
				|| getMode().equals(CursorableLineView.MODE_EDIT)) {
			return true;
		} else {
			return false;
		}
	}

	private MyInputConnection getMyInputConnection() {
		SimpleStage stage = getStage(this);
		MyInputConnection c = stage.getCurrentInputConnection();
		return c;
	}

	private void updateComposingTextFromIME() {
		MyInputConnection c = getMyInputConnection();
		if (c == null) {
			return;
		} // <-- ここをとおることはない
		getLeft().setMessage(c.getComposingText());
	}
	private void updateCommitTextFromIME() {
		MyInputConnection c = getMyInputConnection();
		if (c == null) {
			return;
		} // <-- ここをとおることはない
		mTextBuffer.IsCrlfMode(this.isCrlfMode());
		while (true) {
			CommitText text = c.popFirst();
			if (text != null) {
				if (text.isKeyCode()) {
					switch (text.getKeyCode()) {
					case KeyEvent.KEYCODE_BACK:
					case KeyEvent.KEYCODE_DEL:
						mTextBuffer.delete();
						break;
					case KeyEvent.KEYCODE_ENTER:
						mTextBuffer.crlf();
						break;
					case KeyEvent.KEYCODE_DPAD_LEFT:
						back();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case KeyEvent.KEYCODE_DPAD_RIGHT:
						front();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case KeyEvent.KEYCODE_DPAD_UP:
						next();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case KeyEvent.KEYCODE_DPAD_DOWN:
						prev();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case KeyEvent.KEYCODE_SPACE:
						mTextBuffer.pushCommit(" ",1);
						break;
					}
				} else {
					mTextBuffer.pushCommit(text.getText(),
							text.getNewCursorPosition());
				}
				getStage(this).resetTimer();
			} else {
				break;
			}
		}
	}
}
