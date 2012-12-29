package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;

public class ShortcutStateMachine {
	private int mCurPos = 0;
	private Command[] mBase = new Command[0];
	private int mBaseLength = 0;
	private Command[] mWorkList = new Command[KeyEventManager.EMACS_SHORTCUT_BASIC.length];
	private int mLength = 0;

	public ShortcutStateMachine(Command[] base) {
		updateCommnad(base);
		clear();
	}

	public static void log(String log) {
		//android.util.Log.v("kiyo", ""+log);
	}

	public Command getBase(int i) {
		return mBase[i];
	}

	public int getBaseLength() {
		return mBaseLength;
	}

	public Command getWork(int i) {
		return mWorkList[i];
	}

	public int getWorkList() {
		return mLength;
	}
	

	public synchronized void updateCommnad(Command[] newCommand) {
		mCurPos = 0;
		mBaseLength = newCommand.length;
		mLength = mBaseLength;
		if(mBase.length < mBaseLength) {
			mBase = new Command[mBaseLength];
		}
		if(mWorkList.length < mBaseLength) {
			mWorkList = new Command[mBaseLength];
		}
		System.arraycopy(newCommand, 0, mBase, 0, mBaseLength);
		System.arraycopy(newCommand, 0, mWorkList, 0, mLength);
	}

	public boolean useHardKey() {
		if(mCurPos != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(int keycode, boolean ctl, boolean alt,EditableLineView view, EditableLineViewBuffer buffer) {		//
		clear();
		for(int j=0;j<mLength;j++) {
			Command c = mWorkList[j];
			if(c.match(0, keycode, ctl, alt)){
				if(c.action(0, view, buffer)) {
					clear();
					return true;
				} 
				return true;
			}
		}
		return false;
	}

	public boolean update(char code, boolean ctl, boolean alt,EditableLineView view, EditableLineViewBuffer buffer) {
		int i=0;
		log("#c="+code+",c/a="+ctl+"/"+alt);
		
		for(int j=0;j<mLength;j++) {
			Command c = mWorkList[j];
			if(c.match(mCurPos, code, ctl, alt)){
				if(c.action(mCurPos, view, buffer)) {
					clear();
					return true;
				} else {
					mWorkList[i]=c;
					i++;
				}
			}
		}
		mLength = i;
		//android.util.Log.v("kiyo","len="+mLength+","+mCurPos);
		if(mLength == 0) {
			clear();
			return false;
		} else {
			mCurPos++;
			return true;
		}
	}

	public void clear() {
		if(mCurPos != 0) {
			mCurPos = 0;
			mLength = mBaseLength;
			System.arraycopy(mBase, 0, mWorkList, 0, mBaseLength);
		}
	}
}