package info.kyorohiro.helloworld.textviewer;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import info.kyorohiro.helloworld.android.base.ForegroundService;

public class KyoroServiceForForgroundApp extends ForegroundService {
	public KyoroServiceForForgroundApp() {
		super(1);
	}

	public static Intent startForgroundService(Context context, String message) {
		Intent startIntent = new Intent(context,
				KyoroServiceForForgroundApp.class);
		if (message != null) {
			startIntent.putExtra("message", message);
		}
		context.startService(startIntent);
		return startIntent;
	}

	public void stopForgroundService(Context context, Intent intent) {
		context.stopService(intent);
	}

	@Override
	public void onStartHandle(Intent intent) {
		super.onStartHandle(intent);
		String message = ".....";
		if (intent != null && intent.getExtras() != null
				&& intent.getExtras().getString("message") != null) {
			message = intent.getExtras().getString("message");
		}
		startForgroundAtOnGoing(R.drawable.ic_launcher, "textviewer", message,
				KyoroTextViewerActivity.class);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}
