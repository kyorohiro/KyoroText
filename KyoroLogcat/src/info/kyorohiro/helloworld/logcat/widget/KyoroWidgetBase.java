package info.kyorohiro.helloworld.logcat.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class KyoroWidgetBase extends AppWidgetProvider {
	// log tag
	public static String ACTION_CHANGE = "ACTION_CHANGE";
	public static String ACTION_SET = "ACTION_SET";

	private int mLayoutId = 0;
	private int[] mClickableViewIds = null;
	private String mType = "";

	public KyoroWidgetBase(int layoutId, int[] clickableViewIds, String type) {
		mLayoutId = layoutId;
		mClickableViewIds = clickableViewIds.clone();
		mType = type;
	}

	@Override
	public void onEnabled(Context context) {
		Toast.makeText(context, "onEnable", Toast.LENGTH_LONG);
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Toast.makeText(context, "onUpdate", Toast.LENGTH_LONG);
		KyoroWidgetService.setWidgetImage(context, mType);
		Intent intentForClickAction = KyoroWidgetService.getIntentToStartButtonAction(context, mType);
		intentForClickAction.setType(mType);
		setClickAction(intentForClickAction, context, appWidgetManager,
				appWidgetIds);
	}

	private void setClickAction(Intent intentForClick, Context context,
			AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews views = new RemoteViews(context.getPackageName(), mLayoutId);
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intentForClick, 0);
		for(int id : mClickableViewIds){
			views.setOnClickPendingIntent(id, pendingIntent);
		}

		appWidgetManager.updateAppWidget(appWidgetIds, views);
	}

}
