package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class CrlfTask implements Task {
	@Override
	public String getCommandName() {
		return "yank";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		buffer.crlf();
		buffer.clearYank();
	}
 }