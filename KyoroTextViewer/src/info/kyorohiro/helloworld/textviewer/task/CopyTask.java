package info.kyorohiro.helloworld.textviewer.task;

import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import android.content.Context;
import android.text.ClipboardManager;

public class CopyTask implements Runnable {
	public static void copyStart() {
		KyoroApplication.getKyoroApplication().getHanler().post(new CopyTask());
	}
	public void run() {
		KyoroApplication.getKyoroApplication().getHanler()
		.post(new Runnable() {
			public void run() {
				try {
					CursorableLineView mLineView = LineViewManager.getManager().getFocusingTextViewer()
							.getLineView();
					ClipboardManager cm = (ClipboardManager) KyoroApplication
							.getKyoroApplication().getSystemService(Context.CLIPBOARD_SERVICE);
					CharSequence copy = mLineView.copy();
					cm.setText("" + copy);
					LineViewManager.getManager().getCircleMenu().addCircleMenu(0, "Copy");
					KyoroApplication.showMessage("" + copy);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		});
	}
}
