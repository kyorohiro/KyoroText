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

public class PreferenceFontSizeDialog extends Dialog {

	private Activity mOwnerActivity =  null; 
	private AutoCompleteTextView mEdit = null;
	private Button mOK = null;
	private LinearLayout mLayout = null;
	private ViewGroup.LayoutParams mParams = 
		new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

	public PreferenceFontSizeDialog(Activity owner) {
		super(owner);
		mOwnerActivity = owner;
		mLayout = new LinearLayout(getContext());
		setEditText();
		TextView label = new TextView(getContext());
		label.setText("--font size-");
		mLayout.setOrientation(LinearLayout.VERTICAL);
		mLayout.addView(label, mParams);
		mLayout.addView(mEdit, mParams);
		mEdit.setText(""+KyoroLogcatSetting.getFontSize());
		TextView memo = new TextView(getContext());
		memo.setText(""+"\n");
		mOK = new Button(getContext());
		mOK.setText("Update");
		mOK.setOnClickListener(new android.view.View.OnClickListener(){
			public void onClick(View v) {
				if(mEdit == null){
					return;
				}
				String text = mEdit.getText().toString();
				if(text == null){
					KyoroLogcatSetting.setFontSize(""+KyoroLogcatSetting.OPTION_FONT_SIZE_DEFAULT);
				}
				KyoroLogcatSetting.setFontSize(""+text);
			    PreferenceFontSizeDialog.this.dismiss();
			}
		});
		mLayout.addView(mOK, mParams);
		mLayout.addView(memo);
		setContentView(mLayout, mParams);
	}

	public void setEditText(){
		mEdit = new AutoCompleteTextView(getContext());
		mEdit.setSelected(false);
		mEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		mEdit.setHint("text size");
		mEdit.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        ArrayAdapter<String> automatedStrage = 
        	new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,
        		new String[]{
        		"8",
        		"16",
        		"32",
        		"64",
        		});
        mEdit.setAdapter(automatedStrage);
        mEdit.setThreshold(1);
	}

	public static PreferenceFontSizeDialog createDialog(Activity owner) {
		return new PreferenceFontSizeDialog(owner);		
	}


}

