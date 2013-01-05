package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.IMEController;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleKeyEvent;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Command.CommandPart;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.OtherWindow;


public class KeyEventManager extends IMEController{
	private ShortcutStateMachine mManager = null;
	private CharSequence mCurrentMode = "BASIC";
	public KeyEventManager() {
		init();
	}

	public void onUpdate(CharSequence mode) {
	//	android.util.Log.v("kiyo","#mode="+mode);
	}

	public void setMode(CharSequence mode) {
		if(!mCurrentMode.equals(mode)){
			onUpdate(mode);
		}
		mCurrentMode = mode;
	}

	public CharSequence getMode() {
		return mCurrentMode;
	}

	protected void init() {
		mManager = new ShortcutStateMachine(EMACS_SHORTCUT_BASIC);
	}

	public ShortcutStateMachine getManager() {
		return mManager;
	}

	@Override
	public boolean tryUseBinaryKey(int keycode, boolean shift, boolean ctl, boolean alt) {
		if(mManager.useHardKey()) {
			return true;
		} else {
			return super.tryUseBinaryKey(keycode, shift, ctl, alt);
		}
	}

	public static Command[] EMACS_SHORTCUT_BASIC =  {
		new Command(new CommandPart[]{new CommandPart('g', true, false)}, null),
		new Command(new CommandPart[]{new CommandPart('a', true, false)}, new BeginningOfLine()),
		new Command(new CommandPart[]{new CommandPart('e', true, false)}, new EndOfLine()),
		new Command(new CommandPart[]{new CommandPart('n', true, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart('p', true, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart('f', true, false)}, new FowardWord()),
		new Command(new CommandPart[]{new CommandPart('b', true, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart('h', true, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart('d', true, false)}, new DeleteBackwardChar()),
		new Command(new CommandPart[]{new CommandPart('l', true, false)}, new Recenter()),
		new Command(new CommandPart[]{new CommandPart('k', true, false)}, new KillLine()),
		new Command(new CommandPart[]{new CommandPart('y', true, false)}, new Yank()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart((char)0x1b, false, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('<', false, true)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('>', false, true)}, new EndOfBuffer()),
		//
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_DOWN, false, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_UP, false, false)}, new BackwardWord()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_LEFT, false, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DPAD_RIGHT, false, false)}, new FowardWord()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_DEL, false, false)}, new DeleteChar()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_ENTER, false, false)}, new CrlfTask()),
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_SPACE, false, false)}, new SingleByteSpaceTask()),	
		new Command(new CommandPart[]{new CommandPart(SimpleKeyEvent.KEYCODE_TAB, false, false)}, new SingleByteTabTask()),	

	};

	private static Command[] EMACS_SHORTCUT_EXTRA = {
	};

	public Command[] getEmacsShortExtra() {
		return EMACS_SHORTCUT_EXTRA;
	}
	public int numOfCommnad() {
		return EMACS_SHORTCUT_EXTRA.length + EMACS_SHORTCUT_BASIC.length; 
	}
	
	public void setExtraCommand(Command[] extra) {
		if(extra != null) {
			EMACS_SHORTCUT_EXTRA = extra;
		}
	}

	public void updateCommitTextFromIME(EditableLineView mTextView, EditableLineViewBuffer mTextBuffer) {
		// following code is yaxutuke sigoto
		if (!mTextView.isFocus()) {
			return;
		}
		setMode(mTextView.getMode());
		mTextView.getMyInputConnection().setIMEController(KeyEventManager.this);
		MyInputConnection c = mTextView.getMyInputConnection();
		if (c == null) {
			return;
		}

		mTextBuffer.IsCrlfMode(mTextView.isCrlfMode());
	
		boolean first = true;
		while (true) {
			CommitText text = c.popFirst();
			if (text != null) {
				mTextView.getStage(mTextView).resetTimer();
				if(first == true) {
					cursorIsInShowingView(mTextView);
					first = false;
				}
				// android.util.Log.v("kiyo","key= #");
				mTextBuffer.setCursor(mTextView.getLeft().getCursorRow(), mTextView.getLeft().getCursorCol());
				a(text, mTextView, mTextBuffer);
				mTextView.getLeft().setCursorRow(mTextBuffer.getRow());
				mTextView.getLeft().setCursorCol(mTextBuffer.getCol());
			} else {
				break;
			}
		}
	}

	private void cursorIsInShowingView(EditableLineView v) {
		int s = v.getShowingTextStartPosition();
		int e = v.getShowingTextEndPosition();
		int c = v.getLeft().getCursorCol();
		if(!(s<=c&&c<=e)) {
			v.recenter();
		}
	}
	 private void a(CommitText text, EditableLineView mTextView, EditableLineViewBuffer mTextBuffer) {
		// android.util.Log.v("kiyo","#key= #");
//		 android.util.Log.v("kiyo","#key= #"+text.getText().length()
//				 +","+text.getText().charAt(0)+","
//				 +text.pushingCtrl()+","+ text.pushingAlt());
		if(text.getText()!=null&&text.getText().length()==1&&mManager.update(text.getText().charAt(0),
				text.pushingCtrl(), text.pushingAlt(), mTextView, mTextBuffer)) {
//			android.util.Log.v("kiyo","key= ++");
			return;
		} else {
//			android.util.Log.v("kiyo","key= --");
			mManager.clear();
		}
		mTextBuffer.clearYank();
		if (text.isKeyCode()) {
			//android.util.Log.v("kiyo","#key  --1-- ="+text.getKeyCode());
			mManager.update(text.getKeyCode(), text.pushingCtrl(), text.pushingAlt(), mTextView, mTextBuffer);
		} else {
			//android.util.Log.v("kiyo","#key  --2-- ="+text.getText());
			if(getMode().toString().startsWith(CursorableLineView.MODE_EDIT)) {
				mTextBuffer.pushCommit(text.getText(),
						text.getNewCursorPosition());
			}
		}
	 }
	 //android.util.Log.v("kiyo","#key= end");

	 public interface Task {
		 public String getCommandName();
		 public void act(EditableLineView view, EditableLineViewBuffer buffer);
	 }
 

}
