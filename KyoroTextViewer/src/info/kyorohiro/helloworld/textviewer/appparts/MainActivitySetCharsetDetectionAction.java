package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;

import info.kyorohiro.helloworld.pfdep.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.appparts.MenuActionWarningMessagePlus.MyTask;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.textviewer.util.CharsetDetectorSample;
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

public class MainActivitySetCharsetDetectionAction implements MainActivityMenuAction {

	public static String TITLE = "charset auto detection";
	private BufferManager mDisplayedTextViewer = null;

	public MainActivitySetCharsetDetectionAction(BufferManager viewer) {
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
			mCharsetListUIParts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ListView charsetListUIParts = (ListView) parent;
					String selectedCharset = (String)charsetListUIParts.getItemAtPosition(position);
					TextViewer viewer = mDisplayedTextViewer.getFocusingTextViewer();
					viewer.setCharset(selectedCharset);					
					if(selectedCharset != null && !selectedCharset.equals("")){
						KyoroSetting.setCurrentCharset(selectedCharset);
						viewer.restart();
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
			TextViewer viewer = mDisplayedTextViewer.getFocusingTextViewer();
			String path = viewer.getCurrentPath();
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
