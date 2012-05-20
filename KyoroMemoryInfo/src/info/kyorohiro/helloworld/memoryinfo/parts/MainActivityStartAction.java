package info.kyorohiro.helloworld.memoryinfo.parts;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import info.kyorohiro.helloworld.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.memoryinfo.KyoroApplication;
import info.kyorohiro.helloworld.memoryinfo.KyoroMemoryInfoService;
import info.kyorohiro.helloworld.memoryinfo.KyoroSetting;
import info.kyorohiro.helloworld.memoryinfo.task.MemoryInfoTask;
import info.kyorohiro.helloworld.memoryinfo.task.TaskRunner;

public class MainActivityStartAction implements MainActivityMenuAction {

	private TaskRunner mTask = new TaskRunner();

	@Override
	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		if(KyoroSetting.isForground()) {
			menu.add("stop");
		} else {
			menu.add("start");
		}
		return false;
	}

	@Override
	public boolean onMenuItemSelected(Activity activity, int featureId,MenuItem item) {
		if("start".equals(item.getTitle())) {
			Thread t = new Thread(new Runnable() {
				public void run(){
					KyoroSetting.isForground(true);
					KyoroMemoryInfoService.startService(KyoroApplication.getKyoroApplication(), "start");
					mTask.start(new MemoryInfoTask());
				}
			});
			t.start();
		}
		else if("stop".equals(item.getTitle())){
			KyoroSetting.isForground(false);
			KyoroMemoryInfoService.stopService(activity);
			mTask.stop();
		}
		return false;
	}

}
