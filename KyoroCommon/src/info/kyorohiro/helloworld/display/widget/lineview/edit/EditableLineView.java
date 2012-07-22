package info.kyorohiro.helloworld.display.widget.lineview.edit;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import android.graphics.Color;
import android.view.KeyEvent;

import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.CommitText;
import info.kyorohiro.helloworld.display.simple.EditableSurfaceView.MyInputConnection;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.io.MyBuilder;
import info.kyorohiro.helloworld.io.BigLineDataBuilder.W;

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
				mTextBuffer.pushCommit(text.getText(),
						text.getNewCursorPosition());
				getStage(this).resetTimer();
			} else {
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keycode) {
		if(editable()){
			try {
				lock();
				//
				// 本来ならば、MyInputConnectionでするべき処理
				// ちょっと動作確認したいだけなので、
				// ここに書く。
				if(keycode == KeyEvent.KEYCODE_DEL || keycode == KeyEvent.KEYCODE_BACK) {
					mTextBuffer.setCursor(getLeft().getCursorRow(), getLeft().getCursorCol());			
					mTextBuffer.deleteOne();
					getLeft().setCursorRow(mTextBuffer.getRow());
					getLeft().setCursorCol(mTextBuffer.getCol());
				}
				if(keycode == KeyEvent.KEYCODE_ENTER) {
					//mTextBuffer.crlfOne();			
				}
				return super.onKeyDown(keycode);
			}finally {
				releaseLock();
			}
		}
		return super.onKeyDown(keycode);
	}
}
