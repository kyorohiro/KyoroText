package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.File;
import java.io.IOException;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward.ISearchForwardTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

public class FindFile implements Task {

	@Override
	public String getCommandName() {
		return "find-file";
	}

	@Override
	public void act(EditableLineView view, EditableLineViewBuffer buffer) {
		TextViewer target = BufferManager.getManager().getFocusingTextViewer();
		if(target == null || view != target.getLineView()) {
			return;
		}
		BufferManager.getManager().getMiniBuffer().startMiniBufferTask(new FindFileTask(target));
		buffer.clearYank();
	}

	public class FindFileTask implements MiniBufferTask {
		private TextViewer mViewer = null;
		private File mCurrentPath  = new File("");
		public FindFileTask(TextViewer viewer) {
			mViewer = viewer;
		}
		@Override
		public void enter(String line) {
			File newFile = new File(line);
			File parent = newFile.getParentFile();
			if(!parent.exists()) {
				parent.mkdirs();
			}
			if(!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			try {
				mViewer.readFile(newFile, false);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
			
		@Override
		public void tab(String line) {
			File lf = new File(line);
			if(!mCurrentPath.getAbsoluteFile().equals(lf.getAbsoluteFile())) {
				//android.util.Log.v("kiyo","##--------1------------");
				MiniBuffer modeBuffer = BufferManager.getManager().getMiniBuffer();
				//EditableLineViewBuffer buffer = (EditableLineViewBuffer)modeBuffer.getLineView().getLineViewBuffer();
				//buffer.clear();
				mCurrentPath = lf.getAbsoluteFile();
				modeBuffer.startTask(new UpdateInfo(BufferManager.getManager().getInfoBuffer(), new File(line)));
			} else {
				//android.util.Log.v("kiyo","##--------2------------");
				TextViewer mInfo = BufferManager.getManager().getInfoBuffer();
				if(mInfo == null || mInfo.isDispose()) {
					return;
				}
				int num = mInfo.getLineView().getShowingTextStartPosition();
				int p = mInfo.getLineView().getPositionY();
				int v = mInfo.getLineView().getShowingTextSize();
				//android.util.Log.v("kiyo","##p="+p+",num="+num+",v="+v);
				if(0<num) {
					mInfo.getLineView().setPositionY(mInfo.getLineView().getPositionY()+3);
				} else {
					mInfo.getLineView().setPositionY(0);					
				}
			}
		}

		@Override
		public void begin() {
			//
			File target = new File(mViewer.getCurrentPath());
			File base = target.getParentFile();
			// todo 
			BufferManager.getManager().beginInfoBuffer();
			BufferManager.getManager().changeFocus(BufferManager.getManager().getMiniBuffer());
			MiniBuffer modeBuffer = BufferManager.getManager().getMiniBuffer();
			EditableLineViewBuffer buffer = (EditableLineViewBuffer)modeBuffer.getLineView().getLineViewBuffer();
			buffer.clear();
			modeBuffer.getLineView().getLeft().setCursorCol(0);
			modeBuffer.getLineView().getLeft().setCursorRow(0);
			buffer.pushCommit(""+base.getAbsolutePath(), 1);
			modeBuffer.getLineView().recenter();
			modeBuffer.startTask(new UpdateInfo(BufferManager.getManager().getInfoBuffer(), mCurrentPath = base.getAbsoluteFile()));
		}
		@Override
		public void end() {
			BufferManager.getManager().endInfoBuffer();			
		}
	}

	public static class UpdateInfo implements Runnable {
		private TextViewer mInfo = null;
		private File mPath = null;
		public UpdateInfo(TextViewer info, File path) {
			mInfo = info;
			mPath = path;
		}

		@Override
		public void run() {
			try {
				EditableLineViewBuffer buffer = (EditableLineViewBuffer)mInfo.getLineView().getLineViewBuffer();
				buffer.clear();
				if(mPath == null) {
					return;
				}
				if(!mPath.isDirectory()&&mPath.isFile()) {
					buffer.pushCommit(""+mPath.getName(), 1);
					buffer.crlf();
				}
				if(mPath.isDirectory()) {
					for(File f : mPath.listFiles()) {
						buffer.pushCommit(""+f.getName(), 1);
						buffer.crlf();
					}
				}
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}
}