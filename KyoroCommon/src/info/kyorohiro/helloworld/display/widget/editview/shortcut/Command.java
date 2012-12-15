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
	
	public boolean action(int index, EditableLineView view, EditableLineViewBuffer buffer) {
		if(index+1==mCommand.length) {
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
}
