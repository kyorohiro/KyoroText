package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.SortedMap;

import info.kyorohiro.helloworld.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.appparts.MainActivitySetCharsetAction.DialogForShowDeviceSupportCharset;
import info.kyorohiro.helloworld.textviewer.util.CharsetDetectorSample;
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

public class MainActivitySetCharsetDetectionAction implements MainActivityMenuAction {

	public static String TITLE = "charset auto detection";
	private TextViewer mDisplayedTextViewer = null;

	public MainActivitySetCharsetDetectionAction(TextViewer viewer) {
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

	private DialogForShowDeviceSupportCharset mDialog = null;
	private void showDialog(Activity activity) {
		mDialog = new DialogForShowDeviceSupportCharset(activity);
		mDialog.show();
		mDialog.setOwnerActivity(activity);

		Thread th = new Thread(new AutoDetection());
		th.start();
	}

	private ListView mCharsetListUIParts = null;
	public class DialogForShowDeviceSupportCharset extends Dialog {
		public DialogForShowDeviceSupportCharset(Context context) {
			super(context);
			mCharsetListUIParts = new ListView(context);
			ArrayAdapter<String> displayCharsetList = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
			mCharsetListUIParts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ListView charsetListUIParts = (ListView) parent;
					String selectedCharset = (String)charsetListUIParts.getItemAtPosition(position);
					mDisplayedTextViewer.setCharset(selectedCharset);					
					if(selectedCharset != null && !selectedCharset.equals("")){
						KyoroSetting.setCurrentCharset(selectedCharset);
						mDisplayedTextViewer.restart();
					}
					DialogForShowDeviceSupportCharset.this.dismiss();	            }
			});
			setContentView(mCharsetListUIParts);

		}
	}

	public void a(ArrayAdapter<String> displayCharsetList) {
		mDialog.getOwnerActivity().runOnUiThread(new A(displayCharsetList));
	}

	public class A implements Runnable {
		ArrayAdapter<String> mNext;
		public A(ArrayAdapter<String> displayCharsetList) {
			mNext = displayCharsetList;
		}

		@Override
		public void run() {	
			ListView v = mCharsetListUIParts;
			if(v!=null){
				v.setAdapter(mNext);
			}
		}

	}
	public class AutoDetection implements Runnable {
		@Override
		public void run() {
			String path = mDisplayedTextViewer.getCurrentPath();
			if(path == null || path.equals("")){
				return;
			}
			File f = new File(path);
			if(f==null||!f.exists()|| !f.canRead() || !f.isFile()){
				return;
			}
			CharsetDetectorSample det = new CharsetDetectorSample();
			det.detect(f);

			try {
				ArrayAdapter<String> displayCharsetList = 
					new ArrayAdapter<String>(mDialog.getContext()
							, android.R.layout.simple_list_item_1);
				String[] result = det.getResult();
				for(String s: result){
					displayCharsetList.add(""+s);
				}
				a(displayCharsetList);
			} catch(Throwable t) {
				
			}
		}
		
	}
}
