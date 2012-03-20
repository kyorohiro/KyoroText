package info.kyorohiro.helloworld.logcat.appparts;

import info.kyorohiro.helloworld.logcat.KyoroLogcatSetting;
import android.app.Activity;
import android.app.Dialog;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreferenceLogcatOptionDialog extends Dialog {

	private Activity mOwnerActivity =  null; 
	private AutoCompleteTextView mEdit = null;
	private Button mOK = null;
	private LinearLayout mLayout = null;
	private ViewGroup.LayoutParams mParams = 
		new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

	public PreferenceLogcatOptionDialog(Activity owner) {
		super(owner);
		mOwnerActivity = owner;
		mLayout = new LinearLayout(getContext());
		setEditText();
		TextView label = new TextView(getContext());
		label.setText("--option-------------------");
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.addView(label, mParams);
		mLayout.addView(mEdit, mParams);
		mEdit.setText(KyoroLogcatSetting.getLogcatOption());
		TextView memo = new TextView(getContext());
		memo.setText(
				"--example)"+"\n" +
				"-v time" + "\n" +
				"-v time -b main" + "\n" +
				"-v time -b events" + "\n" +
				"-v time -b radio" + "\n" +
				"=following device dependent=" + "\n" +
				"-v time -b system" + "\n" +
				"-v time -b main -b events -b radio -b system" + "\n"
				);
		mOK = new Button(getContext());
		mOK.setText("Update");
		mOK.setOnClickListener(new android.view.View.OnClickListener(){
			public void onClick(View v) {
				if(mEdit == null){
					return;
				}
				String text = mEdit.getText().toString();
				if(text == null){
					KyoroLogcatSetting.setDefaultLogcatOption();
				}
				KyoroLogcatSetting.setLogcatOption(text.replace("-d|-c", " "));
			    PreferenceLogcatOptionDialog.this.dismiss();
			}
		});
		mLayout.addView(mOK);
		mLayout.addView(memo);
		setContentView(mLayout, mParams);
	}

	public void setEditText(){
		mEdit = new AutoCompleteTextView(getContext());
		mEdit.setSelected(false);
		mEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		mEdit.setHint("Filter regex(find)");
        ArrayAdapter<String> automatedStrage = 
        	new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,
        		new String[]{
        		"-v time -b main",
        		"-v time -b events",
        		"-v time -b radio",
        		"-v time -b system",
        		"-v time -b main -b events",
        		"-v time -b main -b events -b radio",
        		"-v time -b main -b events -b radio -b system" 
        		});
        mEdit.setAdapter(automatedStrage);
        mEdit.setThreshold(1);
	}

	public static PreferenceLogcatOptionDialog createDialog(Activity owner) {
		return new PreferenceLogcatOptionDialog(owner);		
	}


}

