package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class KeyboadQuit implements Task {

	@Override
	public String getCommandName() {
		return null;
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
	}

}
