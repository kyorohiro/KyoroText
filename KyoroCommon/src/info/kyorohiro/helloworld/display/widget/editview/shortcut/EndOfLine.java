package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class EndOfLine implements Task {
	@Override
	public String getCommandName() {
		return "end-of-line";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		int _c = buffer.getCol();
		CharSequence _t = buffer.get(_c);
		buffer.setCursor(_t.length(), _c);
		buffer.clearYank();
	}
 }