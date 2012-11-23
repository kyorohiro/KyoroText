package info.kyorohiro.helloworld.textviewer.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineViewBuffer;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import info.kyorohiro.helloworld.util.Utility;

//
// 2012/11/23 
// todo
// now all text is convert to utf8
// next modify text only is covert utf8 and save 
//
public class SaveTask implements Runnable {

	private TextViewer mViewer = null;
	private EditableLineView mEditor = null;
	private EditableLineViewBuffer mBuffer = null;
	private File mSaveFilePath = null;
	private File mTmpFilePath = null;
	private File mBakFilePath = null;
	private File mCurrentFilePath = null;
	private String mCharset = "UTF8";

	public SaveTask(TextViewer viewer, File path){//EditableLineView editor, String charset, File path) {
		LineView _viewer = viewer.getLineView();
		EditableLineView editor = (EditableLineView)_viewer;
		mViewer = viewer;
		mEditor = editor;
		mBuffer = (EditableLineViewBuffer)editor.getLineViewBuffer();
		mSaveFilePath = path;
		mCharset = viewer.getCharset();
	}

	@Override
	public void run() {
		try {
			KyoroApplication.showMessage("start save");
			action();
			KyoroApplication.showMessage("end save");
		} catch (IOException e) {
			e.printStackTrace();
			KyoroApplication.showMessage("failed save "+e);
		}
	}

	public void action() throws IOException {
		try {
			mEditor.isLockScreen(true);
			mBuffer.isSync(true);
			mViewer.getManagedLineViewBuffer().reserve();
			save_init();

			
			// �ꎞ�ۑ�
			mStream = new FileOutputStream(mTmpFilePath);
//			for(int i=0;i<mBuffer.getNumberOfStockedElement();i++) {
//				KyoroString str = mBuffer.get(i);
//				byte[] b = (""+str).getBytes(mCharset);
//				mStream.write(b, 0, b.length);
//				Thread.yield();
//			}

			for(int i=0;i<mBuffer.getNumberOfStockedElement();i++) {
				KyoroString str = mBuffer.get(i);
				if(str.getBeginPointer()<0||str.getEndPointer()<0){
					continue;
				}
				byte[] b = (""+str).getBytes(mCharset);
				mStream.write(b, 0, b.length);
				Thread.yield();
			}

			// �����̃t�@�C������ʖ��ŋL�^���Ă���
			mBakFilePath.delete();
			mSaveFilePath.renameTo(mBakFilePath.getAbsoluteFile());
			
			// �ꎞ�ۑ������t�@�C�����ړ�����B
			mTmpFilePath.renameTo(mSaveFilePath.getAbsoluteFile());
		}
		finally {
			mEditor.isLockScreen(false);
			mBuffer.isSync(false);
			mViewer.getManagedLineViewBuffer().release();
			save_end();
		}
//		if(mSaveFilePath.getAbsolutePath().equals(mCurrentFilePath.getAbsolutePath())) {
//			mViewer.readFile(mCurrentFilePath);
//		}
		mViewer.readFile(mSaveFilePath);
	}

	private OutputStream mStream = null;
	public void save_init() throws IOException {
		mTmpFilePath = getTmpFile();
		mBakFilePath = getBackupFile();
		mCurrentFilePath = new File(mViewer.getCurrentPath());
		if(!mTmpFilePath.exists()){
			mTmpFilePath.createNewFile();
		}
		if(!mBakFilePath.exists()){
			mBakFilePath.createNewFile();
		}
		
	}
	public void save_end() throws IOException {
		if(mStream != null) {
			try{
			mStream.close();
			}catch(Throwable t){
				t.printStackTrace();
			}
		}
	}

	public File getBackupFile() {
		return getSubFile(".kyorohiro.bak");
	}

	public File getTmpFile() {
		return getSubFile(".kyorohiro.tmp");
	}

	public File getSubFile(String subExt) {
		//if(!mSaveFilePath.exists()){
		//	return null;
		//}
		int num = 0;
		File file = null;
		for(int i=0;i<10;i++){
			file = new File(mSaveFilePath.getAbsolutePath()+"."+num+subExt);
			if(!file.exists()){
				return file;
			}
			num++;
		}
		return file;
	}

}
