package info.kyorohiro.helloworld.ext.textviewer.manager.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.util.AsyncronousTask;
import info.kyorohiro.helloworld.util.AutocandidateList;
import info.kyorohiro.helloworld.util.FileListGetter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;

public class FindFileTask implements Runnable {
	private TextViewer mInfo = null;
	private File mPath = null;
	private String mFilter = "";
	private AutocandidateList mCandidate = new AutocandidateList(); 
	public FindFileTask(TextViewer info, File path, String filter) {
//		android.util.Log.v("kiyo","---UpdateInfo"+path+","+filter);
		mInfo = info;
		mPath = path;
		mFilter = filter;
	}

	public String getCandidate(String c) {
		return mCandidate.candidateText(c);
	}

	@Override
	public void run() {
//		android.util.Log.v("kiyo","-------D1------");

		try {
//			android.util.Log.v("kiyo","FT: start ");
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
//			android.util.Log.v("kiyo","FT: start 00002");
			if(mPath.isDirectory()) {
//				android.util.Log.v("kiyo","QWW--0-");
				FileListGetter getter = new FileListGetter(mPath, mFilter, Thread.currentThread());
				AsyncronousTask sync = new AsyncronousTask(getter);
				Thread t = new Thread(sync);
				t.start();
//				android.util.Log.v("kiyo","QWW--1-");
				if(!sync.syncTask()) {
					return;
				}
//				android.util.Log.v("kiyo","QWW--2-");
//				android.util.Log.v("kiyo","-------D01------");
				if(Thread.currentThread().isInterrupted()) {
//					android.util.Log.v("kiyo","-------D01-1------");
					return;
				}
//				android.util.Log.v("kiyo","-------D01-2-----");
				File[] list = getter.getFileList();
//				android.util.Log.v("kiyo","-------D02------");
				Thread.sleep(0);
//				android.util.Log.v("kiyo","QWW--3-");
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
//			android.util.Log.v("kiyo","FT: start 00003");
		} catch(Throwable t) {
//			android.util.Log.v("kiyo","-------D1------");
			t.printStackTrace();
		} finally {
//			android.util.Log.v("kiyo","-------D2------");
//			android.util.Log.v("kiyo","FT: end ");
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