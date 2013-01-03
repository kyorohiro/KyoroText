package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward.ISearchForwardTask;

public class DeleteWindow implements Task {

	@Override
	public String getCommandName() {
		return "delete-window";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		BufferManager.getManager().deleteWindow();
		buffer.clearYank();
	}

}
