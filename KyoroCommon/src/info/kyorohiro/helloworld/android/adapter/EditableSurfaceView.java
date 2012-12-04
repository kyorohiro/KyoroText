package info.kyorohiro.helloworld.android.adapter;


import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;

import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
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

/**
 * todo Android�Ɉˑ����Ă��镔���͕�������
 */
public class EditableSurfaceView extends MultiTouchSurfaceView {

	private InputMethodManager mManager = null;
	private _MyInputConnection mCurrentInputConnection = null;
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
		//mManager.
		return mCurrentInputConnection;
	}

	public MyInputConnection getCurrentInputConnection() {
		return mCurrentInputConnection;
	}

	public void showInputConnection() {
		mManager.showSoftInput(this, 0);
	}

	public void hideInputConnection() {
        mManager.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    	mComposingText = "";
    	mCommitText = "";
    	mCommitTextList.clear();
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

	private boolean mPushingCtl = false;
	public boolean pushingCtl() {
		return mPushingCtl;
	}

	private static HashMap<Integer, Character> sMMap = new HashMap<Integer, Character>();
	private static HashMap<Integer, Character> sRMap = new HashMap<Integer, Character>();
	static {
		sMMap.put(77, '@');
		sRMap.put(77, '`');
		sMMap.put(74, ';');
		sRMap.put(74, '+');
		sMMap.put(75, ':');
		sRMap.put(75, '*');
		sMMap.put(72, ']');
		sRMap.put(72, '}');
		sMMap.put(71, '[');
		sRMap.put(71, '{');
		sMMap.put(76, '/');
		sRMap.put(76, '?');
		sMMap.put(73, '\\');
		sRMap.put(73, '_');
		sMMap.put(73, '\\');
		sMMap.put(55, ',');
		sRMap.put(55, '<');
		sMMap.put(56, '.');
		sRMap.put(56, '>');
		sMMap.put(216, '\\');
		sRMap.put(216, '|');
		sMMap.put(70, '^');
		sRMap.put(70, '~');
		sMMap.put(69, '-');
		sRMap.put(69, '=');


		sMMap.put(62, ' ');	
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		log("dispatchKeyEvent"+event.getKeyCode()+","+event.toString());

		char dispatchKey = 0;
		int keycode = event.getKeyCode();
		log("test="+keycode+"s="+event.isShiftPressed());

		// 
		if(KeyEvent.KEYCODE_A <= event.getKeyCode()&&event.getKeyCode()<=KeyEvent.KEYCODE_Z){
			switch(event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				dispatchKey =(char)( 
						(int)'a'+(int)event.getKeyCode()-(int)KeyEvent.KEYCODE_A);
				if(event.isShiftPressed()){
					dispatchKey +='A'-'a';
				}
				CommitText v = new CommitText(""+dispatchKey, 1);
				mCommitTextList.addLast(v);
			}
			return true;
		} 
		else if(KeyEvent.KEYCODE_0<keycode&&keycode <=KeyEvent.KEYCODE_9) {
			switch(event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				dispatchKey =(char)( 
						(int)'0'+(int)event.getKeyCode()-(int)KeyEvent.KEYCODE_0);		
				if(event.isShiftPressed()) {
					dispatchKey += '!'-'0'-1;
				}
				CommitText v = new CommitText(""+dispatchKey, 1);
				mCommitTextList.addLast(v);
			}
			return true;
		}
		else if(event.isShiftPressed()&&sRMap.containsKey(keycode)){
			switch(event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				dispatchKey = sRMap.get(keycode);
				CommitText v = new CommitText(""+dispatchKey, 1);
				mCommitTextList.addLast(v);
			}
			return true;
		}else if(!event.isShiftPressed()&&sMMap.containsKey(keycode)){
			switch(event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				dispatchKey = sMMap.get(keycode);
				CommitText v = new CommitText(""+dispatchKey, 1);
				mCommitTextList.addLast(v);
			}
			return true;
		} else {
			if(keycode == KeyEvent.KEYCODE_ENTER|| keycode==KeyEvent.KEYCODE_DEL) {
				switch(event.getAction()) {
				case KeyEvent.ACTION_DOWN:
//					(event.getKeyCode());
					// foolowing code infinite loop
				mCurrentInputConnection.sendKeyEvent(event);
				}
				return true;

			} 
		}
		
		return super.dispatchKeyEvent(event);
	}
	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		char dispatchKey = 0;

		log("dispatchKeyEventPreIme"+event.getKeyCode()+","+event.toString());

		int ctrl = event.getMetaState();
		if(0x1000==(ctrl&0x1000)){
			mPushingCtl = true;
			switch(event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				// for asus fskaren 
				if(KeyEvent.KEYCODE_A <= event.getKeyCode()&&event.getKeyCode()<=KeyEvent.KEYCODE_Z){
					dispatchKey =(char)( 
							(int)'a'+(int)event.getKeyCode()-(int)KeyEvent.KEYCODE_A);
					CommitText v = new CommitText(""+dispatchKey, 0);
					v.pushingCtrl(pushingCtl());
					mCommitTextList.addLast(v);
					mComposingText = "";
					mCommitText = "";
				}
			}
			// return true reason
			// Kyoro Common manage keyevent. Android IME can not manage.
			return true;
		} else {
			mPushingCtl = false;
			switch(event.getAction()) {
			case KeyEvent.ACTION_DOWN:
				if(event.getKeyCode()==0x72||
				event.getKeyCode()==0x71) {
					mPushingCtl = true;
				}
				break;
			case KeyEvent.ACTION_UP:
				if(event.getKeyCode()==0x72||
				event.getKeyCode()==0x71) {
					mPushingCtl = false;
				}
				break;
			}
			return super.dispatchKeyEventPreIme(event);
		}
	}
	
	private static CharSequence mComposingText = null;
	private static CharSequence mCommitText = null;
	private static LinkedList<CommitText> mCommitTextList = new LinkedList<CommitText>();
	private static int mStart = 0;
	private static int mEnd = 0;
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
			if(mCommitText!=null&&mComposingText.length() != 0) {
				addCommitText(mComposingText, 1);
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
			if(pushingCtl()){//todo
		    	mComposingText = "";
		    	mCommitText = "";
		    	commitText(text, 0);
		    	getEditable().clear();
		    	mManager.restartInput(EditableSurfaceView.this);
				return super.setComposingText("", 0);
			} else {
				mComposingText = text;
				return super.setComposingText(text, newCursorPosition);
			}
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
				addCommitText(text.getText(), text.getPosition());
//				mCommitTextList.addLast(new CommitText(text.getText(), text.getPosition()));
				return super.commitCompletion(text);
			} finally {
				mComposingText = "";
				getEditable().clear();
			}
		}

		public void addCommitText(CharSequence text, int position) {
			CommitText v = new CommitText(text, position);
			v.pushingCtrl(pushingCtl());
			mCommitTextList.addLast(v);			
		}

		public void addKeyEvent(int event) {
			CommitText v = new CommitText(event);
			v.pushingCtrl(pushingCtl());
			mCommitTextList.addLast(v);
		}

		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			log("commitText="+text+","+newCursorPosition);
			log("--1--="+Selection.getSelectionStart(text));
			log("--2--="+Selection.getSelectionEnd(text));
			mCommitText = text;
			CommitText v = new CommitText(text, newCursorPosition);
			v.pushingCtrl(pushingCtl());
			mCommitTextList.addLast(v);
			try{
				return super.commitText(text, newCursorPosition);
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
				// �^�[���ŁAjava.lang.IndexOutOfBoundsException�����������̂ŃK�[�h
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
		public boolean sendKeyEvent(KeyEvent event) {
			log("sendKeyEvent="+event.toString());
//			android.util.Log.v("kiyo","sendKeyEvent="+event.toString());
			if(event.getAction() == KeyEvent.ACTION_DOWN ) {
				addKeyEvent(event.getKeyCode());
//					mCommitTextList.addLast(new CommitText(event.getKeyCode()));
			}
			return true;
//			return super.sendKeyEvent(event);
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

		@Override
		public boolean pushingCtr() {
			return EditableSurfaceView.this.pushingCtl();
		}
		
	}

	public static void log(String log) {
		//android.util.Log.v("kiyo", ""+log);
	}
}
