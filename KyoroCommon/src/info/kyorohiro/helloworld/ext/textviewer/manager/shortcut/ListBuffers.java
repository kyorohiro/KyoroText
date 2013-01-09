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
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile.FindFileTask;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.FindFile.MyReceiver;
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
		BufferManager.getManager().getMiniBuffer().startMiniBufferTask(new ListBuffersTask(target));
		buffer.clearYank();
	}

	public static class ListBuffersTask implements MiniBufferTask {
		private UpdateInfo mUpdate = null;
		private WeakReference<TextViewer> mViewer = null;

		public ListBuffersTask(TextViewer viewer) {
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
			miniBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer()));

			//
			MessageDispatcher.getInstance().addReceiver(new MyListBuffersReceiver(this, mViewer.get()));

		}

		@Override
		public void end() {
		}
	}

	public static class UpdateInfo implements Runnable {
		private TextViewer mInfo = null;
		private AutocandidateList mCandidate = new AutocandidateList(); 
		public UpdateInfo(TextViewer info) {
			mInfo = info;
		}

		public String getCandidate(String c) {
			return mCandidate.candidateText(c);
		}

		@Override
		public void run() {
			try {
				int c=0;
				mCandidate.clear();
				EditableLineView infoBufferEViewer = mInfo.getLineView();
				EditableLineViewBuffer infoBufferEBuffer = (EditableLineViewBuffer)mInfo.getLineView().getLineViewBuffer();
				BufferList bufferList = BufferManager.getManager().getBufferList();
				infoBufferEViewer.setTextSize(BufferManager.getManager().getBaseTextSize());
				infoBufferEBuffer.clear();

				{
					for(int i=0;i<bufferList.size();i++) {
						TextViewer tmp = bufferList.getTextViewer(i);
						if(tmp != null && !tmp.isDispose()) {
							infoBufferEBuffer.getDiffer().asisSetType("list-buffers");
							infoBufferEBuffer.getDiffer().asisSetExtra(""+i+":"+tmp.getCurrentPath());
							infoBufferEBuffer.pushCommit(""+i+":   sorry now creating: "+tmp.getCurrentPath(), 1);
							infoBufferEBuffer.crlf(false, false);	
							infoBufferEBuffer.crlf(false, false);	
							infoBufferEBuffer.crlf();
						}
					}
				}
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public static class MyListBuffersReceiver implements Receiver {
		public static ListBuffersTask sTask = null;
		private WeakReference<ListBuffersTask> mTask = null;
		private TextViewer mTarget = null;
		public MyListBuffersReceiver(ListBuffersTask task, TextViewer target) {
			sTask = task;
			mTask = new WeakReference<ListBuffersTask>(task);
			mTarget = target;
		}


		@Override
		public String getType() {
			return "list-buffers";
		}

		@Override
		public void onReceived(KyoroString message, String type) {
//			android.util.Log.v("kiyo","rev="+message);
			ListBuffersTask task = mTask.get();

			if(task != null) {
				//task.input(message.toString());
				//task.input(message.getExtra());
				//task.tab(message.getExtra());
				String[] sp = message.getExtra().split(":");
				
				if(sp != null && sp.length >= 1) {
//					android.util.Log.v("kiyo","rev--1--"+sp[0]);
					int pos = Integer.parseInt(sp[0]);
//					android.util.Log.v("kiyo","rev--2--"+sp[0]);
//					BufferManager.getManager().otherWindow();
					TextViewer target = BufferManager.getManager().getBufferList().getTextViewer(pos);
//					target.dispose();
//					TextViewer viewer = BufferManager.getManager().newTextViewr();
					try {
//						android.util.Log.v("kiyo","rev--3--"+target.getCurrentPath());
//						viewer.readFile(new File(target.getCurrentPath()), false);
						change(target.getCurrentPath());
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					android.util.Log.v("kiyo","rev--3--");
				}
			}
		}

		public void change(String path) {
			if(mTarget != null && !mTarget.isDispose()){
				try {
					mTarget.readFile(new File(path), false);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			BufferManager.getManager().convertTextViewer(newViewer);
		}
	}

}
