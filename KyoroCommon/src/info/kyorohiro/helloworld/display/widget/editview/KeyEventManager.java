package info.kyorohiro.helloworld.display.widget.editview;

import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleKeyEvent;
import info.kyorohiro.helloworld.text.KyoroString;

public class KeyEventManager {
//	private boolean mIsCommandMode = false;
	private boolean mIsEscMode = false;
	private void update(CommitText ct) {
		if(!mIsEscMode) {
			if(ct==null){
				//android.util.Log.v("kiyo","#esc null");
			}
			if(ct.pushingEsc()||ct.getKeyCode()==111) {
				//android.util.Log.v("kiyo","#esc on");
				mIsEscMode = true;
			}
		} else {
			if(ct.pushingEsc()||ct.getText().equals("")||ct.getText().equals("<")
					||ct.getText().equals(">")||ct.getText().equals("{")
					||ct.getKeyCode() == 111){
				
			}else {
				mIsEscMode = false;
			}
		}
	}

	public void updateCommitTextFromIME(EditableLineView mTextView, EditableLineViewBuffer mTextBuffer) {
		boolean esc = mIsEscMode;
		// following code is yaxutuke sigoto
		if(!mTextView.isFocus()){
			return;
		}
		MyInputConnection c = mTextView.getMyInputConnection();
		if (c == null) {
			return;
		} 

		mTextBuffer.IsCrlfMode(mTextView.isCrlfMode());
		while (true) {
			CommitText text = c.popFirst();
			if (text != null) {
				update(text);
				//if (text))
				if (mIsEscMode) {
					CharSequence t = text.getText();
					if(t.length() != 0) {
						int v = t.charAt(0);
						// move
						switch(v){
						case '<':
							mTextBuffer.setCursor(0, 0);
							mTextView.setCursorAndCRLF(mTextView.getLeft(), 0, 0);
							mTextView.recenter();
							mIsEscMode = false;
							break;
						case '>':
							int len = mTextBuffer.getNumberOfStockedElement();
							KyoroString ks = null;
							if(len-1>=0) {
								ks = mTextBuffer.get(len-1);
							}
							if(ks!=null){
								mTextBuffer.setCursor(ks.lengthWithoutLF(mIsEscMode), len-1);
								mTextView.setCursorAndCRLF(mTextView.getLeft(), mTextBuffer.getRow(), mTextBuffer.getCol());
							}
							mTextView.recenter();
							mIsEscMode = false;
							break;
						}
					}
				} else 
				if (text.pushingCtrl()) {
					CharSequence t = text.getText();
					if(t.length() != 0) {
						int v = t.charAt(0);
						// move
						switch(v){
						case 'a':
							mTextBuffer.setCursor(0, mTextBuffer.getCol());
							break;
						case 'e':
							int _c = mTextBuffer.getCol();
							CharSequence _t = mTextBuffer.get(_c);
							mTextBuffer.setCursor(_t.length(), _c);
							break;
						case 'n':
							mTextView.next();
							mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
							break;
						case 'p':
							mTextView.prev();
							mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
							break;
						case 'f':
							mTextView.front();
							mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
							break;
						case 'b':
							mTextView.back();
							mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
							break;
						case 'h':
							mTextBuffer.backwardDeleteChar();
							break;
						case 'd':
							mTextBuffer.deleteChar();
							break;
						case 'l':
							mTextView.recenter();
							break;
						}
						// delete
						switch(v){
						case 'k':
							//android.util.Log.v("kiyo","---k---");
							int _c = mTextBuffer.getCol();
							if(0<=_c&&_c<mTextBuffer.getNumberOfStockedElement()){
								//android.util.Log.v("kiyo","---in---");
//								mTextBuffer.deleteLinePerVisible();
								mTextBuffer.killLine();
							} else {
								//android.util.Log.v("kiyo","---out---"+_c);
							}
							break;
						}
					}
//				}
				} else 
				if (text.isKeyCode()) {
//					android.util.Log.v("kiyo","key="+text.getKeyCode());
					switch (text.getKeyCode()) {
					case SimpleKeyEvent.KEYCODE_BACK:
					case SimpleKeyEvent.KEYCODE_DEL:
						mTextBuffer.deleteChar();
						break;
					case SimpleKeyEvent.KEYCODE_ENTER:
						mTextBuffer.crlf();
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_LEFT:
						mTextView.back();
						mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_RIGHT:
						mTextView.front();
						mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_UP:
						mTextView.prev();
						mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_DPAD_DOWN:
						mTextView.next();
						mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
						break;
					case SimpleKeyEvent.KEYCODE_SPACE:
						mTextBuffer.pushCommit(" ",1);
						break;
					}
				} else {
					mTextBuffer.pushCommit(text.getText(),
							text.getNewCursorPosition());
				}
				
				SimpleDisplayObject.getStage(mTextView).resetTimer();
			} else {
				break;
			}
		}
	}
}
