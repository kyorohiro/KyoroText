package info.kyorohiro.helloworld.display.widget.editview;

import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleKeyEvent;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;

/**
 * This Class
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
				mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
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

	public boolean isEdit() {
		return mTextBuffer.isEdit();
	}

	@Override
	public void setMode(String mode) {
		super.setMode(mode);
		if(CursorableLineView.MODE_EDIT!=mode){
			hideIME();
		}
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

	private void hideIME() {
		SimpleStage stage = getStage(this);
		stage.hideInputConnection();
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
		} 
		getLeft().setMessage(c.getComposingText());
	}

	/* todo now creating*/
	public void inputText(CharSequence clip) {
		MyInputConnection c = getMyInputConnection();
		if (c == null) {
			return;
		} 
		String[] lines = clip.toString().split("\r\n|\n");
		for(int i=0;i<lines.length;i++) {
			c.addCommitText(lines[i], 1);
			if(i+1<lines.length){
				c.addKeyEvent(0x42);
//				c.addKeyEvent(KeyEvent.KEYCODE_ENTER);
			}
		}
	}

	private void updateCommitTextFromIME() {
		// following code is yaxutuke sigoto
		if(!isFocus()){
			return;
		}
		MyInputConnection c = getMyInputConnection();
		if (c == null) {
			return;
		} // <-- �ｽ�ｽ�ｽ�ｽ�ｽ�ｽ�ｽﾆゑｿｽ�ｽ驍ｱ�ｽﾆはなゑｿｽ
		mTextBuffer.IsCrlfMode(this.isCrlfMode());
		while (true) {
			CommitText text = c.popFirst();
			if (text != null) {
				if (text.isKeyCode()) {
					android.util.Log.v("kiyo","key="+text.getKeyCode());
					switch (text.getKeyCode()) {
					case SimpleKeyEvent.KEYCODE_BACK:
					case SimpleKeyEvent.KEYCODE_DEL:
						mTextBuffer.delete();
						break;
					case SimpleKeyEvent.KEYCODE_ENTER:
						mTextBuffer.crlf();
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_LEFT:
						back();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_RIGHT:
						front();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_UP:
						prev();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_DOWN:
						next();
						mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_SPACE:
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
