package info.kyorohiro.helloworld.display.simple;


import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
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
		mCurrentInputConnection = new MyInputConnection(this, false);
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

	private static CharSequence mComposingText = null;
	private static CharSequence mCommitText = null;
	public class MyInputConnection extends BaseInputConnection {

		public CharSequence getComposingText() {
			return mComposingText;
		}

		public CharSequence getCommitText() {
			return mCommitText;
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
			//Editable e = null; 
			//return super.getEditable();
			if(mBuilder == null){
				mBuilder = new SpannableStringBuilder();
			}
			return mBuilder;
			//return mEditableFactory.getInstance().newEditable("");
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
			mCommitText = text.getText();
			return super.commitCompletion(text);
		}
		
		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			log("commitText="+text);
			mCommitText = text;
			return super.commitText(text, newCursorPosition);
		}
	}
	public static void log(String log) {
		android.util.Log.v("kiyo", ""+log);
	}
}
