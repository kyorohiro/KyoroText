package info.kyorohiro.helloworld.textviewer.appparts;

import java.nio.charset.Charset;
import java.util.SortedMap;

import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivitySetCharsetAction implements MainActivityMenuAction {

	public static String TITLE = "charset all";
	private TextViewer mDisplayedTextViewer = null;

	public MainActivitySetCharsetAction(TextViewer viewer) {
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

	public class DialogForShowDeviceSupportCharset extends Dialog {
		public DialogForShowDeviceSupportCharset(Context context) {
			super(context);
			ListView charsetListUIParts = new ListView(context);
			ArrayAdapter<String> displayCharsetList = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
			SortedMap<String,Charset> deviceSupportCharsetList = Charset.availableCharsets();
			for(String selectedCharset : deviceSupportCharsetList.keySet()){
				displayCharsetList.add(""+selectedCharset);				
			}

			charsetListUIParts.setAdapter(displayCharsetList);
			charsetListUIParts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ListView charsetListUIParts = (ListView) parent;
					String selectedCharset = (String)charsetListUIParts.getItemAtPosition(position);

					mDisplayedTextViewer.setCharset(selectedCharset);
					
					// save selected charset in setting file.
					if(selectedCharset != null && !selectedCharset.equals("")){
						KyoroSetting.setCurrentCharset(selectedCharset);
						mDisplayedTextViewer.restart();
					}

					// 
					DialogForShowDeviceSupportCharset.this.dismiss();	            }
			});
			setContentView(charsetListUIParts);
		}
	}
}
