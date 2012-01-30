package info.kyorohiro.helloworld.logcat.widget;

import info.kyorohiro.helloworld.logcat.tasks.KyoroLogcatTaskManagerForSave;
import info.kyorohiro.helloworld.logcat.tasks.SendCurrentLogTask;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class KyoroWidgetService extends Service {
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

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String action = ""+intent.getAction();

		// 
		if(KyoroSaveWidget.TYPE.equals(""+intent.getType())) {
			 if (KyoroWidgetService.ACTION_SET.equals(action)){
					KyoroSaveWidget.set(this);	            
					if(KyoroLogcatTaskManagerForSave.saveTaskIsAlive()){
						KyoroSaveWidget.setStopImage(getApplicationContext());
					}
					else {
						KyoroSaveWidget.setSaveImage(getApplicationContext());
					}			
				}
			 else if(KyoroWidgetService.ACTION_CHANGE.equals(action)){
				if(KyoroLogcatTaskManagerForSave.saveTaskIsAlive()){
					KyoroLogcatTaskManagerForSave.stopSaveTask(getApplicationContext());
				}
				else {
					KyoroLogcatTaskManagerForSave.startSaveTask(getApplicationContext());
				}
			}
		}
		else if(KyoroMailWidget.TYPE.equals(""+intent.getType())) {
			KyoroMailWidget.setSendMailImage(getApplicationContext());
			if(KyoroWidgetService.ACTION_SET.equals(action)){
				KyoroSaveWidget.set(this);	            
			}
			else if(KyoroWidgetService.ACTION_CHANGE.equals(action)){
				SendCurrentLogTask task = new SendCurrentLogTask(this.getApplicationContext());
				task.start();
			}
		}
	}
}
