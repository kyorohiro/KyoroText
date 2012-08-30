package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;
import java.lang.ref.WeakReference;

import info.kyorohiro.helloworld.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.KyoroTextViewerActivity;
import info.kyorohiro.helloworld.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.textviewer.util.Util;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivityOpenFileAction implements MainActivityMenuAction {

	public static String TITLE = "open";
	private LineViewManager mViewer = null;

	public MainActivityOpenFileAction(LineViewManager viewer) {
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

	private WeakReference<Activity> mRefActivity = null;
	private void showExplorer(Activity activity) {
		mRefActivity = new WeakReference<Activity>(activity);
		File directory = new File(KyoroSetting.getCurrentFile());
		File firstCandidateDirectory = directory.getParentFile();
		File secondCandidateDirectory = Environment.getExternalStorageDirectory();
		File thirdCandidateDirectory = Environment.getRootDirectory();

		File showedDirectry = firstCandidateDirectory;
		if (showedDirectry == null || !showedDirectry.exists() || !showedDirectry.canRead()) {
			showedDirectry = secondCandidateDirectory;
		} 

		if (showedDirectry == null || !showedDirectry.exists()||!showedDirectry.canRead()) {
			showedDirectry = thirdCandidateDirectory;
		} 

		SimpleFileExplorer dialog = SimpleFileExplorer.createDialog(activity, showedDirectry);
		((KyoroTextViewerActivity)activity).stopStage();
		dialog.show();
		dialog.setOnSelectedFileAction(new SelectedFileAction() {
			@Override
			public boolean onSelectedFile(File file, String action) {
				if (file.exists() && file.isFile()) {
					if(mViewer.getFocusingTextViewer().readFile(file)&& mRefActivity!=null){
						Activity a = mRefActivity.get();
						if(a!=null){
							// todo refactraing 
							// reset Intent. for open current showing file 
							// when restart this application.
							a.setIntent(null);
						}
					}

					return true;
				}
				return false;
			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {			
			@Override
			public void onCancel(DialogInterface dialog) {
				if(mRefActivity!=null){
					Activity a = mRefActivity.get();
					((KyoroTextViewerActivity)a).startStage();
				}
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(mRefActivity!=null){
					Activity a = mRefActivity.get();
					((KyoroTextViewerActivity)a).startStage();
				}
			}
		});
	}
}
