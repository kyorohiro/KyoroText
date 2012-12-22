package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.LineViewManager;

public class ISearchForward implements Task {

	@Override
	public String getCommandName() {
		return "isearch-forward";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		view.iSearchForward();
	}
	
}
