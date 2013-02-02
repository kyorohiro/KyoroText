package info.kyorohiro.helloworld.ext.textviewer.manager.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.LinkedList;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.io.MarkableFileReader;
import info.kyorohiro.helloworld.io.SimpleTextDecoder;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;

public class DBInfo {
	public static final String sLIST_TET_PARENT = "buffer";
	public static final String sLIST_TXT = "info.txt";
	private SimpleApplication mApplication = null;
	private int mId = 0;
	private String mPath = "";
	private String mCharset = "";
	private int mTextSize = 0;
	private long mBegin = 0;
	private long mEnd  = 0;
	private long mLastModify = 0;
	private long mFileSize = 0;

	public DBInfo(SimpleApplication application, int id) {
		mApplication = application;
		mId = id;
	}

	public void id(int id) {
		mId = id;
	}
	public int id() {
		return mId;
	}
	public void path(String path) {
		mPath = path;
	}
	public String path() {
		return mPath;
	}
	public void charset(String charset) {
		mCharset = charset;
	}
	public String charset() {
		return mCharset;
	}
	public void textsize(int textsize) {
		mTextSize = textsize;
	}
	public int textsize() {
		return mTextSize;
	}
	public void begin(long begin) {
		mBegin = begin;
	}
	public long begin() {
		return mBegin;
	}
	public void end(long end) {
		mEnd = end;
	}
	public long end() {
		return mEnd;
	}
	public void filesize(long filesize) {
		mFileSize = filesize;
	}
	public long filesize() {
		return mFileSize;
	}
	public void lastModify(long lastModify) {
		mLastModify = lastModify;
	}
	public long lastModify() {
		return mLastModify;
	}

	public void inter2File() throws IOException {
		File file = getPath();
		String save = 
				"1.0,id="+mId+",pa="+mPath+",ch="+mCharset+
				",ts="+mTextSize+",be="+mBegin+",en="+mEnd+
				",tlm="+mLastModify+",tsi="+mFileSize;
		VirtualFile v = new VirtualFile(file, 512);
		v.addChunk(save.getBytes("utf8"));
		v.syncWrite();
	}

	public void file2Inter() throws IOException {
		File file = getPath();
		VirtualFile v = new VirtualFile(file, 512);
		byte[] buffer = new byte[(int)v.length()];
		v.read(buffer);
		String base =  new String(buffer,"utf8");
		String[]val = base.split(",[^=]*=");
		mId = Integer.parseInt(val[1]);
		mPath = val[2];
		mCharset = val[3];
		mTextSize = Integer.parseInt(val[4]);
		mBegin = Long.parseLong(val[5]);
		mEnd = Long.parseLong(val[6]);
		mLastModify = Long.parseLong(val[7]);
		mFileSize = Long.parseLong(val[8]);
	}

	private File getPath() throws IOException {
		return BufferDB.getPath(mApplication, ""+mId, sLIST_TXT);
	}

}
