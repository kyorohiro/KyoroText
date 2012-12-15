package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.text.KyoroString;

public class EndOfBuffer implements Task {

	@Override
	public String getCommandName() {
		return "end-of-buffer";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		int len = buffer.getNumberOfStockedElement();
		KyoroString ks = null;
		if (len - 1 >= 0) {
			ks = buffer.get(len - 1);
		}
		if (ks != null) {
			buffer.setCursor(
					ks.lengthWithoutLF(view
							.isCrlfMode()), len - 1);
			view.setCursorAndCRLF(view.getLeft(),
					buffer.getRow(),
					buffer.getCol());
		}
		view.recenter();
		buffer.clearYank();
	}

}
