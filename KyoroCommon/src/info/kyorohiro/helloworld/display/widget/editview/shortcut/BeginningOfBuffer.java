package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class BeginningOfBuffer implements Task {
	@Override
	public String getCommandName() {
		return "beginning-of-buffer";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		buffer.setCursor(0, 0);
		view.setCursorAndCRLF(view.getLeft(), 0, 0);
		view.recenter();
		buffer.clearYank();
	}
 }