package info.kyorohiro.helloworld.memoryinfo.parts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import info.kyorohiro.helloworld.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer.ListItemWithFile;
import info.kyorohiro.helloworld.memoryinfo.KyoroApplication;
import info.kyorohiro.helloworld.memoryinfo.KyoroMemoryInfoService;
import info.kyorohiro.helloworld.memoryinfo.KyoroSetting;
import info.kyorohiro.helloworld.memoryinfo.task.MemoryInfoTask;
import info.kyorohiro.helloworld.memoryinfo.task.TaskRunner;

public class MainActivitySelectPackageAction implements MainActivityMenuAction {

	private ViewGroup.LayoutParams mParams1 = 
			new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		private ViewGroup.LayoutParams mParams2 = 
			new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
		
	@Override
	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add("select");
		return false;
	}

	@Override
	public boolean onMenuItemSelected(Activity activity, int featureId,MenuItem item) {
		if("select".equals(item.getTitle())) {
			Dialog d = createDialog(activity);
			d.show();
		}
		return false;
	}

    public SelectPacakgeDialog createDialog(Activity owner) {
		return new SelectPacakgeDialog(owner);		
	}

    public class SelectPacakgeDialog extends Dialog {

		private LinearLayout mLayout = null;
		private ListView mCurrentFileList = null;
        private ArrayList<String> mAppList = new ArrayList<String>();
 
    	public SelectPacakgeDialog(Context context) {
			super(context);
			mLayout =new LinearLayout(context);
			mLayout.setOrientation(LinearLayout.VERTICAL);
			mCurrentFileList = new ListView(context);
			list();
			ArrayAdapter<String> adapter = 
					 new ArrayAdapter<String>(
							getContext(),
							android.R.layout.simple_list_item_1);
			for(String s:mAppList){
				adapter.add(s);
			}
			mCurrentFileList.setAdapter(adapter);
			
			mLayout.addView(mCurrentFileList,mParams1);
			addContentView(mLayout, mParams1);
			mCurrentFileList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					String item = 
							((ArrayAdapter<String>)mCurrentFileList.getAdapter())
							.getItem(pos);
					KyoroSetting.setData(KyoroSetting.TAG_PACKAGE, item);
					android.util.Log.v("selected",""+item);
					SelectPacakgeDialog.this.dismiss();
				}
			});
		}

		
		public void list() {
			mAppList.clear();
			PackageManager manager = KyoroApplication.getKyoroApplication().getPackageManager();
	        Intent intent = new Intent(Intent.ACTION_MAIN, null);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        List<ResolveInfo> appInfo = manager.queryIntentActivities(intent, 0);
	        // アプリケーション名の取得
	        if (appInfo != null) {
	            for (ResolveInfo info : appInfo) {
//	                mAppList.add( (String)info.loadLabel(manager));
	                mAppList.add( (String)info.activityInfo.packageName);
	            }
	        }
		}
	}
}
