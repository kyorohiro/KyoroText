package info.kyorohiro.helloworld.display.widget.lineview.edit;

import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.CommitText;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.MyInputConnection;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;

/**
 * This Class は CursorableLineViewにIMEからの編集機能を追加したものです。
 * 
 * [方針]
 *   LineViewへ渡しているテキストデータのDIFFデータをキャッシュする。
 *   表示するときは、毎回DIFFデータをマージして、画面に表示する。
 *
 */
public class EditableLineView extends CursorableLineView {

	private EditableLineViewBuffer mTextBuffer = null;

	public EditableLineView(LineViewBufferSpec v, int textSize, int cashSize) {
		super(new EditableLineViewBuffer(v), textSize, cashSize);
		mTextBuffer = (EditableLineViewBuffer) getLineViewBuffer();
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		if(editable()) {
			try {
				mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
				updateOnIMEOutput();
				getLeft().setCursorRow(mTextBuffer.getRow());
				getLeft().setCursorCol(mTextBuffer.getCol());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		super.paint(graphics);
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if (editable()&&inside(x, y)) {
			showIME();
		}
		return super.onTouchTest(x, y, action);
	}

	@Override
	public synchronized void setLineViewBufferSpec(LineViewBufferSpec inputtedText) {
		try {
			lock();
			super.setLineViewBufferSpec(mTextBuffer = new EditableLineViewBuffer(inputtedText));
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
		if (getMode() == CursorableLineView.MODE_EDIT||getMode().equals(CursorableLineView.MODE_EDIT)) {
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

	private void updateOnIMEOutput() {
		MyInputConnection c = getMyInputConnection();
		if (c == null) {return;} // <-- ここをとおることはない

		while (true) {
			CommitText text = c.popFirst();
			if (text != null) {
				if(text.isDeleted()){
					mTextBuffer.delete();
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
