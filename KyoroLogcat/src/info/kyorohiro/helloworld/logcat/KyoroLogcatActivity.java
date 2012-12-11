package info.kyorohiro.helloworld.logcat;

import java.io.File;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleStageForAndroid;
import info.kyorohiro.helloworld.pfdep.android.base.TestActivity;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleController;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus;
import info.kyorohiro.helloworld.display.simple.sample.SimpleCircleControllerMenuPlus.CircleMenuItem;
import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.logcat.util.LogcatViewer;
import info.kyorohiro.helloworld.logcat.widget.KyoroSaveWidget;
import info.kyorohiro.helloworld.logcat.appparts.PreferenceFontSizeDialog;
import info.kyorohiro.helloworld.logcat.appparts.PreferenceLogcatDirectoryDialog;
import info.kyorohiro.helloworld.logcat.appparts.PreferenceLogcatFilenameDialog;
import info.kyorohiro.helloworld.logcat.appparts.PreferenceLogcatOptionDialog;
import info.kyorohiro.helloworld.logcat.tasks.ClearCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.DumpCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.SaveCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.TaskInter;
import info.kyorohiro.helloworld.logcat.tasks.TaskManagerForSave;
import info.kyorohiro.helloworld.logcat.tasks.SendCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.ShowCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.ShowFileContentTask;
import info.kyorohiro.helloworld.pfdep.android.util.IntentActionDialog;
import info.kyorohiro.helloworld.pfdep.android.util.SimpleFileExplorer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KyoroLogcatActivity extends TestActivity {
	public static final String MENU_START_SHOW_LOG = "Start show log";
	public static final String MENU_START_SHOW_LOG_FROM_FILE = "open file(now test!!)";
	public static final String MENU_STOP_SHOW_LOG = "Stop show log";
	public static final String MENU_STOP_SAVE_AT_BGGROUND = "Stop Save at bg";
	public static final String MENU_START_SAVE_AT_BGGROUND = "Start Save at bg";
	public static final String MENU_SEND_MAIL = "send logcat(-d) log mail";
	public static final String MENU_CLEAR_LOG = "clear logcat(-c) log";
	public static final String MENU_SETTING = "setting";
	public static final String MENU_SETTING_LOGCAT_OPTION = "logcat option";
	public static final String MENU_SETTING_LOGCAT_FILENAME = "filename";
	public static final String MENU_SETTING_FONT_SIZE = "font size";
	public static final String MENU_SETTING_SAVEDDIR = "directory";
	public static final String MENU_SAVE = "save logcat(-d)";

	private LogcatViewer mLogcatViewer = null;
	private FlowingLineBuffer mLogcatOutput = null;
	private SimpleCircleControllerMenuPlus mCircleController
	= new SimpleCircleControllerMenuPlus();
//	private SimpleCircleController mCircleController
//	= new SimpleCircleController();
	private SimpleStageForAndroid mStage = null;
	private AutoCompleteTextView mInputForLogFilter = null;
	private TaskInter mShowTask = null;

	public static String EXTRA_PROPERTY_ACTION = "info.kyorohiro.helloworld.logcat.Action";
	public static String EXTRA_PROPERTY_ACTION_VALUE_FOLDER = "info.kyorohiro.helloworld.logcat.Folder";

	public static void startActivityFormFolder(Context context) {
		Intent intent = new Intent();
		intent.setClassName(KyoroLogcatActivity.class.getPackage().getName(),
				KyoroLogcatActivity.class.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		KyoroLogcatCash.startTaskToShowFolder();
		context.startActivity(intent);
	}

	public static void startActivityFormStartSaveDialog(Context context) {
		Intent intent = new Intent();
		intent.setClassName(KyoroLogcatActivity.class.getPackage().getName(),
				KyoroLogcatActivity.class.getName());
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		KyoroLogcatCash.startTaskToStartSaveTask();
		context.startActivity(intent);
	}

	private boolean showTaskIsAlive() {
		if (mShowTask == null || !mShowTask.isAlive()) {
			return false;
		} else {
			return true;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//

		//Debug.waitForDebugger();
		//
		// circle controller
		int deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
		int deviceHeight = getWindowManager().getDefaultDisplay().getHeight();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		float xdpi = metric.xdpi;
		int radius = (int) (xdpi / 2);
		int deviceMinEdge = deviceWidth;
		if (deviceWidth < deviceHeight) {
			deviceMinEdge = deviceWidth;
		} else {
			deviceMinEdge = deviceHeight;
		}
		if (radius > deviceMinEdge / 1.5) {
			radius = (int) (deviceMinEdge / 1.5);
		}

		mLogcatViewer = new LogcatViewer(deviceMinEdge);
		mLogcatOutput = mLogcatViewer.getCyclingStringList();

		mCircleController.setEventListener(mLogcatViewer
				.getCircleControllerAction());
		mCircleController.setRadius(radius);
		mCircleController.setCircleMenuItem(new CircleMenuItem() {
			@Override
			public boolean selected(int id, String title) {
				if(!(mLogcatViewer.getLineView() instanceof CursorableLineView)){
					return false;
				}
				CursorableLineView lineview = (CursorableLineView)mLogcatViewer.getLineView();
				switch(id) {
				case 0:
					lineview.setMode(CursorableLineView.MODE_VIEW);
					mCircleController.clearCircleMenu();
					mCircleController.addCircleMenu(0, "view mode");
					mCircleController.addCircleMenu(1, "select mode");
					break;
				case 1:
					lineview.setMode(CursorableLineView.MODE_SELECT);
					mLogcatViewer.getLineView();
					mCircleController.clearCircleMenu();
					mCircleController.addCircleMenu(0, "view mode");
					mCircleController.addCircleMenu(1, "select mode");
					mCircleController.addCircleMenu(2, "copy");
					break;
				case 2:
					mCircleController.clearCircleMenu();
					mCircleController.addCircleMenu(0, "view mode");
					mCircleController.addCircleMenu(1, "select mode");
					KyoroApplication.showMessage(lineview.copy());
					KyoroApplication.copyToClipboard(lineview.copy());
					lineview.setMode(CursorableLineView.MODE_VIEW);
//					mCircleController.addCircleMenu(2, "copy");
					break;
				}
				return false;
			}
		});
		mCircleController.clearCircleMenu();
		mCircleController.addCircleMenu(0, "view mode");
		mCircleController.addCircleMenu(1, "select mode");

		//
		// simple stage
		mStage = new SimpleStageForAndroid(this.getApplicationContext());
		mStage.getRoot().addChild(new Layout());
		mStage.getRoot().addChild(mLogcatViewer);
		mStage.getRoot().addChild(mCircleController);
		mStage.start();

		//
		// edit text
		mInputForLogFilter = new AutoCompleteTextView(this);
		mInputForLogFilter.setSelected(false);
		mInputForLogFilter.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		mInputForLogFilter.setHint("Filter regex(find)");
		mInputForLogFilter.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		FilterSettingAction f = new FilterSettingAction();
		mInputForLogFilter.setOnEditorActionListener(f);
		mInputForLogFilter.setText(KyoroLogcatSetting.getCurrentFind());
		ArrayAdapter<String> automatedStrage = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, new String[] {
						"[DIVWEFS]/", " [DIVWEFS]/", ".[DIVWEFS]/", " D/",
						" I/", " V/", " W/", " E/", " F/", " S/", "D/", "I/",
						"V/", "W/", "E/", "F/", "S/", "[DIV]/", "[IVW]/",
						"[VWE]/", "[WEF]/", "[EFS]/" });
		mInputForLogFilter.setAdapter(automatedStrage);
		mInputForLogFilter.setThreshold(1);
		ImageButton b = new ImageButton(this);
		b.setOnClickListener(f);
		b.setImageResource(android.R.drawable.ic_media_play);
		//mInputForLogFilter.setHeight(b.getHeight());

		LinearLayout h = new LinearLayout(this);
		h.setOrientation(LinearLayout.HORIZONTAL);
		h.addView(b);
		h.addView(mInputForLogFilter, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		//
		// set layout
		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		// rootLayout.addView(mInputForLogFilter, params);
		rootLayout.addView(h, params);
		rootLayout.addView(mStage);

		// set content
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
								| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(rootLayout);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mStage != null) {
			mStage.start();
		}
		if (KyoroLogcatCash.startTaskIsShowFolder()) {
			startFolder(300);
			KyoroLogcatCash.startTaskToNone();
		} else if (KyoroLogcatCash.startTaskIsStartSave()) {
			startSaveLog();
			KyoroLogcatCash.startTaskToNone();
		}
	}

	@Override
	protected void onPause() {
		if (mStage != null) {
			mStage.stop();
		}
		if (mShowTask != null) {
			stopShowLog();
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
				stopShowLog();
			}
		} finally {
			super.onDestroy();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (showTaskIsAlive() == true) {
			menu.add(MENU_STOP_SHOW_LOG).setIcon(R.drawable.ic_stop);
		} else {
			menu.add(MENU_START_SHOW_LOG).setIcon(R.drawable.ic_start);
		}

		// todo Ç»ÇÒÇ©âòÇ¢ÅAwidget Ç∆ã§í Ç…Ç∑ÇÈ
		if (TaskManagerForSave.saveTaskIsForceKilled()) {
			menu.add(MENU_START_SAVE_AT_BGGROUND).setIcon(
					R.drawable.ic_retry_save);
		} else if (TaskManagerForSave.saveTaskIsAlive()) {
			menu.add(MENU_STOP_SAVE_AT_BGGROUND).setIcon(
					R.drawable.ic_stop_save);
		} else {
			menu.add(MENU_START_SAVE_AT_BGGROUND).setIcon(
					R.drawable.ic_start_save);
		}

		menu.add(MENU_SEND_MAIL).setIcon(R.drawable.ic_send_mail);
		menu.add(MENU_CLEAR_LOG).setIcon(R.drawable.ic_clear_log);

		if (!showTaskIsAlive()) {
			menu.add(MENU_START_SHOW_LOG_FROM_FILE).setIcon(
					R.drawable.ic_folder);
		}
		{
			SubMenu menuSetting = menu.addSubMenu(MENU_SETTING);
			menuSetting.setIcon(android.R.drawable.ic_menu_preferences);
			menuSetting.add(MENU_SETTING_LOGCAT_OPTION);
			menuSetting.add(MENU_SETTING_FONT_SIZE);
			menuSetting.add(MENU_SETTING_LOGCAT_FILENAME);
			menuSetting.add(MENU_SETTING_SAVEDDIR);
		}

		menu.add(MENU_SAVE).setIcon(android.R.drawable.ic_menu_save);
		return true;
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
			startSaveLog();
			// TaskManagerForSave.startSaveTask(this.getApplicationContext());
			myResult = true;
		} else if (MENU_STOP_SAVE_AT_BGGROUND.equals(selectedItemTitle)) {
			TaskManagerForSave.stopSaveTask(this.getApplicationContext());
			myResult = true;
		} else if (MENU_START_SHOW_LOG.equals(selectedItemTitle)) {
			if (mShowTask == null || !mShowTask.isAlive()) {
				// KyoroLogcatSetting.f
				// mLogcatViewer.setFontSize(KyoroLogcatSetting.getFontSize());
				mLogcatOutput.setTextSize(KyoroLogcatSetting.getFontSize());
				mLogcatViewer.setFontSize(KyoroLogcatSetting.getFontSize());
//				mLogcatViewer.getLineView().setTextSize(KyoroLogcatSetting.getFontSize());
				mShowTask = new ShowCurrentLogTask(mLogcatOutput,
						KyoroLogcatSetting.getLogcatOption());
				mShowTask.start();
				// todo following refactraing
				mStage.resetTimer();
			}
			myResult = true;
		} else if (MENU_START_SHOW_LOG_FROM_FILE.equals(selectedItemTitle)) {
			mLogcatOutput.setTextSize(KyoroLogcatSetting.getFontSize());
			startFolder();
			myResult = true;
			// todo following refactraing
			mStage.resetTimer();
		} else if (MENU_STOP_SHOW_LOG.equals(selectedItemTitle)) {
			stopShowLog();
			myResult = true;
		} else if (MENU_SEND_MAIL.equals(selectedItemTitle)) {
			SendCurrentLogTask task = new SendCurrentLogTask(this,
					KyoroLogcatSetting.getLogcatOption());
			task.start();
			myResult = true;
		} else if (MENU_CLEAR_LOG.equals(selectedItemTitle)) {
			ClearCurrentLogTask task = new ClearCurrentLogTask(mLogcatOutput);
			task.start();
			myResult = true;
		} else if (MENU_SETTING_LOGCAT_OPTION.equals(selectedItemTitle)) {
			PreferenceLogcatOptionDialog.createDialog(this).show();
			stopShowLog();
			myResult = true;
		} else if (MENU_SETTING_FONT_SIZE.equals(selectedItemTitle)) {
			PreferenceFontSizeDialog.createDialog(this).show();
			stopShowLog();
			myResult = true;
		} else if (MENU_SETTING_LOGCAT_FILENAME.equals(selectedItemTitle)) {
			PreferenceLogcatFilenameDialog.createDialog(this).show();
			myResult = true;
		} else if (MENU_SETTING_SAVEDDIR.equals(selectedItemTitle)) {
			PreferenceLogcatDirectoryDialog.createDialog(this).show();
			myResult = true;
		}
		else if (MENU_SAVE.equals(selectedItemTitle)) {
			DumpCurrentLogTask task = new DumpCurrentLogTask(
					KyoroLogcatSetting.getLogcatOption());
			task.start();
		}
		return myResult;
	}

	private synchronized void startSaveLog() {
		runOnUiThread(new StartSaveTask());
	}

	private synchronized void stopShowLog() {
		if (mShowTask != null && mShowTask.isAlive()) {
			mShowTask.terminate();
		}
		mShowTask = null;
	}

	private class StartSaveTask implements Runnable {
		public void run() {
			if (!TaskManagerForSave.saveTaskIsForceKilled()) {
				TaskManagerForSave.startSaveTask(KyoroApplication
						.getKyoroApplication().getApplicationContext());
				return;
			}

			String[] items = { "retry save task", "fin save task" };
			AlertDialog.Builder alert = new AlertDialog.Builder(
					KyoroLogcatActivity.this);
			// alert.setTitle("Title");
			alert.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					if (arg1 == 0) {
						TaskManagerForSave.startSaveTask(KyoroApplication
								.getKyoroApplication().getApplicationContext());
					} else {
						TaskManagerForSave.stopSaveTask(KyoroApplication
								.getKyoroApplication().getApplicationContext());
					}
				}
			});

			alert.show();
		}
	}

	private void startFolder() {
		runOnUiThread(new ShowFolderDialogTask());
	}

	Handler handler = null;

	private void startFolder(final int delay) {
		runOnUiThread(new ShowFolderDialogTask());

		// runOnUiThread(new Runnable(){
		// public void run() {
		// handler = new Handler();
		// handler.postDelayed(new ShowFolderDialogTask(), delay);
		// }
		// });
	}

	public class ShowFolderDialogTask implements Runnable {
		public void run() {
			Toast.makeText(KyoroLogcatActivity.this, "tap and long tap",
					Toast.LENGTH_LONG).show();

			SimpleFileExplorer dialog = SimpleFileExplorer.createDialog(
					KyoroLogcatActivity.this,
					KyoroLogcatSetting.getHomeDirInSDCard());
			dialog.show();
			dialog.setOnSelectedFileAction(new FileSelectedAction());
		}
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			int x = graphics.getWidth() - mCircleController.getWidth() / 2;
			int y = graphics.getHeight() - mCircleController.getHeight() / 2;
			KyoroLogcatActivity.this.mCircleController.setPoint(x, y);
		}
	}

	public class FileSelectedAction implements
			SimpleFileExplorer.SelectedFileAction {
		public boolean onSelectedFile(final File file, String action) {
			try {
				if (file == null || !file.exists() || file.isDirectory()) {
					return false;
				}

				if (SimpleFileExplorer.SelectedFileAction.LONG_CLICK
						.equals(action)) {
					KyoroLogcatActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							IntentActionDialog dialog = new IntentActionDialog(
									KyoroLogcatActivity.this,
									KyoroLogcatActivity.this, file);
							dialog.show();
						}
					});
					return true;
				} else {
					if (mShowTask == null || !mShowTask.isAlive()) {
//						mLogcatOutput.setTextSize(KyoroLogcatSetting.getFontSize());
//						mLogcatViewer.getLineView().setTextSize(KyoroLogcatSetting.getFontSize());
						mShowTask = new ShowFileContentTask(mLogcatOutput, file);
						mShowTask.start();
					}
					return true;
				}
			} catch (Throwable t) {
				t.printStackTrace();
				return false;
			}
		}
	}

	public class FilterSettingAction implements
			TextView.OnEditorActionListener, View.OnClickListener {
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// All IME Application take that actionId become imeoption's value.
			update();
			return true;
		}

		public void onClick(View v) {
			update();
		}

		private void update() {
			try {
				CharSequence mUserInputedText = mInputForLogFilter.getText();
				String filterText = "";
				if (mUserInputedText != null) {
					filterText = mUserInputedText.toString();
				}
				Pattern filter = Pattern.compile(filterText);
				mLogcatViewer.startFilter(filter);
				// todo following refactraing
				mStage.resetTimer();

			} catch (Throwable t) {
				KyoroApplication
						.showNotification("Failed to filter logcat log from input text.");
			}
		}
	}
}