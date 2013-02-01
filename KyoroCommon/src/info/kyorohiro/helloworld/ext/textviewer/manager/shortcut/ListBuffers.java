package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher;
import info.kyorohiro.helloworld.display.simple.MessageDispatcher.Receiver;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferList;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBufferJob;
import info.kyorohiro.helloworld.ext.textviewer.manager.message.ListBuffersReceiver;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile.FindFileJob;
import info.kyorohiro.helloworld.ext.textviewer.manager.task.ListBufferTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.AutocandidateList;

public class ListBuffers implements Task {

	private WeakReference<TextViewer> mViewer = null;

	@Override
	public String getCommandName() {
		return "list-buffers";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		TextViewer target = BufferManager.getManager().getFocusingTextViewer();
		if(target == null || view != target.getLineView()) {
			return;
		}
		BufferManager.getManager().getMiniBuffer().startMiniBufferJob(new ListBuffersJob(target));
		buffer.clearYank();
	}

	public static class ListBuffersJob implements MiniBufferJob {
		private ListBufferTask mUpdate = null;
		private WeakReference<TextViewer> mViewer = null;

		public ListBuffersJob(TextViewer viewer) {
			mViewer = new WeakReference<TextViewer>(viewer);
		}

		@Override
		public void enter(String line) {
		}

		@Override
		public void tab(String line) {
		}

		@Override
		public void begin() {
			// 
			BufferManager.getManager().beginInfoBuffer();
			BufferManager.getManager().changeFocus(BufferManager.getManager().getMiniBuffer());
			// clear
			MiniBuffer miniBuffer = BufferManager.getManager().getMiniBuffer();
			EditableLineViewBuffer miniBufferEdiableBuffer = (EditableLineViewBuffer)miniBuffer.getLineView().getLineViewBuffer();
			miniBufferEdiableBuffer.clear();
			//
			miniBuffer.getLineView().getLeft().setCursorCol(0);
			miniBuffer.getLineView().getLeft().setCursorRow(0);
			
			//
			miniBuffer.getLineView().recenter();
			
			//
			miniBuffer.startTask(mUpdate = new ListBufferTask(BufferManager.getManager().getInfoBuffer()));

			//
			MessageDispatcher.getInstance().addReceiver(new ListBuffersReceiver(this, mViewer.get()));

		}

		@Override
		public void end() {
		}
	}


}
