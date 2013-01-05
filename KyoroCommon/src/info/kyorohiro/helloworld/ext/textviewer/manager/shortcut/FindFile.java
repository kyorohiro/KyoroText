package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward.ISearchForwardTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.util.AutocandidateList;

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
		private UpdateInfo mUpdate = null;

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
			File pf = lf.getParentFile();
			MiniBuffer modeBuffer = BufferManager.getManager().getMiniBuffer();
			if(mCurrentPath.getAbsoluteFile().equals(lf.getAbsoluteFile())) {
				//android.util.Log.v("kiyo","##--------3------------"+mCurrentPath+","+pf.getAbsolutePath());
				//android.util.Log.v("kiyo","##--------2------------");
				mCurrentPath = lf.getAbsoluteFile();
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
			} else {
				File cp = mCurrentPath.getParentFile();
			//	if(!cp.exists()) {
			//		cp = mCurrentPath;
			//	}
				if(cp != null&&cp.equals(pf)) {
					if(mUpdate != null) {
						String candidate = mUpdate.getCandidate(lf.getName());
						//android.util.Log.v("kiyo","##---4--"+candidate+","+lf.getName());
						if(lf.getName().length()<candidate.length()) {
							//					MiniBuffer buffer = BufferManager.getManager().getMiniBuffer();
							EditableLineViewBuffer buffer = (EditableLineViewBuffer)modeBuffer.getLineView().getLineViewBuffer();
							buffer.clear();
							modeBuffer.getLineView().getLeft().setCursorCol(0);
							modeBuffer.getLineView().getLeft().setCursorRow(0);
							//
							File t = new File(pf,candidate);
							buffer.pushCommit(t.getAbsolutePath(), 1);
							modeBuffer.getLineView().recenter();
						}
					}
				}
//				android.util.Log.v("kiyo","##--------4------------"+mCurrentPath+","+lf.getAbsolutePath());
				if(lf.exists() && lf.isDirectory()) {
					mCurrentPath = lf.getAbsoluteFile();
					modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), new File(line), ""));
				} else if(pf.exists()){
					mCurrentPath = lf.getAbsoluteFile();
					modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), pf, lf.getName()));	
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
			modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), mCurrentPath = base.getAbsoluteFile(),""));
		}
		@Override
		public void end() {
			BufferManager.getManager().endInfoBuffer();			
		}
	}

	public static class UpdateInfo implements Runnable {
		private TextViewer mInfo = null;
		private File mPath = null;
		private String mFilter = "";
		private AutocandidateList mCandidate = new AutocandidateList(); 
		public UpdateInfo(TextViewer info, File path, String filter) {
			mInfo = info;
			mPath = path;
			mFilter = filter;
		}

		public String getCandidate(String c) {
			return mCandidate.candidateText(c);
		}

		@Override
		public void run() {
			try {
				int c=0;
				mCandidate.clear();
				MyFilter filter = new MyFilter(mFilter);
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
					for(File f : mPath.listFiles(filter)) {
						if(c<100){
							c++;
							mCandidate.add(f.getName());
						}
						buffer.pushCommit(""+f.getName()+(f.isDirectory()?"/":""), 1);
						buffer.crlf();
					}
				}
			} catch(Throwable t) {
				t.printStackTrace();
			}
		}
	}
	public static class MyFilter implements FilenameFilter {
		String mFilter = "";
		public MyFilter(String filter) {
			mFilter = filter;
		}

		@Override
		public boolean accept(File dir, String filename) {
			if(filename.startsWith(mFilter)) {
				//android.util.Log.v("kiyo","aa="+filename+","+mFilter);
				return true;
			}
			return false;
		}
	}

}