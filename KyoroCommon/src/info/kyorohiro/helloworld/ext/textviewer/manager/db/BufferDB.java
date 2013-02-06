package info.kyorohiro.helloworld.ext.textviewer.manager.db;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.editview.differ.Differ;
import info.kyorohiro.helloworld.ext.textviewer.manager.db.DBList.Item;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.util.TaskTicket;

import java.io.File;
import java.io.IOException;

public class BufferDB {
  
	private SimpleApplication mApplication = null;
	private static BufferDB sInstance = null;
	
	private DBList mDbList = null;
	private BufferDB(SimpleApplication app) {
		mApplication = app;
	};

	public static BufferDB getBufferDB(SimpleApplication app) {
		if(sInstance == null) {
			sInstance = new BufferDB(app);
		}
		return sInstance;
	}

	public DBList getDBList() throws IOException {
		if(mDbList == null) {
			mDbList = new DBList(mApplication);
			mDbList.file2List();
		}
		return mDbList;
	}


	public void start(SimpleApplication app, TextViewer target) {
		//
		if(!target.isEdit()){return;}
		//
		BufferDBTask task = new BufferDBTask(this, app, target);
		Thread th = new Thread(task);
		th.start();
	}

	public static class BufferDBTask implements Runnable {
		private BufferDB mDb = null;
		private TextViewer mTarget = null;
		private SimpleApplication mApp = null;
		public BufferDBTask(BufferDB db, SimpleApplication app, TextViewer target) {
			mDb = db;
			mApp = app;
			mTarget = target;
			mTarget.getManagedLineViewBuffer().reserve();
		}

		public void run() {
			try {
				if(mTarget == null) {
					return;
				}
//				android.util.Log.v("kiyo", "buffer task# ---1--");
				BufferDB b = BufferDB.getBufferDB(mApp);
				DBList list = b.getDBList();
				list.file2List();
				int id =list.getEmptyID();

				//
				// create infofile
//				android.util.Log.v("kiyo", "buffer task# ---5--");
				String path = mTarget.getCurrentPath();
				String charset = mTarget.getCharset();
				int textsize = mTarget.getLineView().getTextSize();
				long lastModify = mTarget.getVFile().getBase().lastModified();
				long filesize = mTarget.getVFile().getBase().length();
				DBInfo info = new DBInfo(mApp, id);
				info.path(path);
				info.charset(charset);
				info.textsize(textsize);
				info.lastModify(lastModify);
				info.filesize(filesize);
				info.inter2File();
				
				//
				// create differfile
//				android.util.Log.v("kiyo", "buffer task# ---10--");
				EditableLineViewBuffer buffer = (EditableLineViewBuffer)mTarget.getLineView().getLineViewBuffer();
				Differ differ = buffer.getDiffer();
				File base = getPath(mApp, ""+id, "differ.txt");
				VirtualFile file = new VirtualFile(base, 201);
				TaskTicket<String> ticket=
				differ.save(buffer, file);
				ticket.syncTask();
				file.syncWrite();
				file.close();
				//
				//
				File l = new File(mTarget.getCurrentPath());
				list.add(new Item(id, ""+l.getName()));
				list.list2File();
//				android.util.Log.v("kiyo", "buffer task# ---15--"+differ.length());
			} catch (IOException e) {
//				android.util.Log.v("kiyo", "buffer task# ---20--");
				e.printStackTrace();
			} finally {
//				android.util.Log.v("kiyo", "buffer task# ---25--");
				mTarget.getManagedLineViewBuffer().reserve();
			}
//			android.util.Log.v("kiyo", "buffer task# ---30--");
		}
	}

	public TextViewer getTarget() {
		return null;
	}

	public SimpleApplication getApplication() {
		return null;
	}

	public void save(TextViewer viewer, File path) throws IOException {
		EditableLineViewBuffer buffer = (EditableLineViewBuffer)viewer.getLineView().getLineViewBuffer();
		VirtualFile vfile = new VirtualFile(path, 512);
		buffer.getDiffer().save(buffer, vfile);
		vfile.syncWrite();
		vfile.close();
	}








	
	//
	//
	//
	
	public static final String sLIST_TET_PARENT = "buffer";
	public static File getPath(SimpleApplication app, String path) throws IOException {
		File parent = new File(app.getApplicationDirectory(), sLIST_TET_PARENT);
		File listTxt = new File(parent, path);
		if(!parent.exists()) {
			parent.mkdirs();
		}
		if(!listTxt.exists()) {
			listTxt.createNewFile();
		}
		return listTxt;
	}

	public static File getPath(File parent, File listTxt) throws IOException {
//		android.util.Log.v("kiyo",""+parent.getAbsolutePath());
//		android.util.Log.v("kiyo",""+listTxt.getAbsolutePath());
		if(!parent.exists()) {
			parent.mkdirs();
		}
		if(!listTxt.exists()) {
			listTxt.createNewFile();
		}
		return listTxt;
	}

	public static File getPath(SimpleApplication app, String pa, String path) throws IOException {
		File pparent = new File(app.getApplicationDirectory(), sLIST_TET_PARENT);
		File parent = new File(pparent, pa);
		File f = new File(parent, path);
		return getPath(parent, f);
	}
}
