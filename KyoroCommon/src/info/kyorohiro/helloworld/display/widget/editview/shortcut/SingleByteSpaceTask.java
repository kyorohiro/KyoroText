package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class SingleByteSpaceTask implements Task {
	@Override
	public String getCommandName() {
		return "single_byte_space_task";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		buffer.pushCommit(" ", 1);
		buffer.clearYank();
	}
 }