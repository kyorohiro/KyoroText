package info.kyorohiro.samples.android.surfaceview_editor;



import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

/**
 * For introduce KyoroText logic
 * extrtact essense about ime and to create editor from surface view. 
 */
public class EditableSurfaceView extends SurfaceView {

	private InputMethodManager mManager = null;
	private _MyInputConnection mCurrentInputConnection;
	private IMEController mController = new IMEController();
	private boolean mIMEIsShow = false;
	private static CharSequence mComposingText = null;
	private static CharSequence mCommitText = null;
	private static LinkedList<CommitText> mCommitTextList = new LinkedList<CommitText>();


	//
	// Begin Utility
	//
	public static boolean pushingCtl(KeyEvent event) {
		int ctrl = event.getMetaState();
		if(0x1000==(ctrl&0x1000)||0x2000==(ctrl&0x2000)||0x4000==(ctrl&0x4000)){
			return true;
		} else {
			return false;			
		}
	}
	//
	// End Utility
	//

	//
	// Begin Control ImputMethodService Code
	//
	public void showInputConnection() {
		mManager.showSoftInput(this, 0);
		mIMEIsShow = true;
	}

	public void hideInputConnection() {
        mManager.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		//mIMEIsShow = false;
    	mComposingText = "";
    	mCommitText = "";
    	mCommitTextList.clear();
	}
	//
	// End Control ImputMethodService Code
	//

	public MyInputConnection getCurrentInputConnection() {
		return mCurrentInputConnection;
	}

	public EditableSurfaceView(Context context) {
		super(context);
		mManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		outAttrs.inputType = //EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS|
				EditorInfo.TYPE_CLASS_TEXT;
		outAttrs.imeOptions = 
				//EditorInfo.IME_ACTION_UNSPECIFIED|
				//EditorInfo.IME_ACTION_NONE| 
				//EditorInfo.IME_FLAG_NO_ACCESSORY_ACTION|
				//EditorInfo.IME_FLAG_NO_ENTER_ACTION|
				//EditorInfo.IME_ACTION_DONE|
				EditorInfo.IME_FLAG_NO_EXTRACT_UI;
				/*
				EditorInfo.IME_ACTION_UNSPECIFIED|
				//EditorInfo.IME_MASK_ACTION|EditorInfo.IME_MASK_ACTION|
				//EditorInfo.IME_ACTION_DONE|
				//EditorInfo.IME_FLAG_NO_ENTER_ACTION|
				EditorInfo.IME_FLAG_NO_EXTRACT_UI;*/
				
		if(mCurrentInputConnection == null) {
			mCurrentInputConnection = new _MyInputConnection(this, true);
		}
		return mCurrentInputConnection;
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret  = super.onTouchEvent(event);
		return ret;
	}
	
	@Override
	public boolean onCheckIsTextEditor() {
		return true;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		log("dispatchKeyEvent"+event.getKeyCode()+","+event.toString());
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK||event.getKeyCode() == KeyEvent.KEYCODE_MENU
			||event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN||event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
			return super.dispatchKeyEvent(event);
		}
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			mController.binaryKey(mCurrentInputConnection, event.getKeyCode(), 
					event.isShiftPressed(), pushingCtl(event), event.isAltPressed());
		}
		return true;
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		log("dispatchKeyEventPreIme"+event.getKeyCode()+","+event.toString());
		if(!mIMEIsShow) {
			return super.dispatchKeyEventPreIme(event);			
		} else 
		if(mController.tryUseBinaryKey(event.isShiftPressed(), pushingCtl(event), event.isAltPressed())){
			if(event.getAction() == KeyEvent.ACTION_DOWN) {
				mController.binaryKey(mCurrentInputConnection, event.getKeyCode(), event.isShiftPressed(), pushingCtl(event), event.isAltPressed());
			}
			return true;
		} else {
			return super.dispatchKeyEventPreIme(event);
		}
	}

	@Override
	public boolean dispatchKeyShortcutEvent(KeyEvent event) {
		log("dispatchKeyShortcutEvent"+event.getKeyCode()+","+event.toString());
		return super.dispatchKeyShortcutEvent(event);
	}

	public void setIMEController(IMEController controller) {
		mController = controller;
	}

	public class _MyInputConnection extends BaseInputConnection implements  
	MyInputConnection{

		@Override
		public int getCursorCapsMode(int reqModes) {
			log("getCursorCapsMode" + reqModes);
			return super.getCursorCapsMode(reqModes);
		}
		@Override
		public boolean beginBatchEdit() {
			log("beginBatchEditn");
		 	return super.beginBatchEdit();
		}

		@Override
		public boolean endBatchEdit() {
			log("endBatchEdit");
			return super.endBatchEdit();
		}

		public CharSequence getComposingText() {
			return mComposingText;
		}

		public CharSequence getCommitText() {
			return mCommitText;
		}
		
		public CommitText popFirst() {
			if(0<mCommitTextList.size()){
			return mCommitTextList.removeFirst();
			}
			else {
				return null;
			}
		}

		public _MyInputConnection(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);
			log("new MyInputConnection");
		}

		@Override
		protected void finalize() throws Throwable {
			log("finalize()");
			super.finalize();
		}

		@Override
		public boolean finishComposingText() {
			log("finishComposingText");
			// for asus fskaren 
			{
				if(mComposingText != null&&mComposingText.length() != 0) {
					addCommitText(new CommitText(mComposingText, 1));
					mComposingText = "";
				}
			}
			return super.finishComposingText();
		}

		private SpannableStringBuilder mBuilder = null;
		@Override
		public Editable getEditable() {
			log("getEditable");
			if(mBuilder == null){
				mBuilder = new SpannableStringBuilder();
			}
			return mBuilder;
		}

		@Override
		public boolean setComposingText(CharSequence text, int newCursorPosition) {
			log("setComposingText="+text+","+newCursorPosition);
			mComposingText = text;
			return super.setComposingText(text, newCursorPosition);
		}

		@Override
		public boolean setSelection(int start, int end) {
			log("setSelection s="+start+",e="+end);	
			return super.setSelection(start, end);
		}

		@Override
		public boolean commitCompletion(CompletionInfo text) {
			log("commitCompletion="+text);
			try {
				mCommitText = text.getText();
				addCommitText(new CommitText(text.getText(), text.getPosition()));
				return super.commitCompletion(text);
			} finally {
				mComposingText = "";
				getEditable().clear();
			}
		}

		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			log("commitText="+text+","+newCursorPosition);
			mController.decorateKey(mCurrentInputConnection, text, newCursorPosition, false, false/*ctk*/, false/*alt*/);		
			mCommitText = text;
			try{
				return true;
			} finally {
				mComposingText = "";
				getEditable().clear();
			}
		}

		@Override
		public CharSequence getTextAfterCursor(int length, int flags) {
			try {
				CharSequence a = super.getTextAfterCursor(length, flags);
				log("getTextAfterCursor="+a.toString()+","+length+","+flags);
				return a;
			} catch(Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		@Override
		public CharSequence getTextBeforeCursor(int length, int flags) {
			CharSequence a = super.getTextBeforeCursor(length, flags);
			log("getTextBeforeCursor="+a.toString()+","+length+","+flags);
			return a;
		}
		@Override
		public void addCommitText(CommitText text) {
			log("# addlast---"+text);
			mCommitTextList.addLast(text);
		}

		//
		// ======================================================================
		//
		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			log("sendKeyEvent="+event.toString());
			return super.sendKeyEvent(event);
		}
		
		@Override
		public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
			log("getExtractedText="+request.hintMaxChars+","+flags);
			return super.getExtractedText(request, flags);
		}
		@Override
		public boolean performEditorAction(int actionCode) {
			log("performEditorAction="+actionCode);
			return super.performEditorAction(actionCode);
		}

		@Override
		public boolean performContextMenuAction(int id) {
			log("performContextMenuAction="+id);
			return super.performContextMenuAction(id);
		}
		
		@Override
		public boolean performPrivateCommand(String action, Bundle data) {
			log("performPrivateCommand="+action+","+data.toString());
			return super.performPrivateCommand(action, data);
		}

		@Override
		public boolean reportFullscreenMode(boolean enabled) {
			log("reportFullscreenMode="+enabled);
			return super.reportFullscreenMode(enabled);
		}
		
	}

	public static void log(String log) {
		android.util.Log.v("kiyo", ""+log);
	}
}
