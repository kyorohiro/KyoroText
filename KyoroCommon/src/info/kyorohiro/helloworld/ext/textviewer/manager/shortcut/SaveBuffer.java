package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.File;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.task.SaveTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

public class SaveBuffer implements Task {

	@Override
	public String getCommandName() {
		return "save-buffer";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		LineViewManager.getManager().getApplication().showMessage("save buffer");
		TextViewer target = LineViewManager.getManager().getFocusingTextViewer();
		if(target.getCurrentPath() == null || target.getCurrentPath().length() == 0) {
			LineViewManager.getManager().getApplication().showMessage("failed to save: unset file path");			
		}
		else if(target.getLineView() == view) {
			LineViewManager.getManager().getModeLineBuffer().startTask(new SaveTask(target, new File(target.getCurrentPath())));
		} else {
			LineViewManager.getManager().getApplication().showMessage("failed to save");			
		}
	}

}
