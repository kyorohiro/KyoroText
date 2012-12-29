package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class Command {
	private CommandPart[] mCommand = new CommandPart[0];
	private Task mAction = null;

	public Command(CommandPart[] c, Task action) {
		mCommand = c;
		mAction = action;
	}

	public Task getTask() {
		return mAction;
	}

	public boolean action(int index, EditableLineView view, EditableLineViewBuffer buffer) {
//		android.util.Log.v("kiyo","==2A=1="+index+","+mCommand.length);
		if (index + 1 == mCommand.length) {
			if (mAction != null) {
//				android.util.Log.v("kiyo","==2A=2=");
				mAction.act(view, buffer);
			}
//			android.util.Log.v("kiyo","==2A=3=");
			return true;
		} else {
//			android.util.Log.v("kiyo","==2A=4=");
			return false;
		}
	}

	public boolean match(int index, int keycode, boolean ctl, boolean alt) {
		if (index < mCommand.length) {
			return mCommand[index].match(keycode, ctl, alt);
		} else {
			return false;
		}
	}

	public boolean match(int index, char c, boolean ctl, boolean alt) {
		if (index < mCommand.length) {
			return mCommand[index].match(c, ctl, alt);
		} else {
			return false;
		}
	}

	public static class CommandPart {
		private char mC = ' ';
		private boolean mCtl = false;
		private boolean mAlt = false;
		private boolean mIsKeycode = false;
		private int mKeycode = 0;

		public CommandPart(char c, boolean ctl, boolean alt) {
			mC = c;
			mCtl = ctl;
			mAlt = alt;
			mIsKeycode = false;
		}

		public CommandPart(int keycode, boolean ctl, boolean alt) {
			mIsKeycode = true;
			mCtl = ctl;
			mAlt = alt;
			mKeycode = keycode;
		}

		public boolean match(int keycode, boolean ctl, boolean alt) {
//			android.util.Log.v("kiyo","==1=="+keycode+","+mKeycode);
			if(!mIsKeycode) {
				return false;
			}
			if (keycode == mKeycode && mCtl == ctl && mAlt == alt) {
//				android.util.Log.v("kiyo","==2A=="+keycode+","+mKeycode);
				return true;
			} else {
//				android.util.Log.v("kiyo","==2B=="+keycode+","+mKeycode);
				return false;
			}
		}

		public boolean match(char c, boolean ctl, boolean alt) {
			if(mIsKeycode) {
				return false;
			}
			if (c == mC && mCtl == ctl && mAlt == alt) {
				return true;
			} else {
				return false;
			}
		}
	}
}
