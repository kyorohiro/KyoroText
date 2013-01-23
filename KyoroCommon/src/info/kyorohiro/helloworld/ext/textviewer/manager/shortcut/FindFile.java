package info.kyorohiro.helloworld.ext.textviewer.manager.shortcut;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import android.webkit.ConsoleMessage.MessageLevel;

import info.kyorohiro.helloworld.display.simple.MessageDispatcher;
import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.MessageDispatcher.Receiver;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.shortcut.KeyEventManager.Task;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.manager.MiniBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.message.FindFileReceiver;
import info.kyorohiro.helloworld.ext.textviewer.manager.shortcut.ISearchForward.ISearchForwardTask;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.AsyncronousTask;
import info.kyorohiro.helloworld.util.AutocandidateList;
import info.kyorohiro.helloworld.util.FileListGetter;

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
		BufferManager.getManager().getMiniBuffer().startMiniBufferJob(new FindFileTask(target));
		buffer.clearYank();
	}

	public static class FindFileTask implements MiniBufferJob {
//		private TextViewer mViewer = null;
		private WeakReference<TextViewer> mViewer = null;

		private File _mCurrentPath  = new File("");
		private File mCurrentDir  = new File("");
		private UpdateInfo mUpdate = null;
		private boolean mFirstFocosIsInfo = false;

		public void setCurrentPath(File path) {
			_mCurrentPath = path;
//			android.util.Log.v("kiyo","setCurrent="+path);
		}
		public File getCurrentFile() {
			return _mCurrentPath;
		}
		public FindFileTask(TextViewer viewer, File path, boolean firstFocosIsInfo) {
			mViewer = new WeakReference<TextViewer>(viewer);
			setCurrentPath(path);
			mFirstFocosIsInfo = firstFocosIsInfo;
		}

		public FindFileTask(TextViewer viewer, File path) {
			mViewer = new WeakReference<TextViewer>(viewer);
			setCurrentPath(path);
		}

		public FindFileTask(TextViewer viewer) {
			mViewer = new WeakReference<TextViewer>(viewer);
			setCurrentPath(new File(viewer.getCurrentPath()));
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
//			android.util.Log.v("kiyo","enter " + line);
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
					viewer.readFile(newFile);
				} else {
					viewer.readFile(newFile);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
//			android.util.Log.v("kiyo","/enter " + line);
			}
		}

		public void input(String mini) {
//			android.util.Log.v("kiyo","input " + mini);
			TextViewer viewer = mViewer.get();
			if(viewer == null||!isAlive()) {
//				android.util.Log.v("kiyo","in--1--");
				return;
			}

			MiniBuffer minibuffer = BufferManager.getManager().getMiniBuffer();
			minibuffer.startMiniBufferJob(this);
			File path = null;
			File parent = getCurrentFile();//mCurrentPath.getParentFile();

			mini = mini.replaceAll("\r\n|\n|", "");

			path = new File(mini);
			if(!path.exists()) {
//				android.util.Log.v("kiyo","in--2--");
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
//				android.util.Log.v("kiyo","in--3--");
//				android.util.Log.v("kiyo","##--------A-1-----------");
				enter(path.toString());
			} else {
//				android.util.Log.v("kiyo","in--4--");
//				android.util.Log.v("kiyo","##--------A-3-----------");
				tab(path.toString());
			}
			
		}

		@Override
		public void tab(String line) {
//			android.util.Log.v("kiyo","tab--1--");
//			android.util.Log.v("kiyo","tab " + line);
			TextViewer viewer = mViewer.get();
			if(viewer == null||!isAlive()) {
//				android.util.Log.v("kiyo","tab--2--");
				return;
			}
			File lf = new File(line);
			File pf = lf.getParentFile();
			MiniBuffer modeBuffer = BufferManager.getManager().getMiniBuffer();
			if(getCurrentFile().getAbsoluteFile().equals(lf.getAbsoluteFile())) {
//				android.util.Log.v("kiyo","tab--3--"+getCurrentFile().getAbsoluteFile());
//				android.util.Log.v("kiyo","tab--3--"+lf.getAbsoluteFile());
				//android.util.Log.v("kiyo","##--------3------------"+mCurrentPath+","+pf.getAbsolutePath());
//				android.util.Log.v("kiyo","##--------1------------");
				setCurrentPath(lf.getAbsoluteFile());
				TextViewer mInfo = BufferManager.getManager().getInfoBuffer();
				if(mInfo == null || mInfo.isDispose()) {
//					android.util.Log.v("kiyo","tab--4--");
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
//				android.util.Log.v("kiyo","tab--5--");
//				android.util.Log.v("kiyo","##--------2------------");
				File cp = getCurrentFile().getParentFile();
			//	if(!cp.exists()) {
			//		cp = mCurrentPath;
			//	}
				if(cp != null&&cp.equals(pf)) {
//					android.util.Log.v("kiyo","tab--6--");
					if(mUpdate != null) {
//						android.util.Log.v("kiyo","tab--7--");
						String candidate = mUpdate.getCandidate(lf.getName());
						//android.util.Log.v("kiyo","##---4--"+candidate+","+lf.getName());
						if(lf.getName().length()<candidate.length()) {
//							android.util.Log.v("kiyo","tab--8--");
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
//					android.util.Log.v("kiyo","tab--9--");
//					android.util.Log.v("kiyo","##--------4-1-----------");
					setCurrentPath(lf.getAbsoluteFile());
					mCurrentDir = lf.getAbsoluteFile();
//					android.util.Log.v("kiyo","FT: start 00");
					modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), new File(line), ""));
				} else if(pf.exists()){
//					android.util.Log.v("kiyo","tab--10--");
//					android.util.Log.v("kiyo","##--------4-2-----------");
					setCurrentPath(lf.getAbsoluteFile());
					mCurrentDir = pf;
//					android.util.Log.v("kiyo","FT: start 01");
					modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), pf, lf.getName()));	
				}
			}
		}

		private boolean mFirst = true;
		@Override
		public void begin() {
			if(!mFirst) {
				return;
			}
//.util.Log.v("kiyo","begin");
			//
//			android.util.Log.v("kiyo","begin"+getCurrentFile());
			File target = getCurrentFile();
			File base = target.getParentFile();
			if(base == null||target.isDirectory()){
				base = target;
			}
			// todo 
			BufferManager.getManager().beginInfoBuffer();
			if(mFirst) {
				mFirst = false;
				if(mFirstFocosIsInfo) {
					BufferManager.getManager().changeFocus(BufferManager.getManager().getInfoBuffer());					
				} else{
					BufferManager.getManager().changeFocus(BufferManager.getManager().getMiniBuffer());
				}
			} 
			MiniBuffer modeBuffer = BufferManager.getManager().getMiniBuffer();
			EditableLineViewBuffer buffer = (EditableLineViewBuffer)modeBuffer.getLineView().getLineViewBuffer();
			buffer.clear();
			modeBuffer.getLineView().getLeft().setCursorCol(0);
			modeBuffer.getLineView().getLeft().setCursorRow(0);
			buffer.pushCommit(""+base.getAbsolutePath(), 1);
			modeBuffer.getLineView().recenter();
//			android.util.Log.v("kiyo","FT: start 03");
			//if(mFirst) {
				setCurrentPath(base.getAbsoluteFile());
				modeBuffer.startTask(mUpdate = new UpdateInfo(BufferManager.getManager().getInfoBuffer(), getCurrentFile(),""));			
			//}
				//
			MessageDispatcher.getInstance().addReceiver(new FindFileReceiver(this));

		}
		@Override
		public void end() {
//			android.util.Log.v("kiyo","end ");
			BufferManager.getManager().endInfoBuffer();			
		}
	}

	public static class UpdateInfo implements Runnable {
		private TextViewer mInfo = null;
		private File mPath = null;
		private String mFilter = "";
		private AutocandidateList mCandidate = new AutocandidateList(); 
		public UpdateInfo(TextViewer info, File path, String filter) {
//			android.util.Log.v("kiyo","---UpdateInfo"+path+","+filter);
			mInfo = info;
			mPath = path;
			mFilter = filter;
		}

		public String getCandidate(String c) {
			return mCandidate.candidateText(c);
		}

		@Override
		public void run() {
//			android.util.Log.v("kiyo","-------D1------");

			try {
//				android.util.Log.v("kiyo","FT: start ");
				int c=0;
				mCandidate.clear();
				BufferManager.getManager().beginInfoBuffer();
				EditableLineView viewer = mInfo.getLineView();
				mInfo.getTextViewerBuffer().getBigLineData().ffformatterOn();
				EditableLineViewBuffer buffer = (EditableLineViewBuffer)mInfo.getLineView().getLineViewBuffer();
				viewer.setTextSize(BufferManager.getManager().getBaseTextSize());
				buffer.setCursor(0, 0);
				if(mPath == null) {
					return;
				}

				VirtualFile vfile = mInfo.getTextViewerBuffer().getBigLineData().getVFile();

				{
					File p = mPath.getParentFile();
					Thread.sleep(0);
					if(p!=null&&p.getParentFile() != null && p.getParentFile().isDirectory()) {
						addFile(vfile, p.getParentFile(), "../..");						
					}
					if(p!=null&&p.isDirectory()) {
						addFile(vfile, p, "..");
					}
				}
				if(!mPath.isDirectory()&&mPath.isFile()) {
					addFile(vfile, mPath, null);
				}
//				android.util.Log.v("kiyo","FT: start 00002");
				if(mPath.isDirectory()) {
//					android.util.Log.v("kiyo","QWW--0-");
					FileListGetter getter = new FileListGetter(mPath, mFilter, Thread.currentThread());
					AsyncronousTask sync = new AsyncronousTask(getter);
					Thread t = new Thread(sync);
					t.start();
//					android.util.Log.v("kiyo","QWW--1-");
					if(!sync.syncTask()) {
						return;
					}
//					android.util.Log.v("kiyo","QWW--2-");
//					android.util.Log.v("kiyo","-------D01------");
					if(Thread.currentThread().isInterrupted()) {
//						android.util.Log.v("kiyo","-------D01-1------");
						return;
					}
//					android.util.Log.v("kiyo","-------D01-2-----");
					File[] list = getter.getFileList();
//					android.util.Log.v("kiyo","-------D02------");
					Thread.sleep(0);
//					android.util.Log.v("kiyo","QWW--3-");
					addFileList(vfile, viewer, buffer, list);
					{
						File p = mPath.getParentFile();
						if(p!=null&&p.isDirectory()) {
							addFile(vfile, p, "..");
						}
					}
				} else {
					{
						File p = mPath.getParentFile();
						if(p!=null&&p.isDirectory()) {
							addFile(vfile, p, "..");
						}
					}
				}
//				android.util.Log.v("kiyo","FT: start 00003");
			} catch(Throwable t) {
//				android.util.Log.v("kiyo","-------D1------");
				t.printStackTrace();
			} finally {
//				android.util.Log.v("kiyo","-------D2------");
//				android.util.Log.v("kiyo","FT: end ");
			}
		}





		private void addFileList(VirtualFile v, EditableLineView viewer,	
				EditableLineViewBuffer buffer,
				//Collection<File> fileList
				File[] fileList
				) throws InterruptedException, UnsupportedEncodingException, IOException {
			int size = buffer.getDiffer().length();
			int c=0;
			if(fileList == null) {
				return;
			}
			for(File f : fileList) {
				Thread.sleep(0);
				if(f == null) {
					continue;
				}
				c++;
				if(c<100){
					mCandidate.add(f.getName());
				}
				addFile(v, f, null);
				if(c%100==0){
					Thread.sleep(10);
				}
			}
		}

		private void addFile(VirtualFile vFile, File file, String label) throws UnsupportedEncodingException, IOException, InterruptedException {
			Thread.sleep(0);
			if(file == null) {
				return;
			}
			String INFO = ":::"+file.getAbsolutePath();
			if(label == null) {
				label = file.getName();
			}
			vFile.addChunk(("●"+label+(file.isDirectory()?"/":"")+INFO).getBytes("utf8"));
			String date = DateFormat.getDateTimeInstance().format(new Date(file.lastModified()));
			vFile.addChunk(("\r\n  "+date+""+INFO).getBytes("utf8"));
			vFile.addChunk(("\r\n  "+file.length()+"byte"+INFO+":::HR").getBytes("utf8"));			
			vFile.addChunk(("\r\n").getBytes("utf8"));			
		}


	}

}