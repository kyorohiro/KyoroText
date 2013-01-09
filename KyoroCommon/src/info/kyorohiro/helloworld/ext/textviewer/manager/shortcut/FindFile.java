package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.LinkedList;

//import android.webkit.ConsoleMessage.MessageLevel;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher;
import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.MessageDispatcher.Receiver;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward.ISearchForwardTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.text.KyoroString;
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

	public static class FindFileTask implements MiniBufferTask {
//		private TextViewer mViewer = null;
		private WeakReference<TextViewer> mViewer = null;

		private File mCurrentPath  = new File("");
		private UpdateInfo mUpdate = null;

		public FindFileTask(TextViewer viewer, File path) {
			mViewer = new WeakReference<TextViewer>(viewer);
			mCurrentPath = path;
		}

		public FindFileTask(TextViewer viewer) {
			mViewer = new WeakReference<TextViewer>(viewer);
			mCurrentPath = new File(viewer.getCurrentPath());
		}

		public boolean isAlive() {
			TextViewer viewer = mViewer.get();
			if(viewer == null) {
				return false;
			}
			if(viewer.isDispose()) {
				return false;
			}
			return true;
		}
		@Override
		public void enter(String line) {
			File newFile = new File(line);
			File parent = newFile.getParentFile();
			TextViewer viewer = mViewer.get();
			if(viewer == null||!isAlive()) {
				return;
			}
			
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
				SimpleApplication app = BufferManager.getManager().getApplication();
				if(newFile.getParentFile().equals(app.getApplicationDirectory())) {
					viewer.readFile(newFile, false);
				} else {
					viewer.readFile(newFile, true);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		public void input(String mini) {
			TextViewer viewer = mViewer.get();
			if(viewer == null||!isAlive()) {
				return;
			}
			
			File path = null;
			File parent = mCurrentPath.getParentFile();
			mini = mini.replaceAll("\r\n|\n|", "");
//			if(mini.equals("..")) {
//				path = parent;
//			}else 
//			if(mCurrentPath.isDirectory()) {
//				path = new File(mCurrentPath, mini);
//			} else {
//				if(parent == null) {
//					return;
//				}
//				path = new File(parent, mini);
//			}
			path = new File(mini);
//			android.util.Log.v("kiyo","##--A-"+path.isFile());
//			android.util.Log.v("kiyo","##--A-"+path.isDirectory());
//			android.util.Log.v("kiyo","##--A->"+path.exists()+"<");
//			android.util.Log.v("kiyo","##--A->"+path+"<");
			if(!path.exists()) {
//				android.util.Log.v("kiyo","##--AZ-");				
				return;
			}
//			android.util.Log.v("kiyo","##--AV-");				
	
			MiniBuffer modeBuffer = BufferManager.getManager().getMiniBuffer();
			EditableLineViewBuffer buffer = (EditableLineViewBuffer)modeBuffer.getLineView().getLineViewBuffer();
			buffer.clear();
			modeBuffer.getLineView().getLeft().setCursorCol(0);
			modeBuffer.getLineView().getLeft().setCursorRow(0);
			//
			buffer.pushCommit(path.getAbsolutePath(), 1);
			modeBuffer.getLineView().recenter();
			//
			//
//			android.util.Log.v("kiyo","#------"+path);
			if(path.exists()&&!path.isDirectory()&&path.isFile()) {
//				android.util.Log.v("kiyo","##--------A-1-----------");
				enter(path.toString());
			} else {
//				android.util.Log.v("kiyo","##--------A-3-----------");
				tab(path.toString());
			}
			
		}

		@Override
		public void tab(String line) {
			TextViewer viewer = mViewer.get();
			if(viewer == null||!isAlive()) {
				return;
			}
			File lf = new File(line);
			File pf = lf.getParentFile();
			MiniBuffer modeBuffer = BufferManager.getManager().getMiniBuffer();
			if(mCurrentPath.getAbsoluteFile().equals(lf.getAbsoluteFile())) {
				//android.util.Log.v("kiyo","##--------3------------"+mCurrentPath+","+pf.getAbsolutePath());
//				android.util.Log.v("kiyo","##--------1------------");
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
//				android.util.Log.v("kiyo","##--------2------------");
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
//					android.util.Log.v("kiyo","##--------4-1-----------");
					mCurrentPath = lf.getAbsoluteFile();
					modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), new File(line), ""));
				} else if(pf.exists()){
//					android.util.Log.v("kiyo","##--------4-2-----------");
					mCurrentPath = lf.getAbsoluteFile();
					modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), pf, lf.getName()));	
				}
			}
		}

		@Override
		public void begin() {
			//
			File target = mCurrentPath;
			File base = target.getParentFile();
			if(base == null||target.isDirectory()){
				base = target;
			}
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
			
			//
			MessageDispatcher.getInstance().addReceiver(new MyReceiver(this));
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
				EditableLineView viewer = mInfo.getLineView();
				EditableLineViewBuffer buffer = (EditableLineViewBuffer)mInfo.getLineView().getLineViewBuffer();
				viewer.setTextSize(BufferManager.getManager().getBaseTextSize());
				buffer.clear();
				if(mPath == null) {
					return;
				}

				{
					File p = mPath.getParentFile();
					if(p!=null&&p.isDirectory()) {
						buffer.getDiffer().asisSetType("find");
						buffer.getDiffer().asisSetExtra(p.getAbsolutePath());
						buffer.pushCommit("..", 1);
						buffer.crlf(false, false);	
						buffer.crlf(false, false);	
						buffer.crlf();	
					}
				}
				if(!mPath.isDirectory()&&mPath.isFile()) {
					buffer.pushCommit(""+mPath.getName(), 1);
					buffer.crlf(false, false);
					buffer.crlf(false, false);	
					buffer.crlf();
				}
				if(mPath.isDirectory()) {
					int size = buffer.getDiffer().length();
					for(File f : mPath.listFiles(filter)) {
						c++;
						if(c<100){
							mCandidate.add(f.getName());
						}
						buffer.getDiffer().asisSetType("find");
						buffer.getDiffer().asisSetExtra(f.getAbsolutePath());
						buffer.pushCommit(""+f.getName()+(f.isDirectory()?"/":""), 1);
						buffer.crlf(false, false);
						buffer.crlf(false, false);	
						buffer.crlf();
						viewer.setPositionY(viewer.getPositionY()+(buffer.getDiffer().length()-size));
						size = buffer.getDiffer().length();
						if(c>1000) {
							break;
						}
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

	public static class MyReceiver implements Receiver {
		public static FindFileTask sTask = null;
		private WeakReference<FindFileTask> mTask = null;
		public MyReceiver(FindFileTask task) {
			sTask = task;
			mTask = new WeakReference<FindFileTask>(task);
		}

		@Override
		public String getType() {
			return "find";
		}

		@Override
		public void onReceived(KyoroString message, String type) {
//			android.util.Log.v("kiyo","rev="+message);
			FindFileTask task = mTask.get();
			if(!task.isAlive()){
				return;
			}
			if(task != null) {
				//task.input(message.toString());
				task.input(message.getExtra());
				//task.tab(message.getExtra());
			}
		}
	}
}