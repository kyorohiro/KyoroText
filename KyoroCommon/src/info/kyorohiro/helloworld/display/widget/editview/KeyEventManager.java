package info.kyorohiro.helloworld.display.widget.editview;

import android.widget.TextView;
import info.kyorohiro.helloworld.display.simple.CommitText;
import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleKeyEvent;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CyclingListForAsyncDuplicate.Task;

public class KeyEventManager {

	public static class CommandPart {
		private char mC = ' ';
		private boolean mCtl = false;
		private boolean mAlt = false;

		public CommandPart(char c, boolean ctl, boolean alt) {
			mC = c;
			mCtl = ctl;
			mAlt = alt;
		}

		public boolean match(char c, boolean ctl, boolean alt) {
			if (c == mC && mCtl == ctl && mAlt == alt) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static class Command {
		private CommandPart[] mCommand = new CommandPart[0];
		private Task mAction = null;

		public Command(CommandPart[] c, Task action) {
			mCommand = c;
			mAction = action;
		}
		
		public boolean action(int index, EditableLineView view, EditableLineViewBuffer buffer) {
			if(index+1==mCommand.length) {
				for(CommandPart p : mCommand) {
					android.util.Log.v("kiyo","k="+p.toString());
				}
				if(mAction != null) {
					mAction.act(view, buffer);
				}
				return true;
			} else {
				return false;
			}
		}

		public boolean match(int index,char c, boolean ctl, boolean alt) {
			if(index<mCommand.length) {
				return mCommand[index].match(c, ctl, alt);	
			} else {
				return false;
			}
		}
	}

	public static Command[] EMACS_SHORTCUT = {
		new Command(new CommandPart[]{new CommandPart('g', true, false)}, null),
		new Command(new CommandPart[]{new CommandPart('a', true, false)}, new BeginningOfLine()),
		new Command(new CommandPart[]{new CommandPart('e', true, false)}, new EndOfLine()),
		new Command(new CommandPart[]{new CommandPart('n', true, false)}, new NextLine()),
		new Command(new CommandPart[]{new CommandPart('p', true, false)}, new PreviousLine()),
		new Command(new CommandPart[]{new CommandPart('f', true, false)},null),
		new Command(new CommandPart[]{new CommandPart('b', true, false)},null),
		new Command(new CommandPart[]{new CommandPart('h', true, false)},null),
		new Command(new CommandPart[]{new CommandPart('d', true, false)},null),
		new Command(new CommandPart[]{new CommandPart('l', true, false)},null),
		new Command(new CommandPart[]{new CommandPart('k', true, false)},null),

		new Command(new CommandPart[]{new CommandPart('{', true, false), new CommandPart('<', false, false)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('}', true, false), new CommandPart('>', false, false)}, new EndOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('<', false, true)}, new BeginningOfBuffer()),
		new Command(new CommandPart[]{new CommandPart('>', false, true)}, new EndOfBuffer()),
	};

	public static class Manager {
		private int mCurPos = 0;
		private Command[] mList = new Command[EMACS_SHORTCUT.length];
		private int mLength = 0;

		public Manager() {
			clear();
		} 
		public boolean update(char code, boolean ctl, boolean alt,EditableLineView view, EditableLineViewBuffer buffer) {
			int i=0;
			for(int j=0;j<mLength;j++) {
				Command c = mList[j];
				if(c.match(mCurPos, code, ctl, alt)){
					if(c.action(mCurPos, view, buffer)) {
						clear();
						return true;
					} else {
						mList[i]=c;
						i++;
					}
				}
			}
			mLength = i;
			if(mLength == 0) {
				return false;
			} else {
				mCurPos++;
				return true;
			}
		}
		public void clear() {
			mCurPos = 0;
			if(mLength != EMACS_SHORTCUT.length)  {
				mLength = EMACS_SHORTCUT.length;
				System.arraycopy(EMACS_SHORTCUT, 0, mList, 0, mLength);
			}
		}
	}


	private Manager mManager = new Manager();
	public void updateCommitTextFromIME(EditableLineView mTextView,
			EditableLineViewBuffer mTextBuffer) {
		// following code is yaxutuke sigoto
		if (!mTextView.isFocus()) {
			return;
		}
		MyInputConnection c = mTextView.getMyInputConnection();
		if (c == null) {
			return;
		}

		mTextBuffer.IsCrlfMode(mTextView.isCrlfMode());
		mTextView.getStage(mTextView).start();
	
		while (true) {
			CommitText text = c.popFirst();
			if (text != null) {
				a(text, mTextView, mTextBuffer);
			} else {
				break;
			}
		}
	}

	 private void a(CommitText text, EditableLineView mTextView, EditableLineViewBuffer mTextBuffer) {
		 android.util.Log.v("kiyo","key= #"+text.getText().length()
				 +","+text.getText().charAt(0)+","
				 +text.pushingCtrl()+","+ text.pushingAlt());
		if(text.getText().length()==1&&mManager.update(text.getText().charAt(0),
				text.pushingCtrl(), text.pushingAlt(), mTextView, mTextBuffer)) {
			android.util.Log.v("kiyo","key= -----");
			return;
		} else {
			mManager.clear();
		}
		if (text.isKeyCode()) {
			android.util.Log.v("kiyo","key="+text.getKeyCode());
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
			mTextBuffer.pushCommit(text.getText(),
					text.getNewCursorPosition());
		}
	}
	 
	 public interface Task {
		 public String getCommandName();
		 public void act(EditableLineView view, EditableLineViewBuffer buffer);
	 }

	 public static class BeginningOfBuffer implements Task {
		@Override
		public String getCommandName() {
			return "beginning-of-buffer";
		}
		@Override
		public void act(EditableLineView view, EditableLineViewBuffer buffer) {
			buffer.setCursor(0, 0);
			view.setCursorAndCRLF(view.getLeft(), 0, 0);
			view.recenter();				
		}
	 }

	 public static class EndOfBuffer implements Task {

		@Override
		public String getCommandName() {
			return "end-of-buffer";
		}

		@Override
		public void act(EditableLineView view, EditableLineViewBuffer buffer) {
			int len = buffer.getNumberOfStockedElement();
			KyoroString ks = null;
			if (len - 1 >= 0) {
				ks = buffer.get(len - 1);
			}
			if (ks != null) {
				buffer.setCursor(
						ks.lengthWithoutLF(view
								.isCrlfMode()), len - 1);
				view.setCursorAndCRLF(view.getLeft(),
						buffer.getRow(),
						buffer.getCol());
			}
			view.recenter();
		}
		 
	 }
	 public static class BeginningOfLine implements Task {
		@Override
		public String getCommandName() {
			return "beginning-of-line";
		}
		@Override
		public void act(EditableLineView view, EditableLineViewBuffer buffer) {
			buffer.setCursor(0, buffer.getCol());
			view.getLeft().setCursorCol(buffer.getCol());
			view.getLeft().setCursorRow(0);
		}
	 }

	 public static class EndOfLine implements Task {
		@Override
		public String getCommandName() {
			return "end-of-line";
		}
		@Override
		public void act(EditableLineView view, EditableLineViewBuffer buffer) {
			int _c = buffer.getCol();
			CharSequence _t = buffer.get(_c);
			buffer.setCursor(_t.length(), _c);
		}
	 }

	 public static class NextLine implements Task {
		@Override
		public String getCommandName() {
			return "next-line";
		}
		@Override
		public void act(EditableLineView view, EditableLineViewBuffer buffer) {
			view.next();
			buffer.setCursor(view.getLeft()
					.getCursorRow(), view.getLeft()
					.getCursorCol());
		}
	 }

	 public static class PreviousLine implements Task {
		@Override
		public String getCommandName() {
			return "previous-line";
		}
		@Override
		public void act(EditableLineView view, EditableLineViewBuffer buffer) {
			view.prev();
			buffer.setCursor(view.getLeft().getCursorRow(), view.getLeft().getCursorCol());
		}
	 }


	 
}
