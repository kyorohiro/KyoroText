package info.kyorohiro.helloworld.android.adapter;

import info.kyorohiro.helloworld.display.simple.MetaState;
import android.view.KeyEvent;

public class MetaStateForAndroid implements MetaState {
	private boolean mPushingCtl = false;
	private boolean mPushingAlt = false;
	private boolean mPushingEsc = false;
	private boolean mPushingShift = false;

	public void clear() {
		mPushingCtl = false;
		mPushingAlt = false;
		mPushingEsc = false;
		mPushingShift = false;		
	}

	public boolean pushingCtl() {
		return mPushingCtl;
	}

	public boolean pushingAlt() {
		return mPushingAlt;
	}

	public boolean pushingEsc() {
		return mPushingEsc;
	}

	public boolean pushingShift() {
		return mPushingShift;
	}

	public static boolean isCtl(int keycode) {
		if (keycode==0x72|| keycode == 0x71){
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEsc(int keycode) {
		if(keycode==111){
			return true;
		} else {
			return false;
		}
	}
	public void update(KeyEvent event) {
		
		//alt
		mPushingAlt = event.isAltPressed();		
		
		// shift 
		mPushingShift = event.isShiftPressed();

		// ctl
		int ctrl = event.getMetaState();
		if(0x1000==(ctrl&0x1000)||0x2000==(ctrl&0x2000)||0x4000==(ctrl&0x4000)){
			mPushingCtl = true;
		} else {
			mPushingCtl = false;			
		}

		//esc
		if(mPushingCtl&&event.isShiftPressed()&&event.getKeyCode() == 71){
			//android.util.Log.v("kiyo","#esc on#");
			mPushingEsc = true;
		} else {
		//	android.util.Log.v("kiyo","#esc off#");
			mPushingEsc = false;			
		}
		if(isEsc(event.getKeyCode())&&event.getAction()==KeyEvent.ACTION_DOWN){
			mPushingEsc = true;
		}
	} 
}
