package info.kyorohiro.helloworld.logcat.widget;

import info.kyorohiro.helloworld.android.base.TestService;
import info.kyorohiro.helloworld.logcat.KyoroApplication;
import info.kyorohiro.helloworld.logcat.KyoroLogcatActivity;
import info.kyorohiro.helloworld.logcat.KyoroLogcatSetting;
import info.kyorohiro.helloworld.logcat.tasks.TaskManagerForSave;
import info.kyorohiro.helloworld.logcat.tasks.SendCurrentLogTask;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class KyoroWidgetService extends TestService {
	public KyoroWidgetService() {
		super(10001);
	}


	public static String ACTION_CHANGE = "ACTION_CHANGE";
	public static String ACTION_SET = "ACTION_SET";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static void setWidgetImage(Context context, String type) {
        Intent intent =  new Intent(context, KyoroWidgetService.class);
        intent.setAction(KyoroWidgetService.ACTION_SET);
        intent.setType(type);
        context.startService(intent);
	}
	
	public static Intent getIntentToStartButtonAction(Context context, String type) {
	    Intent intent =  new Intent(context, KyoroWidgetService.class);
	    intent.setAction(KyoroWidgetService.ACTION_CHANGE);
        intent.setType(type);
	    return intent;
	}
/*
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		onStartHandle(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onStartHandle(intent);
		return super.onStartCommand(intent, flags, startId);
	}
*/
	@Override
	public void onStartHandle(Intent intent) {
		if(intent == null) {
			return;
		}

		String action = ""+intent.getAction();
		// 
		if(KyoroSaveWidget.TYPE.equals(""+intent.getType())) {
			 if (KyoroWidgetService.ACTION_SET.equals(action)){
					KyoroSaveWidget.set(this);
					KyoroSaveWidget.setImageAtAutoDetect(getApplicationContext());
				}
			 else if(KyoroWidgetService.ACTION_CHANGE.equals(action)){
				if(TaskManagerForSave.saveTaskIsAlive()){
					TaskManagerForSave.stopSaveTask(getApplicationContext());
				}
				else {
					if(TaskManagerForSave.saveTaskIsForceKilled()) {
						KyoroLogcatActivity.startActivityFormStartSaveDialog(getApplicationContext());
					}
					else {
						TaskManagerForSave.startSaveTask(getApplicationContext());
					}
				}
			}
		}
		else if(KyoroMailWidget.TYPE.equals(""+intent.getType())) {
			KyoroMailWidget.setSendMailImage(getApplicationContext());
			if(KyoroWidgetService.ACTION_SET.equals(action)){
				KyoroSaveWidget.set(this);	            
			}
			else if(KyoroWidgetService.ACTION_CHANGE.equals(action)){
				SendCurrentLogTask task = new SendCurrentLogTask(this.getApplicationContext(), KyoroLogcatSetting.getLogcatOption());
				task.start();
			}
		}
		else if(KyoroFolderWidget.TYPE.equals(""+intent.getType())) {
			KyoroMailWidget.setSendMailImage(getApplicationContext());
			if(KyoroWidgetService.ACTION_SET.equals(action)){
				KyoroFolderWidget.set(this);	            
			}
			else if(KyoroWidgetService.ACTION_CHANGE.equals(action)){
				KyoroLogcatActivity.startActivityFormFolder(this);
			}
		}
	}
}
