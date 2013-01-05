package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward.ISearchForwardTask;

public class FindFile implements Task {

	@Override
	public String getCommandName() {
		return "find-file";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		BufferManager.getManager().getMiniBuffer().startMiniBufferTask(new FindFileTask());
		buffer.clearYank();
	}

	public class FindFileTask implements MiniBufferTask {
		@Override
		public void enter(String line) {
		}

		@Override
		public void tab(String line) {
		}

		@Override
		public void begin() {
			BufferManager.getManager().beginInfoBuffer();
		}

		@Override
		public void end() {
		}

	}

}