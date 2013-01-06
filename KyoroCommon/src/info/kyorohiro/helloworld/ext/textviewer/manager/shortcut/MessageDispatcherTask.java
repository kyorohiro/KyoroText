package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;

public class MessageDispatcherTask implements Task {

	@Override
	public String getCommandName() {
		return "message-dispatcher";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		TextViewer viewer = BufferManager.getManager().getFocusingTextViewer();
		if(viewer == null) {
			return;
		}
		if(viewer.isDispose()) {
			return;
		}

		EditableLineView v = viewer.getLineView();
		int index = v.getLeft().getCursorCol();
		try {
			KyoroString message = v.getKyoroString(index);
			if(message != null) {
				MessageDispatcher.getInstance().send(message);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
