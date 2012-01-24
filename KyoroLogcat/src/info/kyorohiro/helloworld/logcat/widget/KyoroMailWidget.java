package info.kyorohiro.helloworld.logcat.widget;

import info.kyorohiro.helloworld.logcat.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
public class KyoroMailWidget extends AppWidgetProvider {
	public static String TYPE = "send";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		setSendMailImage(context);
	}

	public static void setSendMailImage(Context context){
		chagneImage(context, R.drawable.ic_send_mail);
	}

	protected static void chagneImage(Context context, int id) {
		Intent intentForClickAction = KyoroWidgetService.getIntentToStartButtonAction(context, TYPE);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_mail);
		views.setImageViewResource(R.id.widget_mail_img, id);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentForClickAction, 0);
		views.setOnClickPendingIntent(R.id.widget_mail, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_mail_img, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_mail_text, pendingIntent);
		ComponentName cn = new ComponentName(context.getPackageName(), KyoroMailWidget.class.getName());
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(cn, views);
	}

	public static void set(Context context) {
		Intent intentForClickAction = KyoroWidgetService.getIntentToStartButtonAction(context, TYPE);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_mail);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentForClickAction, 0);
		views.setOnClickPendingIntent(R.id.widget_mail, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_mail_img, pendingIntent);
		views.setOnClickPendingIntent(R.id.widget_mail_text, pendingIntent);

		ComponentName thisWidget = new ComponentName(context, KyoroMailWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
	}
}
