package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;

public class OtherWindow implements Task {

	@Override
	public String getCommandName() {
		return "other-window";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		BufferManager.getManager().otherWindow();
	}
	
}
