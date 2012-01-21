package info.kyorohiro.helloworld.logcat.widget;

import info.kyorohiro.helloworld.logcat.R;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

public class KyoroSaveWidget extends KyoroWidgetBase {
	public static String TYPE = "save";
	public KyoroSaveWidget() {
		super(R.layout.widget_save, new int[]{R.id.widget_save,R.id.widget_save_img,R.id.widget_save_text}, TYPE);
	}

	public static void setSaveImage(Context context){
		chagneImage(context, R.drawable.ic_start_save);
	}

	public static void setStopImage(Context context){
		chagneImage(context, R.drawable.ic_stop_save);
	}

	protected static void chagneImage(Context context, int id) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_save);
		remoteViews.setImageViewResource(R.id.widget_save_img, id);
		ComponentName cn = new ComponentName(context.getPackageName(), KyoroSaveWidget.class.getName());
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(cn, remoteViews);
	}

}
