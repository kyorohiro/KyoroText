package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;
import java.nio.charset.Charset;
import java.util.SortedMap;

import info.kyorohiro.helloworld.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivitySetFontAction extends  MainActivityMenuAction {

	public static String TITLE = "charset";
	private TextViewer mViewer = null;

	public MainActivitySetFontAction(TextViewer viewer) {
		mViewer = viewer;
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
		A a = new A(activity);
		a.show();
	}
	
	public class A extends Dialog {
		public A(Context context) {
			super(context);
			ListView view = new ListView(context);
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
			// 使用可能なキャラセット
			SortedMap<String,Charset> m = Charset.availableCharsets();
			for(String s : m.keySet()){
		        adapter.add(""+s);				
			}
			view.setAdapter(adapter);

			view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	                ListView listView = (ListView) parent;
	                String item = (String)listView.getItemAtPosition(position);
	                mViewer.setCharset(item);
	                if(item != null && !item.equals("")){
	                	KyoroSetting.setCurrentCharset(item);
	                }
	                A.this.dismiss();	            }
	        });
			
			setContentView(view);

		}
	}
}
