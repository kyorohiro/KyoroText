package info.kyorohiro.helloworld.logcat.widget;

import info.kyorohiro.helloworld.logcat.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class KyoroSaveWidget extends AppWidgetProvider {
	public static String TYPE = "save";


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		KyoroWidgetService.setWidgetImage(context, TYPE);
	}

	public static void setSaveImage(Context context){
		chagneImage(context, R.drawable.ic_start_save);
	}

	public static void setStopImage(Context context){
		chagneImage(context, R.drawable.ic_stop_save);
	}

	protected static void chagneImage(Context context, int id) {
		Intent intentForClickAction = KyoroWidgetService.getIntentToStartButtonAction(context, TYPE);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_save);
		views.setImageViewResource(R.id.widget_save_img, id);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentForClickAction, 0);
		views.setOnClickPendingIntent(R.id.widget_save, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_save_img, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_mail_text, pendingIntent);
		ComponentName cn = new ComponentName(context.getPackageName(), KyoroSaveWidget.class.getName());
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(cn, views);
	}

	public static void set(Context context) {
		Intent intentForClickAction = KyoroWidgetService.getIntentToStartButtonAction(context, TYPE);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_save);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentForClickAction, 0);
		views.setOnClickPendingIntent(R.id.widget_save, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_save_img, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_mail_text, pendingIntent);

		ComponentName thisWidget = new ComponentName(context, KyoroSaveWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
	}
}
