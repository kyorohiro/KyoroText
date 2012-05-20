package info.kyorohiro.helloworld.memoryinfo;

import java.lang.ref.WeakReference;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

//
// This class make it an open possibility to easy access Context instance.
//
public class KyoroApplication extends Application {

	private static WeakReference<KyoroApplication> sInstanceReference = null;
	private Handler mHandler = null;

	public KyoroApplication() {
		init(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mHandler = new Handler();
	}

	public static synchronized void init(KyoroApplication app) {
		if(sInstanceReference != null) {
			sInstanceReference.clear();
		}
		sInstanceReference = new WeakReference<KyoroApplication>(app);				
	}

	public static  synchronized KyoroApplication getKyoroApplication() {
		KyoroApplication app = sInstanceReference.get();
		//if(app != null) {
			return app;
		//} else {
		//	// dummy
		//	return new KyoroApplication();
		//}
	}

	public static void showMessage(String message) {
		try {
			getKyoroApplication().getHanler().post(new ToastMessage(getKyoroApplication(), message));
		} catch(Throwable e){
			e.printStackTrace();
		}
	}

	public Handler getHanler(){
		return getKyoroApplication().mHandler;
	}

	private static class ToastMessage implements Runnable {
		private Context mContext = null;
		private String mMessage = null;
		public ToastMessage(Context c, String m) {
			mContext = c;
			mMessage = m;
		}
		public void run(){
			try {
				Toast.makeText(mContext.getApplicationContext(), mMessage, Toast.LENGTH_SHORT).show();
				mContext = null;
				mMessage = null;
			} catch(Throwable e){
				e.printStackTrace();
			} 
		}
	}
}
