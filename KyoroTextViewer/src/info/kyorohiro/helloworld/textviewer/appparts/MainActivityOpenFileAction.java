package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

import info.kyorohiro.helloworld.pfdep.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.pfdep.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.pfdep.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.KyoroTextViewerActivity;
import info.kyorohiro.helloworld.textviewer.appparts.MenuActionWarningMessagePlus.MyTask;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile.FindFileTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivityOpenFileAction implements MainActivityMenuAction {

	public static String TITLE = "open";
	private BufferManager mViewer = null;

	public MainActivityOpenFileAction(BufferManager viewer) {
		mViewer = viewer;
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId,
			MenuItem item) {
		if (item.getTitle().equals(TITLE)) {
			 MenuActionWarningMessagePlus.showDialog(activity, new  MyTask() {
				 public void run(Activity c){
					//showExplorer(c);
					 TextViewer viewer = BufferManager.getManager().getFocusingTextViewer();
					 if(viewer != null) {
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
							FindFileTask t = new FindFileTask(viewer, showedDirectry);
							BufferManager.getManager().getMiniBuffer().startMiniBufferTask(t);
					 }
				 }
			 }, BufferManager.getManager().getFocusingTextViewer().isEdit());

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
					try {
						if(mViewer.getFocusingTextViewer().readFile(file, true)&& mRefActivity!=null){
							Activity a = mRefActivity.get();
							if(a!=null){
								// todo refactraing 
								// reset Intent. for open current showing file 
								// when restart this application.
								a.setIntent(null);
							}
						}
					} catch (FileNotFoundException e) {
						KyoroApplication.showMessage("file can not read null file");
						e.printStackTrace();
					} catch (NullPointerException e) {
						KyoroApplication.showMessage("file can not read " + file.toString());
						e.printStackTrace();
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
