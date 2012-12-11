package info.kyorohiro.helloworld.textviewer.appparts;

import java.nio.charset.Charset;
import java.util.SortedMap;

import info.kyorohiro.helloworld.pfdep.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivitySetCRLFAction implements MainActivityMenuAction {

	public static String TITLE = "crlf setting";
	private LineViewManager mDisplayedTextViewer = null;

	public MainActivitySetCRLFAction(LineViewManager viewer) {
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
		DialogForShowCRLFSetting dialog = new DialogForShowCRLFSetting(activity);
		dialog.show();
	}

	public class DialogForShowCRLFSetting extends Dialog {
		RadioButton rb1 = new RadioButton(getContext());
		RadioButton rb2 = new RadioButton(getContext());

		public DialogForShowCRLFSetting(Context context) {
			super(context);
			rb1.setId(101);
			rb2.setId(102);
			RadioGroup l = new RadioGroup(getContext());
			l.setOrientation(LinearLayout.VERTICAL);
			rb1.setText(KyoroSetting.VALUE_CRLF);
			rb2.setText(KyoroSetting.VALUE_LF);
			l.addView(rb1);
			l.addView(rb2);
			setContentView(l);
			rb1.setSelected(false);
			rb2.setSelected(false);
			if(KyoroSetting.VALUE_CRLF.equals(KyoroSetting.getCurrentCRLF())){
//				android.util.Log.v("kiyo","kkk=sdf -1-");
				rb1.setSelected(true);
				l.check(rb1.getId());
			} else {
//				android.util.Log.v("kiyo","kkk=sdf -2-");
				rb2.setSelected(true);
				l.check(rb2.getId());
			}
			
			l.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					TextViewer tv = LineViewManager.getManager().getFocusingTextViewer();
					LineView v = tv.getLineView();
//					android.util.Log.v("kiyo","kkk=sdf"+checkedId);
					if(checkedId == rb1.getId()) {
//						android.util.Log.v("kiyo","kkk=sdf -3-");
						KyoroSetting.setCurrentCRLF(KyoroSetting.VALUE_CRLF);
						v.isCrlfMode(true);
					} else {
//						android.util.Log.v("kiyo","kkk=sdf -4-");
						KyoroSetting.setCurrentCRLF(KyoroSetting.VALUE_LF);
						v.isCrlfMode(false);
					}
				}
			});
		}
	}
}
