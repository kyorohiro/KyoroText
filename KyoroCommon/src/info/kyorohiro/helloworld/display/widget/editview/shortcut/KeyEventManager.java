package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import java.nio.Buffer;

import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.IMEController;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleKeyEvent;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.Command.CommandPart;
import info.kyorohiro.helloworld.text.KyoroString;

public class KeyEventManager extends IMEController{


	@Override
	public boolean tryUseBinaryKey(boolean shift, boolean ctl, boolean alt) {
		if(mManager.useHardKey()) {
			return true;
		} else {
			return super.tryUseBinaryKey(shift, ctl, alt);
		}
	}

	public static Command[] EMACS_SHORTCUT = {
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
	};


	private Manager mManager = new Manager();
	public void updateCommitTextFromIME(EditableLineView mTextView,
			EditableLineViewBuffer mTextBuffer) {
		// following code is yaxutuke sigoto
		if (!mTextView.isFocus()) {
			return;
		}
		mTextView.getMyInputConnection().setIMEController(KeyEventManager.this);
		MyInputConnection c = mTextView.getMyInputConnection();
		if (c == null) {
			return;
		}

		mTextBuffer.IsCrlfMode(mTextView.isCrlfMode());
		mTextView.getStage(mTextView).start();
	
		while (true) {
			CommitText text = c.popFirst();
			if (text != null) {
//				 android.util.Log.v("kiyo","key= #");
				a(text, mTextView, mTextBuffer);
			} else {
				break;
			}
		}
	}

	 private void a(CommitText text, EditableLineView mTextView, EditableLineViewBuffer mTextBuffer) {
//		 android.util.Log.v("kiyo","key= #"+text.getText().length()
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
//			android.util.Log.v("kiyo","key="+text.getKeyCode());
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
				mTextBuffer.setCursor(mTextView.getLeft()
						.getCursorRow(), mTextView.getLeft()
						.getCursorCol());
				break;
			case SimpleKeyEvent.KEYCODE_DPAD_RIGHT:
				mTextView.front();
				mTextBuffer.setCursor(mTextView.getLeft()
						.getCursorRow(), mTextView.getLeft()
						.getCursorCol());
				break;
			case SimpleKeyEvent.KEYCODE_DPAD_UP:
				mTextView.prev();
				mTextBuffer.setCursor(mTextView.getLeft()
						.getCursorRow(), mTextView.getLeft()
						.getCursorCol());
				break;
			case SimpleKeyEvent.KEYCODE_DPAD_DOWN:
				mTextView.next();
				mTextBuffer.setCursor(mTextView.getLeft()
						.getCursorRow(), mTextView.getLeft()
						.getCursorCol());
				break;
			case SimpleKeyEvent.KEYCODE_SPACE:
				mTextBuffer.pushCommit(" ", 1);
				break;
			}
		} else {
//			android.util.Log.v("kiyo","key= -");
			mTextBuffer.pushCommit(text.getText(),
					text.getNewCursorPosition());
		}
	}
	 
	 public interface Task {
		 public String getCommandName();
		 public void act(EditableLineView view, EditableLineViewBuffer buffer);
	 }
 

}
