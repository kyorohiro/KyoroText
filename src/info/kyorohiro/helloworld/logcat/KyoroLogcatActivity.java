package info.kyorohiro.helloworld.logcat;

import info.kyorohiro.helloworld.logcat.base.TestActivity;
import info.kyorohiro.helloworld.logcat.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.logcat.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.logcat.display.simple.SimpleStage;
import info.kyorohiro.helloworld.logcat.display.widget.CircleController;
import info.kyorohiro.helloworld.logcat.display.widget.LogcatViewer;
import info.kyorohiro.helloworld.logcat.util.CyclingStringList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class KyoroLogcatActivity extends TestActivity {
	public static final String MENU_START_SHOW_LOG = "Start show log";
	public static final String MENU_STOP_SHOW_LOG = "Stop show log";
	public static final String MENU_STOP_SAVE_AT_BGGROUND = "Stop Save at bg";
	public static final String MENU_START_SAVE_AT_BGGROUND = "Start Save at bg";
	public static final String MENU_SEND_MAIL = "send logcat(-d) log mail";
	public static final String MENU_CLEAR_LOG = "clear log";

	private LogcatViewer mLogcatViewer = new LogcatViewer(3000);
	private CyclingStringList mLogcatOutput = mLogcatViewer.getCyclingStringList();
	private CircleController mCircleController = new CircleController();
	private SimpleStage mStage = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.changeTitle(this, "kyoro logcat", Color.parseColor("#cc795514"), Color.parseColor("#e5cf2a"));
		this.changeMenuBgColor(this, Color.parseColor("#e5cf2a"));
		mCircleController.setEventListener(mLogcatViewer.getCircleControllerEvent());
		mStage = new SimpleStage(this.getApplicationContext());
		mStage.getRoot().addChild(new Layout());
		mStage.getRoot().addChild(mLogcatViewer);
		mStage.getRoot().addChild(mCircleController);
		mStage.start();
		setContentView(mStage);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mStage != null) {
			mStage.start();
		}

	}

	@Override
	protected void onPause() {
		if (mStage != null) {
			mStage.stop();
		}
		if (mShowTask != null) {
			mShowTask.terminate();
			mShowTask = null;
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		try {
			if (mStage != null) {
				mStage.stop();
			}
			if (mShowTask != null) {
				mShowTask.terminate();
				mShowTask = null;
			}
		} finally {
			super.onDestroy();
		}
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if(showTaskIsAlive() == true){
			menu.add(MENU_STOP_SHOW_LOG).setIcon(R.drawable.ic_stop);
		}
		else {
			menu.add(MENU_START_SHOW_LOG).setIcon(R.drawable.ic_start);			
		}

		if (KyoroLogcatTaskManagerForSave.saveTaskIsAlive()) {
			menu.add(MENU_STOP_SAVE_AT_BGGROUND).setIcon(
					R.drawable.ic_stop_save);
		} else {
			menu.add(MENU_START_SAVE_AT_BGGROUND).setIcon(
					R.drawable.ic_start_save);
		}

		menu.add(MENU_SEND_MAIL).setIcon(R.drawable.ic_send_mail);

		menu.add(MENU_CLEAR_LOG).setIcon(R.drawable.ic_clear_log);

		return true;
	}

	private ShowCurrentLogTask mShowTask = null;
	private boolean showTaskIsAlive() {
		if(mShowTask == null || !mShowTask.isAlive()){
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		boolean parentResult = super.onMenuItemSelected(featureId, item);
		if (parentResult == true || item == null) {
			return parentResult;
		}
		CharSequence selectedItemTitle = item.getTitle();
		if (selectedItemTitle == null) {
			return parentResult;
		}

		boolean myResult = false;
		if (MENU_START_SAVE_AT_BGGROUND.equals(selectedItemTitle)) {
			KyoroLogcatTaskManagerForSave.startSaveTask(this);
			myResult = true;
		} else if (MENU_STOP_SAVE_AT_BGGROUND.equals(selectedItemTitle)) {
			KyoroLogcatTaskManagerForSave.stopSaveTask();
			myResult = true;
		} else if (MENU_START_SHOW_LOG.equals(selectedItemTitle)) {
			if(mShowTask == null || !mShowTask.isAlive()){
				mShowTask = new ShowCurrentLogTask(mLogcatOutput, "-v time");
				mShowTask.start();
			}
			myResult = true;
		} else if (MENU_STOP_SHOW_LOG.equals(selectedItemTitle)) {
			if(mShowTask != null || mShowTask.isAlive()){
				mShowTask.terminate();
			}
			mShowTask = null;
			myResult = true;
		} else if(MENU_SEND_MAIL.equals(selectedItemTitle)){
			SendCurrentLogTask task = new SendCurrentLogTask(this);
			task.start();
		} else if(MENU_CLEAR_LOG.equals(selectedItemTitle)){
			ClearCurrentLogTask task = new ClearCurrentLogTask(mLogcatOutput);
			task.start();
		}
		return myResult;
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			int x = graphics.getWidth() - mCircleController.getWidth();
			int y = graphics.getHeight() - mCircleController.getHeight();
			KyoroLogcatActivity.this.mCircleController.setPoint(x, y);
		}
	}

}