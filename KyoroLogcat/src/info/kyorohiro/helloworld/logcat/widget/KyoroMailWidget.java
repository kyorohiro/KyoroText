package info.kyorohiro.helloworld.logcat.widget;

import info.kyorohiro.helloworld.logcat.R;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

public class KyoroMailWidget extends KyoroWidgetBase {
	public static String TYPE = "send";

	public KyoroMailWidget() {
		super(R.layout.widget_mail, new int[]{R.id.widget_mail,R.id.widget_mail_img,R.id.widget_mail_text}, TYPE);
	}
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		setSendMailImage(context);
	}

	public static void setSendMailImage(Context context){
		chagneImage(context, R.drawable.ic_send_mail);
	}

	protected static void chagneImage(Context context, int id) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_mail);
		remoteViews.setImageViewResource(R.id.widget_mail_img, id);
		ComponentName cn = new ComponentName(context.getPackageName(), KyoroMailWidget.class.getName());
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(cn, remoteViews);
	}

}
