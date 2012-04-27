package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;

import info.kyorohiro.helloworld.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivityOpenFileAction implements MainActivityMenuAction {

	public static String TITLE = "open";
	private TextViewer mViewer = null;

	public MainActivityOpenFileAction(TextViewer viewer) {
		mViewer = viewer;
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId,
			MenuItem item) {
		if (item.getTitle().equals(TITLE)) {
			showExplorer(activity);
			return true;
		}
		return false;
	}

	private void showExplorer(Activity activity) {
		File directory = new File(KyoroSetting.getCurrentFile());
		File firstCandidateDirectory = directory.getParentFile();
		File secondCandidateDirectory = Environment.getExternalStorageDirectory();
		File thirdCandidateDirectory = Environment.getRootDirectory();

		File showedDirectry = firstCandidateDirectory;
		if (showedDirectry == null || !showedDirectry.exists() || !showedDirectry.canRead()) {
			showedDirectry = secondCandidateDirectory;
		} else if (showedDirectry == null || !showedDirectry.exists()||!showedDirectry.canRead()) {
			showedDirectry = thirdCandidateDirectory;
		} 

		SimpleFileExplorer dialog = SimpleFileExplorer.createDialog(activity,
				showedDirectry);
		dialog.show();
		dialog.setOnSelectedFileAction(new SelectedFileAction() {
			@Override
			public boolean onSelectedFile(File file, String action) {
				if (file.exists() && file.isFile()) {
					mViewer.readFile(file);
					return true;
				}
				return false;
			}
		});
	}
}
