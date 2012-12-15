package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class KillLine implements Task {
	@Override
	public String getCommandName() {
		return "kill-line";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		int _c = buffer.getCol();
		if (0 <= _c && _c < buffer.getNumberOfStockedElement()) {
			buffer.killLine();
		}
	}
 }