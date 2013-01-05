package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class SingleByteTabTask implements Task {
	@Override
	public String getCommandName() {
		return "single_byte_tab_task";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		buffer.pushCommit(""+((char)0x09), 1);
		buffer.clearYank();
	}
 }