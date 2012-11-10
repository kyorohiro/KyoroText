package info.kyorohiro.helloworld.textviewer.task;

import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineView;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.content.Context;
import android.text.ClipboardManager;

public class PastTask implements Runnable {
	public static void pasteStart() {
		KyoroApplication.getKyoroApplication().getHanler().post(new PastTask());
	}
	public void run() {
		KyoroApplication.getKyoroApplication().getHanler()
		.post(new Runnable() {
			public void run() {
				try {
					ClipboardManager cm = (ClipboardManager) KyoroApplication
							.getKyoroApplication().getSystemService(Context.CLIPBOARD_SERVICE);
					CharSequence clipdata = cm.getText();

					TextViewer textViewer = LineViewManager.getManager().getFocusingTextViewer();
					EditableLineView lineView = textViewer.getLineView();
					lineView.inputText(clipdata);
					LineViewManager.getManager().getCircleMenu().addCircleMenu(0, "Paste");
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		});
	}
}
