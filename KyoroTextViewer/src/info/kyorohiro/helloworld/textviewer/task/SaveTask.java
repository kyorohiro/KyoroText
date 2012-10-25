package info.kyorohiro.helloworld.textviewer.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineViewBuffer;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;

public class SaveTask implements Runnable {

	private EditableLineViewBuffer mBuffer = null;
	private File mSaveFilePath = null;

	public SaveTask(EditableLineViewBuffer buffer, File path) {
		mBuffer = buffer;
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
			init();
			int len = mBuffer.getNumberOfStockedElement();
			for(int i=0;i<len;i++) {
				KyoroString str = mBuffer.get(i);
				byte[] b = (""+str).getBytes();
				mStream.write(b, 0, b.length);
			}
		}
		finally {
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
