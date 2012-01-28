package info.kyorohiro.helloworld.logcat;

import info.kyorohiro.helloworld.android.base.TestActivity;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.logcat.logcat.LogcatCyclingLineDataList;
import info.kyorohiro.helloworld.logcat.logcat.LogcatViewer;
import info.kyorohiro.helloworld.util.SimpleFileExplorer;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KyoroLogcatActivity extends TestActivity {
	public static final String MENU_START_SHOW_LOG = "Start show log";
	public static final String MENU_START_SHOW_LOG_FROM_FILE = "show log from file";
	public static final String MENU_START_SHOW_LOG_FROM_SHELL = "show log from logcat";

	public static final String MENU_STOP_SHOW_LOG = "Stop show log";
	public static final String MENU_STOP_SAVE_AT_BGGROUND = "Stop Save at bg";
	public static final String MENU_START_SAVE_AT_BGGROUND = "Start Save at bg";
	public static final String MENU_SEND_MAIL = "send logcat(-d) log mail";
	public static final String MENU_CLEAR_LOG = "clear log";

	private LogcatViewer mLogcatViewer = new LogcatViewer(3000);
	private LogcatCyclingLineDataList mLogcatOutput = mLogcatViewer.getCyclingStringList();
	private SimpleCircleController mCircleController = new SimpleCircleController();
	private SimpleStage mStage = null;
	private EditText mInputForLogFilter = null; 

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
		
		mInputForLogFilter = new EditText(this);
		mInputForLogFilter.setSelected(false);
		mInputForLogFilter.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		mInputForLogFilter.setHint("Filter regex(find)");
		mInputForLogFilter.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		mInputForLogFilter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// All IME Application take that actionId become imeoption's value.
				mLogcatViewer.startFilter(mInputForLogFilter.getText().toString());
				return false;
			}
		});

		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rootLayout.addView(mInputForLogFilter,params);
		rootLayout.addView(mStage);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		setContentView(rootLayout);
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
			SubMenu showSubMenu = 
				menu.addSubMenu(MENU_START_SHOW_LOG);
			showSubMenu.setIcon(R.drawable.ic_start);
			showSubMenu.add(MENU_START_SHOW_LOG_FROM_FILE);
			showSubMenu.add(MENU_START_SHOW_LOG_FROM_SHELL);
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
			KyoroLogcatTaskManagerForSave.startSaveTask(this.getApplicationContext());
			myResult = true;
		} else if (MENU_STOP_SAVE_AT_BGGROUND.equals(selectedItemTitle)) {
			KyoroLogcatTaskManagerForSave.stopSaveTask(this.getApplicationContext());
			myResult = true;
		} else if (MENU_START_SHOW_LOG.equals(selectedItemTitle)) {
			if(mShowTask == null || !mShowTask.isAlive()){
				mShowTask = new ShowCurrentLogTask(mLogcatOutput, "-v time");
				mShowTask.start();
			}
			myResult = true;
		} else if(MENU_START_SHOW_LOG_FROM_FILE.equals(selectedItemTitle)) {
			runOnUiThread(new Runnable() {				
				public void run() {
					 Dialog  dialog = SimpleFileExplorer.createDialog(KyoroLogcatActivity.this, Environment.getExternalStorageDirectory());
						 //SiimpleFileReader.createDialog(KyoroLogcatActivity.this);
					 dialog.show();
				}
			});
		}
		else if (MENU_STOP_SHOW_LOG.equals(selectedItemTitle)) {
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