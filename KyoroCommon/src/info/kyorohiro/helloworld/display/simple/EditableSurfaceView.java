package info.kyorohiro.helloworld.display.simple;


import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

public class EditableSurfaceView extends SurfaceView {

	private Editable.Factory mEditableFactory = Editable.Factory.getInstance();
    private Spannable.Factory mSpannableFactory = Spannable.Factory.getInstance();
	private InputMethodManager mManager = null;

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
		return new MyInputConnection(this, true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		mManager.showSoftInput(this,
//				InputMethodManager.SHOW_IMPLICIT);
//				InputMethodManager.RESULT_SHOWN);
//		
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean onCheckIsTextEditor() {
		return true;
	}

	public class MyInputConnection extends BaseInputConnection {

		public MyInputConnection(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);
//			android.util.Log.v("kiyo", "new MyInputConnection");
		}
		
		@Override
		protected void finalize() throws Throwable {
//			android.util.Log.v("kiyo", "finalize()");
			super.finalize();
		}

		@Override
		public boolean finishComposingText() {
//			android.util.Log.v("kiyo", "finishComposingText");
			return super.finishComposingText();
		}

		private SpannableStringBuilder mBuilder = 
			null;
		@Override
		public Editable getEditable() {
//			android.util.Log.v("kiyo", "getEditable");
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
//			android.util.Log.v("kiyo", "compositionText="+text+","+newCursorPosition);
			return super.setComposingText(text, newCursorPosition);
		}

		@Override
		public boolean setSelection(int start, int end) {
//			android.util.Log.v("kiyo", "selection s="+start+",e="+end);			
			return super.setSelection(start, end);
		}

		@Override
		public boolean commitCompletion(CompletionInfo text) {
//			android.util.Log.v("kiyo", "completion="+text);
			return super.commitCompletion(text);
		}
		
		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
//			android.util.Log.v("kiyo", "text="+text);
			return super.commitText(text, newCursorPosition);
		}
	}
}
