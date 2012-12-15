package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class FowardWord implements Task {
	@Override
	public String getCommandName() {
		return "foward-word";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		view.front();
		buffer.setCursor(view.getLeft().getCursorRow(), view.getLeft().getCursorCol());
		buffer.clearYank();
	}
 }