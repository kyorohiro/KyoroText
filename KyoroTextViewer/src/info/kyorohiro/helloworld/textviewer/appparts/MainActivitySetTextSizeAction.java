package info.kyorohiro.helloworld.textviewer.appparts;

import info.kyorohiro.helloworld.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivitySetTextSizeAction implements MainActivityMenuAction {

	public static String TITLE = "font size";
	private TextViewer mDisplayedTextViewer = null;

	public MainActivitySetTextSizeAction(TextViewer viewer) {
		mDisplayedTextViewer = viewer;
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId, MenuItem item) {
		if(item.getTitle().equals(TITLE)) {
			showDialog(activity);
			return true;
		}
		return false;
	}

	private void showDialog(Activity activity) {
		DialogForShowDeviceSupportCharset dialog = new DialogForShowDeviceSupportCharset(activity);
		dialog.show();
	}

	public static LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	private TextView mSample = null;
	private TextView mTextSize = null;
	private Button mOK = null;
	private SeekBar mBar =null;
	public class DialogForShowDeviceSupportCharset extends Dialog {
		public DialogForShowDeviceSupportCharset(Context context) {
			super(context);
			int fontSize = KyoroSetting.getCurrentFontSize();
			mOK = new Button(context);
			mOK.setText("OK");
			mSample = new TextView(context);
			mTextSize = new TextView(context);
			mSample.setText("Sample: ABCDEFGxyz.01234");
			mTextSize.setText(""+fontSize);
			mBar = new SeekBar(context);
			LinearLayout layout = new LinearLayout(context);
			setTitle("------------------font size-----------------------");
			mBar.setMax(126);
			mBar.setProgress(fontSize);
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.addView(mTextSize, params);
			layout.addView(mBar, params);
			layout.setPadding(40, 40, 40, 40);
//			layout.addView(mSample,params);
			layout.addView(mOK);
			setContentView(layout,params);
			mBar.setMinimumWidth(600);
			
			mBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					mTextSize.setText(""+progress);
					mTextSize.setTextSize(progress);
				}
			});
			
			mOK.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					int textSize = mBar.getProgress();
					try {
						KyoroSetting.setCurrentFontSize(""+textSize);
						mDisplayedTextViewer.setCurrentFontSize(textSize);
						mDisplayedTextViewer.restart();
						dismiss();
					} catch(Throwable t){

					} 
				}
			});
		}
	}
}
