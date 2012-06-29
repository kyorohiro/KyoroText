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

	
//	private Editable.Factory mEditableFactory = Editable.Factory.getInstance();
//  private Spannable.Factory mSpannableFactory = Spannable.Factory.getInstance();
	private InputMethodManager mManager = null;

	private MyInputConnection mCurrentInputConnection = null;
	public EditableSurfaceView(Context context) {
		super(context);
		mManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		setFocusable(true);
		setFocusableInTouchMode(true);
		//setBackgroundColor(Color.WHITE);
	}

	@Override
	public void clearFocus() {
		super.clearFocus();
	}
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//         mManager.showSoftInput(this, 0);
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
//		mManager.showSoftInput(this,
//				InputMethodManager.SHOW_IMPLICIT);
//				InputMethodManager.RESULT_SHOWN);
//		
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
	private static LinkedList<CharSequence> mCommitTextList = new LinkedList<CharSequence>();
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
			log("--1--="+Selection.getSelectionStart(getEditable()));
			log("--2--="+Selection.getSelectionEnd(getEditable()));
			return super.beginBatchEdit();
		}

		@Override
		public boolean endBatchEdit() {
			log("endBatchEdit");
			log("--1--="+Selection.getSelectionStart(getEditable()));
			log("--2--="+Selection.getSelectionEnd(getEditable()));
			return super.endBatchEdit();
		}

		public CharSequence getComposingText() {
			return mComposingText;
		}

		public CharSequence getCommitText() {
			return mCommitText;
		}
		
		public CharSequence popFirst() {
			if(0<mCommitTextList.size()){
			return mCommitTextList.removeFirst();
			}
			else {
				return "";
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
			//log("getEditable");
			//Editable e = null; 
			//return super.getEditable();
			if(mBuilder == null){
				mBuilder = new SpannableStringBuilder();
				mBuilder.setFilters(new InputFilter[]{new A()});
			}
			return mBuilder;
			//return mEditableFactory.getInstance().newEditable("");
		}

		@Override
		public boolean setComposingText(CharSequence text, int newCursorPosition) {
			log("setComposingText="+text+","+newCursorPosition);
			log("--1--="+Selection.getSelectionStart(text));
			log("--2--="+Selection.getSelectionEnd(text));
			log("--3--="+Selection.getSelectionStart(getEditable()));
			log("--3--="+Selection.getSelectionEnd(getEditable()));

			mComposingText = text;
		
			if(3<=text.length()){
				SpannableString s = (SpannableString)text;
				Object[] a = s.getSpans(0, s.length(), Object.class);
				for(Object b:a) {
					log("0="+b.toString());							
					log("1="+s.getSpanStart(b));							
					log("2="+s.getSpanEnd(b));	
				}
//				log(""+s.getSpanFlags(s.charAt(2)));							
//				graphics.drawText(""+s.getSpanStart(c.getEditable().), 120, 160);							
			}

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
//			log("--1--="+Selection.getSelectionStart(text.getText()));
//			log("--2--="+Selection.getSelectionEnd(text.getText()));
//			log("--3--="+text.getPosition());
			try {
				mCommitText = text.getText();
				mCommitTextList.addLast(mCommitText);
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
			mCommitTextList.addLast(mCommitText);
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
	public static void log(String log) {
		//android.util.Log.v("kiyo", ""+log);
	}

	public class A implements InputFilter {

		@Override
		public CharSequence filter(CharSequence arg0, int arg1, int arg2,
				Spanned arg3, int arg4, int arg5) {
			log("filter="+arg0+","+arg1+","+arg2);
			log("filter="+arg3+","+arg4+","+arg5);
			return null;
		}
		
	}
}
