package info.kyorohiro.helloworld.textviewer.appparts;

import info.kyorohiro.helloworld.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.textviewer.task.SearchTextTask;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivitySearchText implements MainActivityMenuAction {

	public static String TITLE = "search";
	private LineViewManager mDisplayedTextViewer = null;

	public MainActivitySearchText(LineViewManager viewer) {
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
	private EditText mSearchWord = null;
	private TextView mMessage = null;
	private Button mNext = null;
	public class DialogForShowDeviceSupportCharset extends Dialog {
		public DialogForShowDeviceSupportCharset(Context context) {
			super(context);
			int fontSize = KyoroSetting.getCurrentFontSize();
			mNext = new Button(context);
			mNext.setText("next");
			mSearchWord = new EditText(context);
			mMessage = new TextView(context);
			mSearchWord.setText(".*");
			mMessage.setText(""+fontSize);
			LinearLayout layout = new LinearLayout(context);
			setTitle("------------------search-----------------------");
			layout.setOrientation(LinearLayout.VERTICAL);
			layout.addView(mSearchWord, params);
			layout.addView(mMessage, params);
			layout.setPadding(40, 40, 40, 40);
			layout.addView(mNext);
			setContentView(layout,params);
						
			mNext.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					try {
						TextViewer viewer = mDisplayedTextViewer.getFocusingTextViewer();
						String searchWord = mSearchWord.getText().toString();
						Thread th = new Thread(
						SearchTextTask.getSearchTextTask(viewer, searchWord));
						th.start();
						dismiss();
					} catch(Throwable t){

					} 
				}
			});
		}
	}
}
