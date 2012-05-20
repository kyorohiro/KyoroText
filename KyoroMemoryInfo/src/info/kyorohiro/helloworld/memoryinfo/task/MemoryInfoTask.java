package info.kyorohiro.helloworld.memoryinfo.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

import info.kyorohiro.helloworld.memoryinfo.KyoroSetting;
import info.kyorohiro.helloworld.memoryinfo.util.KyoroMemoryInfo;

public class MemoryInfoTask implements Runnable {

	private KyoroMemoryInfo mInfo = new KyoroMemoryInfo();

	@Override
	public void run() {
		android.util.Log.v("", "");
		FileOutputStream out = null;
		try {
			File saveFile = getPath();
			out = new FileOutputStream(saveFile);
			CharSequence c = "";
			while (true) {
				c = mInfo.update();
				out.write(c.toString().getBytes());
				Thread.sleep(10000);
			}
		} catch (MyException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getPath() throws MyException {
		String path = KyoroSetting.getData(KyoroSetting.TAG_FILE_PATH);
		File f = new File(path);
		if (!f.exists() || f.canWrite()) {
			f = new File(Environment.getExternalStorageDirectory(),"KyoroMemoryInfo");
			if(!f.exists()){
				f.mkdirs();
			}
			f = new File(f,"tmp.txt");
			try {
				f.createNewFile();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

		if (!f.exists() || !f.canWrite()) {
			throw new MyException("not found or not writable");
		}
		return f;
	}

	public static class MyException extends Exception {
		public MyException(String message) {
			super(message);
		}
	}
}
