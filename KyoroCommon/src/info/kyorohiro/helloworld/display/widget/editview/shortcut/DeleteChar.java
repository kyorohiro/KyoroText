package info.kyorohiro.helloworld.display.widget.editview.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;

public class DeleteChar implements Task {
	@Override
	public String getCommandName() {
		return "delete-char";
	}
	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
	//	android.util.Log.v("kiyo","#key(1)=");
		buffer.deleteChar();
	//	android.util.Log.v("kiyo","#key(2)=");
		buffer.clearYank();
//		android.util.Log.v("kiyo","#key(3)=");
	}
 }
