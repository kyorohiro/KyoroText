package info.kyorohiro.helloworld.textviewer.appparts;

import java.nio.charset.Charset;
import java.util.SortedMap;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivitySetCharsetAction implements MainActivityMenuAction {

	public static String TITLE = "charset all";
	private BufferManager mDisplayedTextViewer = null;

	public MainActivitySetCharsetAction(BufferManager viewer) {
		mDisplayedTextViewer = viewer;
	}
	private boolean isOn() {
		TextViewer viewer = BufferManager.getManager().getFocusingTextViewer();
		TextViewer mini = BufferManager.getManager().getMiniBuffer();
		TextViewer info = BufferManager.getManager().getInfoBuffer();
		TextViewer shell = BufferManager.getManager().getShellBuffer();
		if(viewer == null||viewer == mini|| viewer == info || viewer == shell ) {
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

					TextViewer viewer = mDisplayedTextViewer.getFocusingTextViewer();
					viewer.setCharset(selectedCharset);
					
					// save selected charset in setting file.
					if(selectedCharset != null && !selectedCharset.equals("")){
						KyoroSetting.setCurrentCharset(selectedCharset);
						viewer.restart();
					}

					// 
					DialogForShowDeviceSupportCharset.this.dismiss();	            }
			});
			setContentView(charsetListUIParts);
		}
	}
}
