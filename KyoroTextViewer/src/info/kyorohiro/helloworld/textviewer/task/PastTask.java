package info.kyorohiro.helloworld.textviewer.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
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
//					LineViewManager.getManager().getCircleMenu().addCircleMenu(0, "Paste");
					KyoroApplication.getKyoroApplication().getHanler().postDelayed(new A(), 1000);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		});
	}

	//todo
	class A implements Runnable{
		@Override
		public void run() {
			LineViewManager.getManager().getCircleMenu().addCircleMenu(0, "Paste");			
		}
	}
}
