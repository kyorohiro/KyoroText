package info.kyorohiro.helloworld.textviewer.appparts;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivityOpenFileAction extends  MainActivityMenuAction {

	public static String TITLE = "open";
	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId, MenuItem item) {
		return false;
	}
}
