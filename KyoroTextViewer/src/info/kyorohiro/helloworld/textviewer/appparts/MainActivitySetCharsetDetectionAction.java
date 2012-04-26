package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.SortedMap;

import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.util.CharsetDetectorSample;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivitySetCharsetDetectionAction implements MainActivityMenuAction {

	public static String TITLE = "auto charset detection";
	private TextViewer mDisplayedTextViewer = null;

	public MainActivitySetCharsetDetectionAction(TextViewer viewer) {
		mDisplayedTextViewer = viewer;
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId, MenuItem item) {
		if(item.getTitle().equals(TITLE)) {
//			showDialog(activity);
			Thread th = new Thread(new AutoDetection());
			th.start();
			return true;
		}
		return false;
	}

	private void showDialog(Activity activity) {
		DialogForShowDeviceSupportCharset dialog = new DialogForShowDeviceSupportCharset(activity);
		dialog.show();
	}

	public class DialogForShowDeviceSupportCharset extends Dialog {
		public DialogForShowDeviceSupportCharset(Context context) {
			super(context);
		}
	}

	public class AutoDetection implements Runnable {
		@Override
		public void run() {
			String path = mDisplayedTextViewer.getCurrentPath();
			if(path == null || path.equals("")){
				return;
			}
			File f = new File(path);
			if(f==null||!f.exists()|| !f.canRead() || !f.isFile()){
				return;
			}
			CharsetDetectorSample det = new CharsetDetectorSample();
			det.detect(f);
		}
		
	}
}
