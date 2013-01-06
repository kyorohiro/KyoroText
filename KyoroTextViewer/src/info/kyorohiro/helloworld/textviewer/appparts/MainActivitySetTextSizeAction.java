package info.kyorohiro.helloworld.textviewer.appparts;

import info.kyorohiro.helloworld.pfdep.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.appparts.MenuActionWarningMessagePlus.MyTask;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
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
	private BufferManager mDisplayedTextViewer = null;

	public MainActivitySetTextSizeAction(BufferManager viewer) {
		mDisplayedTextViewer = viewer;
	}
	private boolean isOn() {
		TextViewer viewer = BufferManager.getManager().getFocusingTextViewer();
		TextViewer mini = BufferManager.getManager().getMiniBuffer();
		TextViewer info = BufferManager.getManager().getInfoBuffer();
		if(viewer == null||viewer == mini|| viewer == info) {
			return false;
		} else {
			return true;
		}
	}
	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		if(!isOn()) {
			return false;
		}
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId, MenuItem item) {
		if(!isOn()) {
			return false;
		}
		if(item.getTitle().equals(TITLE)) {
			 MenuActionWarningMessagePlus.showDialog(activity, new  MyTask() {
				 public void run(Activity c){
						showDialog(c);
				 }
			 }, BufferManager.getManager().getFocusingTextViewer().isEdit());
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
					if(progress<=0){
						progress = 2;
					}
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
						TextViewer viewer = mDisplayedTextViewer.getFocusingTextViewer();
						mDisplayedTextViewer.setCurrentFontSize(KyoroSetting.getCurrentFontSize());
						viewer.setCurrentFontSize(KyoroSetting.getCurrentFontSize());
						viewer.restart();
						dismiss();
					} catch(Throwable t){

					} 
				}
			});
		}
	}
}
