package info.kyorohiro.helloworld.textviewer.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineViewBuffer;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;

public class SaveTask implements Runnable {

	private EditableLineView mEditor = null;
	private EditableLineViewBuffer mBuffer = null;
	private File mSaveFilePath = null;

	public SaveTask(EditableLineView editor, File path) {
		mEditor = editor;
		mBuffer = (EditableLineViewBuffer)editor.getLineViewBuffer();
		mSaveFilePath = path;
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
			init();
			for(int i=0;i<mBuffer.getNumberOfStockedElement();i++) {
				KyoroString str = mBuffer.get(i);
				byte[] b = (""+str).getBytes();
				mStream.write(b, 0, b.length);
			}
		}
		finally {
			mEditor.isLockScreen(false);
			mBuffer.isSync(false);
			end();
		}
	}

	private OutputStream mStream = null;
	public void init() throws IOException {
		if(!mSaveFilePath.exists()){
			mSaveFilePath.createNewFile();
		}
		mStream = new FileOutputStream(mSaveFilePath);
	}
	public void end() throws IOException {
		if(mStream != null) {
			mStream.close();
		}
	}
}
