package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;

import info.kyorohiro.helloworld.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivityOpenFileAction extends  MainActivityMenuAction {

	public static String TITLE = "open";
	private TextViewer mViewer = null;

	public MainActivityOpenFileAction(TextViewer viewer) {
		mViewer = viewer;
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId, MenuItem item) {
		if(item.getTitle().equals(TITLE)) {
			showExplorer(activity);
			return true;
		}
		return false;
	}

	private void showExplorer(Activity activity) {
		File firstCandidate  = Environment.getExternalStorageDirectory();
		File secoundCandidate = Environment.getRootDirectory();
		File candidate = firstCandidate;
		if(!firstCandidate.exists()||!firstCandidate.canRead()){
			candidate = secoundCandidate;
		}
		SimpleFileExplorer dialog = SimpleFileExplorer.createDialog(activity, candidate);
		dialog.show();
		dialog.setOnSelectedFileAction(new SelectedFileAction() {
			@Override
			public boolean onSelectedFile(File file, String action) {
				if(file.exists() && file.isFile()){
					mViewer.readFile(file);
					return true;
				}
 				return false;
			}
		});
	}
}
