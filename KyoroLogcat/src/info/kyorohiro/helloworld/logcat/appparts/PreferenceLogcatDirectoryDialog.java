package info.kyorohiro.helloworld.logcat.appparts;

import java.io.File;

import info.kyorohiro.helloworld.pfdep.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.pfdep.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.logcat.KyoroLogcatActivity;
import info.kyorohiro.helloworld.logcat.KyoroLogcatSetting;
import info.kyorohiro.helloworld.logcat.KyoroLogcatActivity.FileSelectedAction;
import info.kyorohiro.helloworld.logcat.widget.KyoroSaveWidget;
import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreferenceLogcatDirectoryDialog extends Dialog {

	private Activity mOwnerActivity = null;
	private AutoCompleteTextView mEdit = null;
	private Button mOK = null;
	private Button mBrowse = null;

	private LinearLayout mLayout = null;
	private ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);

	public PreferenceLogcatDirectoryDialog(Activity owner) {
		super(owner);
		mOwnerActivity = owner;
		mLayout = new LinearLayout(getContext());
		setEditText();
		TextView label = new TextView(getContext());
		label.setText("<dirname>-------------------");
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.addView(label, mParams);
		mLayout.addView(mEdit, mParams);
		mEdit.setText(KyoroLogcatSetting.getHomeDirInSDCard().getPath());
		TextView memo = new TextView(getContext());
		memo.setText("update: update directory path."+"and File#mkdirs");
		mBrowse = new Button(getContext());
		mBrowse.setText("browse");
		mBrowse.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				File f = KyoroLogcatSetting.getHomeDirInSDCard();
				if(!f.exists() || !f.isDirectory()){
					f = Environment.getRootDirectory();
				}
				SimpleFileExplorer dialog = SimpleFileExplorer.createDialog(
						PreferenceLogcatDirectoryDialog.this.mOwnerActivity,
						f,
						SimpleFileExplorer.MODE_DIR_SELECT);

				dialog.show();
				dialog.setOnSelectedFileAction(new SelectedFileAction() {
					public boolean onSelectedFile(File file, String action) {
						if (PUSH_SELECT.equals("" + action)) {
							mEdit.setText(file.getPath());
							return true;
						}
						return false;
					}
				});
			}
		});
		mLayout.addView(mBrowse);
		mOK = new Button(getContext());
		mOK.setText("Update");
		mOK.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				if(mEdit == null){
					return; 
				} 
				String text =
					mEdit.getText().toString(); 

				if(text == null) {
					KyoroLogcatSetting.setHomeDirInSDCard("none");
				}
				KyoroLogcatSetting.setHomeDirInSDCard(text);
				try {
					File f = new File(text);
					if(!f.exists()){
						f.mkdirs();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				PreferenceLogcatDirectoryDialog.this.dismiss();
			}
		});
		mLayout.addView(mOK);
		mLayout.addView(memo);
		setContentView(mLayout, mParams);
	}


	public void setEditText() {
		mEdit = new AutoCompleteTextView(getContext());
		mEdit.setSelected(false);
		mEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		mEdit.setHint("Filter regex(find)");
		ArrayAdapter<String> automatedStrage = new ArrayAdapter<String>(
				getContext(), android.R.layout.simple_dropdown_item_1line,
				new String[] { "-v time -b main", "-v time -b events",
						"-v time -b radio", "-v time -b system",
						"-v time -b main -b events",
						"-v time -b main -b events -b radio",
						"-v time -b main -b events -b radio -b system" });
		mEdit.setAdapter(automatedStrage);
		mEdit.setThreshold(1);
	}

	public static PreferenceLogcatDirectoryDialog createDialog(Activity owner) {
		return new PreferenceLogcatDirectoryDialog(owner);
	}

}
