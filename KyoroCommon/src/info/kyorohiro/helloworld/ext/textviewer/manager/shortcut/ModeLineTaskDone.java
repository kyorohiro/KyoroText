package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.ModeLineBuffer;

public class ModeLineTaskDone implements Task {

	@Override
	public String getCommandName() {
		return "mode-line-task-done";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		ModeLineBuffer mBuffer = BufferManager.getManager().getModeLineBuffer();
		mBuffer.done();
	}
	
}
