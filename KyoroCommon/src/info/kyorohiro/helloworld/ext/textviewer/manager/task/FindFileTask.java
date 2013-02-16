package info.kyorohiro.helloworld.ext.textviewer.manager.task;

import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.io.KyoroFile;
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
		mInfo = info;
		mPath = path;
		mFilter = filter;
	}

	public String getCandidate(String c) {
		return mCandidate.candidateText(c);
	}


	private void drawBeginState(KyoroFile output) throws InterruptedException, UnsupportedEncodingException, IOException {
		File p = mPath.getParentFile();
		Thread.sleep(0);
		if(p!=null&&p.getParentFile() != null && p.getParentFile().isDirectory()) {
			showFile(output, p.getParentFile(), "../..");						
		}
		if(p!=null&&p.isDirectory()) {
			showFile(output, p, "..");
		}
	}

	private void drawEndState(KyoroFile output) throws UnsupportedEncodingException, IOException, InterruptedException {
		File p = mPath.getParentFile();
		if(p!=null&&p.isDirectory()) {
			showFile(output, p, "..");
		}
	}

	private FileListGetter getFileList() {
		FileListGetter getter = new FileListGetter(mPath, mFilter, Thread.currentThread());
		AsyncronousTask sync = new AsyncronousTask(getter);
		Thread t = new Thread(sync);
		t.start();
		if(!sync.syncTask()) {
			// call interruped 
			return null;
		} else {
			return getter;
		}
	}

	@Override
	public void run() {
		try {
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
			//
			// draw ./ ../
			drawBeginState(vfile);

			//
			// target path is file 
			if(!mPath.isDirectory()&&mPath.isFile()) {
				showFile(vfile, mPath, null);
			}
			//
			// target path is directory
			else if(mPath.isDirectory()) {
				// get file list
				FileListGetter getter = getFileList();
				if(getter == null) {
					// call interropted
					return;
				}

				// todo following code is needless
				if(Thread.currentThread().isInterrupted()) {
					return;
				}

				// show
				File[] list = getter.getFileList();
				Thread.sleep(0);
				showFileList(vfile,// viewer,
						//buffer,
						list);
				
				drawEndState(vfile);
			}
			vfile.finishPushing();
		} catch(Throwable t) {
			t.printStackTrace();
		} finally {
//			android.util.Log.v("kiyo","FT: end ");
		}
	}
	
	private void showFileList(VirtualFile v,File[] fileList) throws InterruptedException, UnsupportedEncodingException, IOException {
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
			showFile(v, f, null);
			if(c%100==0){
				Thread.yield();
				Thread.sleep(5);
			}
		}
	}

	private void showFile(KyoroFile vFile, File file, String label) throws UnsupportedEncodingException, IOException, InterruptedException {
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