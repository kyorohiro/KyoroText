package info.kyorohiro.helloworld.display.simple;


import java.util.LinkedList;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

public class EditableSurfaceView extends MultiTouchSurfaceView {

	private InputMethodManager mManager = null;
	private MyInputConnection mCurrentInputConnection = null;
	public EditableSurfaceView(Context context) {
		super(context);
		mManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	public void clearFocus() {
		super.clearFocus();
	}
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		outAttrs.inputType = EditorInfo.TYPE_CLASS_TEXT;
		outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE|EditorInfo.IME_FLAG_NO_EXTRACT_UI;
		if(mCurrentInputConnection == null) {
			mCurrentInputConnection = new MyInputConnection(this, true);
		}
		return mCurrentInputConnection;
	}

	public MyInputConnection getCurrentInputConnection() {
		return mCurrentInputConnection;
	}

	public void showInputConnection() {
		mManager.showSoftInput(this, 0);
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
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		log("dispatchKeyEventPreIme"+event.getKeyCode());
		return super.dispatchKeyEventPreIme(event);
	}
	
	private static CharSequence mComposingText = null;
	private static CharSequence mCommitText = null;
	private static LinkedList<CommitText> mCommitTextList = new LinkedList<CommitText>();
	private static int mStart = 0;
	private static int mEnd = 0;
	public class MyInputConnection extends BaseInputConnection {

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
		public MyInputConnection(View targetView, boolean fullEditor) {
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

			mStart = start;
			mEnd = end;
			return super.setSelection(start, end);
		}

		@Override
		public boolean commitCompletion(CompletionInfo text) {
			log("commitCompletion="+text);
			try {
				mCommitText = text.getText();
				mCommitTextList.addLast(new CommitText(text.getText(), text.getPosition()));
				return super.commitCompletion(text);
			} finally {
				mComposingText = "";
				getEditable().clear();
			}
		}
		
		
		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			log("commitText="+text+","+newCursorPosition);
			log("--1--="+Selection.getSelectionStart(text));
			log("--2--="+Selection.getSelectionEnd(text));
			mCommitText = text;
			mCommitTextList.addLast(new CommitText(text, newCursorPosition));
			try{
				return super.commitText(text, newCursorPosition);
			} finally {
				mComposingText = "";
				getEditable().clear();
			}

		}

		@Override
		public CharSequence getTextAfterCursor(int length, int flags) {
			CharSequence a = super.getTextAfterCursor(length, flags);
			log("getTextAfterCursor="+a.toString()+","+length+","+flags);
			return a;
		}
		@Override
		public CharSequence getTextBeforeCursor(int length, int flags) {
			CharSequence a = super.getTextBeforeCursor(length, flags);
			log("getTextBeforeCursor="+a.toString()+","+length+","+flags);
			return a;
		}
		
		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			log("sendKeyEvent="+event.toString());
			return super.sendKeyEvent(event);
		}
		
		@Override
		public ExtractedText getExtractedText(ExtractedTextRequest request,
				int flags) {
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
	public static class CommitText {
		private CharSequence mText = null;
		private int mNewCursorPosition = 0;
		public CommitText(CharSequence text, int newCursorPosition) {
			mText = text;
			mNewCursorPosition = newCursorPosition;
		}
		public CharSequence getText() {
			return mText;
		}
		public int getNewCursorPosition() {
			return mNewCursorPosition;
		}
	}
	public static void log(String log) {
		//android.util.Log.v("kiyo", ""+log);
	}
}
