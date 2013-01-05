package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;

public class SplitWindowHorizontally implements Task {

	@Override
	public String getCommandName() {
		return "split-window-horizontally";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
//		android.util.Log.v("kiyo","begin splitWindowVertically act");
		BufferManager.getManager().splitWindowHorizontally();	
//		android.util.Log.v("kiyo","begin splitWindowVertically --");
		buffer.clearYank();
//		android.util.Log.v("kiyo","begin splitWindowVertically act");
	}

}