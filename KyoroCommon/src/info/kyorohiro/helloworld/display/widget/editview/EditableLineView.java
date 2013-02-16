package info.kyorohiro.helloworld.display.widget.editview;


import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.MyCursor;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager;

/**
 * This Class
 */
public class EditableLineView extends CursorableLineView {

	private EditableLineViewBuffer mTextBuffer = null;
	private KeyEventManager mKeyEventManager = new KeyEventManager();
	private int mR = 0;
	private int mC = 0;

	public EditableLineView(LineViewBufferSpec v, int textSize, int cashSize) {
		super(new EditableLineViewBuffer(v), textSize, cashSize);
		mTextBuffer = (EditableLineViewBuffer) getLineViewBuffer();
	}

	public void setKeyEventManager(KeyEventManager manager) {
		mKeyEventManager = manager;
	}
	public EditableLineViewBuffer getEditableLineBuffer() {
		return mTextBuffer;
	}
	@Override
	public synchronized MyCursor getLeft() {
		return super.getLeft();
	}

	public void clear() {
		if(mTextBuffer != null) {
			mTextBuffer.clear();
		}
	}

	public void recenter() {
		setPositionY(-getLeft().getCursorCol()+
				-(getShowingTextEndPosition()-getShowingTextStartPosition())/2
				+ getLineViewBuffer().getNumberOfStockedElement());
	}

	private void modCursor(int r, int c) {
		if(mR!=r||mC!=c){
			EditableLineViewBuffer.clearYank();
		} else {
			mR = r;
			mC = c;
		}
	}

	@Override
	public synchronized void paint(SimpleGraphics graphics) {
		modCursor(getLeft().getCursorRow(), getLeft().getCursorCol());
		if(isFocus()) {
			try {
				updateCommitTextFromIME();
				if (editable()) {
					if(isTail()&&!CursorableLineView.MODE_SELECT.equals(getMode())){//&&!BufferManager.getManager().getMiniBuffer().isAliveTask()){
						getLeft().setCursorCol(getLineViewBuffer().getNumberOfStockedElement()-1);
					}
					updateComposingTextFromIME();
				}
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

	private boolean mIsChMode = false;
	public void setConstantMode(String mode) {
		if(!mIsChMode||mode.equals(getMode())) {
			setMode(mode);
		}
		mIsChMode = true;
	}

	@Override
	public void setMode(String mode) {
		if(!mIsChMode) {
			super.setMode(mode);
			if(!CursorableLineView.MODE_EDIT.startsWith(""+mode)){
				hideIME();
			}
		}
	}

	@Override
	public void isFocus(boolean isFocus) {
		super.isFocus(isFocus);
		if(isFocus()) {
			if (editable()) {
				showIME();
			}
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
		if(stage != null) {
			stage.showInputConnection();
		}
	}

	private void hideIME() {
		SimpleStage stage = getStage(this);
		if(stage != null) {
			stage.hideInputConnection();
		}
	}

	private boolean editable() {
		if (getMode() == CursorableLineView.MODE_EDIT
				|| getMode().toString().startsWith(CursorableLineView.MODE_EDIT)) {
			return true;
		} else {
			return false;
		}
	}

	public MyInputConnection getMyInputConnection() {
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
			c.addCommitText(new CommitText(lines[i], 1));
			if(i+1<lines.length){
				c.addCommitText(new CommitText(0x42));
			}
		}
	}

	private void updateCommitTextFromIME() {
		int prev = mTextBuffer.getNumberOfStockedElement();
		mKeyEventManager.updateCommitTextFromIME(this, mTextBuffer);
		int current = mTextBuffer.getNumberOfStockedElement();
		if(getLeft().getCursorCol()+2<getShowingTextEndPosition()) {
			setPositionY(getPositionY()+current-prev);
		}
	}
}
