package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;

public class Manager {
	private int mCurPos = 0;
	private Command[] mList = new Command[KeyEventManager.EMACS_SHORTCUT_BASIC.length];
	private int mLength = 0;
	private KeyEventManager mManager = null;

	public Manager(KeyEventManager manager) {
		mManager = manager;
		clear();
	}

	public static void log(String log) {
		//android.util.Log.v("kiyo", ""+log);
	}

	public boolean useHardKey() {
		if(mCurPos != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean update(char code, boolean ctl, boolean alt,EditableLineView view, EditableLineViewBuffer buffer) {
		int i=0;
		log("#c="+code+",c/a="+ctl+"/"+alt);
		
		for(int j=0;j<mLength;j++) {
			Command c = mList[j];
			if(c.match(mCurPos, code, ctl, alt)){
				if(c.action(mCurPos, view, buffer)) {
					clear();
					return true;
				} else {
					//android.util.Log.v("kiyo","+++");
					mList[i]=c;
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
		mCurPos = 0;
		if(mLength != mManager.numOfCommnad())  {
			mLength = mManager.numOfCommnad();
			mList = new Command[mLength];
			System.arraycopy(KeyEventManager.EMACS_SHORTCUT_BASIC, 0, mList, 0, KeyEventManager.EMACS_SHORTCUT_BASIC.length);
			if(KeyEventManager.EMACS_SHORTCUT_BASIC.length < mLength) {
				System.arraycopy(mManager.getEmacsShortExtra(), 0, mList, KeyEventManager.EMACS_SHORTCUT_BASIC.length, mLength-KeyEventManager.EMACS_SHORTCUT_BASIC.length);
			}
		}
	}
}