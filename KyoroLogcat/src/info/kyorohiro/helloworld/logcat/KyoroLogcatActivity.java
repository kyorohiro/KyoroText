package info.kyorohiro.helloworld.logcat;

import java.io.File;
import java.util.regex.Pattern;

import info.kyorohiro.helloworld.android.base.TestActivity;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.CyclingFlowingLineData;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.logcat.logcat.LogcatViewer;
import info.kyorohiro.helloworld.logcat.tasks.ClearCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.TaskManagerForSave;
import info.kyorohiro.helloworld.logcat.tasks.SendCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.ShowCurrentLogTask;
import info.kyorohiro.helloworld.logcat.tasks.ShowFileContentTask;
import info.kyorohiro.helloworld.util.IntentActionDialog;
import info.kyorohiro.helloworld.util.SimpleFileExplorer;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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

	private LogcatViewer mLogcatViewer = new LogcatViewer(3000);
	private CyclingFlowingLineData mLogcatOutput = mLogcatViewer.getCyclingStringList();
	private SimpleCircleController mCircleController = new SimpleCircleController();
	private SimpleStage mStage = null;
	private AutoCompleteTextView mInputForLogFilter = null; 
	private ShowCurrentLogTask mShowTask = null;


	public static void startActivityFormFolder(Context context){
		Intent intent = new Intent(context, KyoroLogcatActivity.class);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setAction(Intent.ACTION_MAIN);
		intent.putExtra("action", "folder");
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private boolean showTaskIsAlive() {
		if(mShowTask == null || !mShowTask.isAlive()){
			return false;
		}
		else {
			return true;
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.changeTitle(this, "kyoro logcat", Color.parseColor("#cc795514"), Color.parseColor("#e5cf2a"));
		this.changeMenuBgColor(this, Color.parseColor("#e5cf2a"));
		mCircleController.setEventListener(mLogcatViewer.getCircleControllerAction());
		mStage = new SimpleStage(this.getApplicationContext());
		mStage.getRoot().addChild(new Layout());
		mStage.getRoot().addChild(mLogcatViewer);
		mStage.getRoot().addChild(mCircleController);
		mStage.start();
		
		mInputForLogFilter = new AutoCompleteTextView(this);
		mInputForLogFilter.setSelected(false);
		mInputForLogFilter.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		mInputForLogFilter.setHint("Filter regex(find)");
		mInputForLogFilter.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		mInputForLogFilter.setOnEditorActionListener(new FilterSettingAction());
        ArrayAdapter<String> automatedStrage = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,
        		new String[]{
        		"[DIVWEFS]/"," [DIVWEFS]/", ".[DIVWEFS]/",
        		" D/"," I/"," V/"," W/"," E/"," F/"," S/",
        		"D/","I/","V/","W/","E/","F/","S/",
        		"[DIV]/","[IVW]/","[VWE]/","[WEF]/","[EFS]/"
        		});
        mInputForLogFilter.setAdapter(automatedStrage);
        mInputForLogFilter.setThreshold(1);
		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rootLayout.addView(mInputForLogFilter,params);
		rootLayout.addView(mStage);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		setContentView(rootLayout);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		Bundle extras = intent.getExtras();
		if(extras == null){
			return;
		}
		String action = extras.getString("action");
		if(action != null && action.equals("folder")){
			startFolder();
		}
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

		if (TaskManagerForSave.saveTaskIsAlive()) {
			menu.add(MENU_STOP_SAVE_AT_BGGROUND).setIcon(
					R.drawable.ic_stop_save);
		} else {
			menu.add(MENU_START_SAVE_AT_BGGROUND).setIcon(
					R.drawable.ic_start_save);
		}

		menu.add(MENU_SEND_MAIL).setIcon(R.drawable.ic_send_mail);
		menu.add(MENU_CLEAR_LOG).setIcon(R.drawable.ic_clear_log);
		
		if (!showTaskIsAlive()) {
			menu.add(MENU_START_SHOW_LOG_FROM_FILE).setIcon(R.drawable.ic_folder);
		}

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
			TaskManagerForSave.startSaveTask(this.getApplicationContext());
			myResult = true;
		} else if (MENU_STOP_SAVE_AT_BGGROUND.equals(selectedItemTitle)) {
			TaskManagerForSave.stopSaveTask(this.getApplicationContext());
			myResult = true;
		} else if (MENU_START_SHOW_LOG.equals(selectedItemTitle)) {
			if(mShowTask == null || !mShowTask.isAlive()){
				mShowTask = new ShowCurrentLogTask(mLogcatOutput, "-v time");
				mShowTask.start();
			}
			myResult = true;
		} else if(MENU_START_SHOW_LOG_FROM_FILE.equals(selectedItemTitle)) {
			startFolder();
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

	private void startFolder() {
		runOnUiThread(new Runnable() {				
			public void run() {
				Toast.makeText(KyoroLogcatActivity.this, "tap and long tap", Toast.LENGTH_LONG).show();

				SimpleFileExplorer dialog = 
					SimpleFileExplorer.createDialog(KyoroLogcatActivity.this, Environment.getExternalStorageDirectory());
				dialog.show();
				dialog.setOnSelectedFileAction(new FileSelectedAction());
			}
		});
	}
	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			int x = graphics.getWidth() - mCircleController.getWidth();
			int y = graphics.getHeight() - mCircleController.getHeight();
			KyoroLogcatActivity.this.mCircleController.setPoint(x, y);
		}
	}

	public class FileSelectedAction implements SimpleFileExplorer.SelectedFileAction {
		public boolean onSelectedFile(final File file, String action) {
			try {
				if(file == null||!file.exists()||file.isDirectory()) {
					return false;
				}
				
				if(SimpleFileExplorer.SelectedFileAction.LONG_CLICK.equals(action)) {
					KyoroLogcatActivity.this.runOnUiThread(new Runnable(){public void run(){
					IntentActionDialog dialog = new IntentActionDialog(
							KyoroLogcatActivity.this,
							KyoroLogcatActivity.this, file);
					dialog.show();
					}});
					return true;
				}
				else {
					ShowFileContentTask task = new ShowFileContentTask(mLogcatOutput, file);
					task.start();
					return true;
				}
			} catch(Throwable t) {
				t.printStackTrace();
				return false;
			}
		}
	}

	public class FilterSettingAction implements TextView.OnEditorActionListener {
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			// All IME Application take that actionId become imeoption's value.
			try {
				CharSequence mUserInputedText = mInputForLogFilter.getText();
				String filterText = "";
				if(mUserInputedText != null){
					filterText = mUserInputedText.toString();
				}
				Pattern filter = Pattern.compile(filterText);
				mLogcatViewer.startFilter(filter);
			} catch(Throwable t) {
				KyoroApplication.showMessageAndNotification("Failed to filter logcat log from input text.");
			}
			return false;
		}
	}
}