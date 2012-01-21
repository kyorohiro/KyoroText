package info.kyorohiro.helloworld.logcat.widget;

import info.kyorohiro.helloworld.logcat.KyoroLogcatTaskManagerForSave;
import info.kyorohiro.helloworld.logcat.SendCurrentLogTask;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;
import android.os.IBinder;

public class KyoroWidgetService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static void setWidgetImage(Context context, String type) {
        Intent intent =  new Intent(context, KyoroWidgetService.class);
        intent.setAction(KyoroSaveWidget.ACTION_SET);
        intent.setType(type);
        context.startService(intent);
	}
	
	public static Intent getIntentToStartButtonAction(Context context, String type) {
	    Intent intent =  new Intent(context, KyoroWidgetService.class);
	    intent.setAction(KyoroSaveWidget.ACTION_CHANGE);
        intent.setType(type);

	    return intent;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Debug.waitForDebugger();
		String action = ""+intent.getAction();

		// 
		if(KyoroSaveWidget.TYPE.equals(""+intent.getType())) {
			if(KyoroSaveWidget.ACTION_CHANGE.equals(action)){
				if(KyoroLogcatTaskManagerForSave.saveTaskIsAlive()){
					KyoroLogcatTaskManagerForSave.stopSaveTask(getApplicationContext());
				}
				else {
					KyoroLogcatTaskManagerForSave.startSaveTask(getApplicationContext());
				}
			}
			else if (KyoroSaveWidget.ACTION_SET.equals(action)){
				if(KyoroLogcatTaskManagerForSave.saveTaskIsAlive()){
					KyoroSaveWidget.setStopImage(getApplicationContext());
				}
				else {
					KyoroSaveWidget.setSaveImage(getApplicationContext());
				}			
			}
		}
		else if(KyoroMailWidget.TYPE.equals(""+intent.getType())) {
			KyoroMailWidget.setSendMailImage(getApplicationContext());
			if(KyoroSaveWidget.ACTION_CHANGE.equals(action)){
				SendCurrentLogTask task = new SendCurrentLogTask(this.getApplicationContext());
				task.start();
			}
		}
		try {
			finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
