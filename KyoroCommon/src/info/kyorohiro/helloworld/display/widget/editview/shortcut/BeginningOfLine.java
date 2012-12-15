package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class BeginningOfLine implements Task {
	@Override
	public String getCommandName() {
		return "beginning-of-line";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		buffer.setCursor(0, buffer.getCol());
		view.getLeft().setCursorCol(buffer.getCol());
		view.getLeft().setCursorRow(0);
	}
 }
