package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;

import info.kyorohiro.helloworld.pfdep.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivityNewShellBufferAction implements MainActivityMenuAction {

	public static String TITLE = "new shell buffer";
	private BufferManager mDisplayedTextViewer = null;

	public MainActivityNewShellBufferAction(BufferManager viewer) {
		mDisplayedTextViewer = viewer;
	}

	private boolean isOn() {
		TextViewer viewer = BufferManager.getManager().getFocusingTextViewer();
		TextViewer mini = BufferManager.getManager().getMiniBuffer();
		TextViewer info = BufferManager.getManager().getInfoBuffer();
		if (viewer == null || viewer == mini || viewer == info) {
			return false;
		} else {
			return true;
		}
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		if (!isOn()) {
			return false;
		}
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId,
			MenuItem item) {
		if (!isOn()) {
			return false;
		}
		if (item.getTitle().equals(TITLE)) {
			startWork();
			return true;
		}
		return false;
	}

	public void startWork() {
		Thread t = new Thread(new A());
		t.start();
	}

	public static int NUM=0;
	public class A implements Runnable {
		public void run() {
			File f= new File(""+KyoroSetting.getCurrentFile());
			if(f.getParentFile() != null&& f.getParentFile().exists()) {
				CrossCuttingProperty.getInstance().setProperty("user.dir",f.getParent());				
			}
			BufferManager.getManager().createShellBuffer("");
		}
	}
}
