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

	private boolean isOn() {
		TextViewer viewer = BufferManager.getManager().getFocusingTextViewer();
		TextViewer mini = BufferManager.getManager().getMiniBuffer();
		TextViewer info = BufferManager.getManager().getInfoBuffer();
		if(viewer == null||viewer == mini|| viewer == info) {
			return false;
		} else {
			return true;
		}
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		if(!isOn()) {
			menu.add("dummy");
			return false;
		}

		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId,
			MenuItem item) {
		if(!isOn()) {
			return false;
		}
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
							FindFileTask t = new FindFileTask(viewer, showedDirectry, true);
							BufferManager.getManager().getMiniBuffer().startMiniBufferJob(t);
					 }
				 }
			 }, BufferManager.getManager().getFocusingTextViewer().isEdit());

			return true;
		}
		return false;
	}
}
