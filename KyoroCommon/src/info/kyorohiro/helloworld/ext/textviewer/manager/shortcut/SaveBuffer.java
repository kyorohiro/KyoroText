package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.File;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.task.SaveTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

public class SaveBuffer implements Task {

	@Override
	public String getCommandName() {
		return "save-buffer";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		BufferManager.getManager().getApplication().showMessage("save buffer");
		TextViewer target = BufferManager.getManager().getFocusingTextViewer();
		if(target.getCurrentPath() == null || target.getCurrentPath().length() == 0) {
			BufferManager.getManager().getApplication().showMessage("failed to save: unset file path");			
		}
		else if(target.getLineView() == view) {
			BufferManager.getManager().getModeLineBuffer().startTask(new SaveTask(target, new File(target.getCurrentPath())));
		} else {
			BufferManager.getManager().getApplication().showMessage("failed to save");			
		}
	}

}
