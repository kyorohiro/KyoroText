package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;
import java.lang.ref.WeakReference;

import info.kyorohiro.helloworld.pfdep.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.pfdep.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.pfdep.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.KyoroTextViewerActivity;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.SaveBuffer;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;


//
// ���̃R�[�h�͋߁X�폜����B
// �� ���j���[�̎g�p�͂�߂�\��Ȃ̂ŁA�����Ηǂ������Ƃ���B
public class MainActivitySaveFileAction implements MainActivityMenuAction {

	public static String TITLE = "save";
	private BufferManager mViewer = null;

	public MainActivitySaveFileAction(BufferManager viewer) {
		mViewer = viewer;
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId,
			MenuItem item) {
		if (item.getTitle().equals(TITLE)) {
			save();
			return true;
		}
		return false;
	}
	private void save() {
		TextViewer target = BufferManager.getManager().getFocusingTextViewer();
		SaveBuffer buffer = new SaveBuffer();
		buffer.act(target.getLineView(), (EditableLineViewBuffer)target.getLineView().getLineViewBuffer());
	}

}
