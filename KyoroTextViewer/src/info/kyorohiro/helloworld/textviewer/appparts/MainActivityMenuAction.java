package info.kyorohiro.helloworld.textviewer.appparts;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class MainActivityMenuAction {

	public static String TITLE = "open";
	public abstract boolean onPrepareOptionsMenu(Activity activity, Menu menu);

	public  abstract boolean onMenuItemSelected(Activity activity, int featureId, MenuItem item);
}
