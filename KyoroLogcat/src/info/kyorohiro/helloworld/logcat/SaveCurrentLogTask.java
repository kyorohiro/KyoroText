package info.kyorohiro.helloworld.logcat;

import info.kyorohiro.helloworld.logcat.util.Logcat;
import info.kyorohiro.helloworld.logcat.util.Logcat.LogcatException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Environment;

public class SaveCurrentLogTask extends Thread {

	private File mSavedDirectory = null;
	@SuppressWarnings("unused")
	private boolean mExternalStorageAvailable = false;
	private boolean mExternalStorageWriteable = false;
	private Logcat mLogcat = null;
	private String mOption = "";


	public SaveCurrentLogTask(String option) {
		mLogcat = new Logcat();
		if(option != null){
			mOption = option;
		}
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	    	mExternalStorageAvailable = mExternalStorageWriteable = true;
	    }
	    else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	    	mExternalStorageAvailable = true;
	    	mExternalStorageWriteable = false;
	    } else {
	    	mExternalStorageAvailable = mExternalStorageWriteable = false;
	    }
	    
		mSavedDirectory = Environment.getExternalStorageDirectory();
	}

	public void terminate() {
		if(mLogcat != null){
			mLogcat.terminate();
		}
		interrupt();
		KyoroApplication.shortcutToStopKyoroLogcatService();
	}

	public void run() {
		if(mExternalStorageWriteable == false){
			KyoroApplication.showMessageAndNotification("failed by external storage is not writeable.\n  check if sdcatd is mounted!!");
			return;
		}

		FileOutputStream output = null;
		InputStream input = null;
		File saveFile = null;
		try {
			Date date = new Date();
			date.setTime(System.currentTimeMillis());
			SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss",Locale.getDefault());
			String filename = "log_"+format.format(date)+".txt";
			saveFile = new File(mSavedDirectory, filename);
			KyoroApplication.showMessage("start logcat log in "+saveFile.getPath());

			KyoroApplication.shortcutToStartKyoroLogcatService();

			mLogcat.start(mOption);

			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			output = new FileOutputStream(saveFile,true);
			input = mLogcat.getInputStream();
			while(mLogcat.isAlive()) {
				byte[] buffer = new byte[1024]; 
				int size = input.read(buffer);
				if(size == -1){
					continue;
				}
				output.write(buffer, 0, size);
			}
		} catch (IOException e) {
			e.printStackTrace();
			KyoroApplication.showMessageAndNotification("failed to throw io exception.\n  please check sdcatd!!");
		} catch(LogcatException e) {
			e.printStackTrace();
			KyoroApplication.showMessageAndNotification("failed by framework error.\n  please restart this application!!");
		} catch(Throwable e) {
			e.printStackTrace();
			KyoroApplication.showMessageAndNotification("failed to throw unexcepted error.\n  please restart this application!!");			
		}
		finally {
			if(output != null){
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(mLogcat != null) {
				mLogcat.terminate();
			}

			KyoroApplication.shortcutToStopKyoroLogcatService();
			KyoroApplication.showMessage("end to save logcat log :" + saveFile.getPath());
		}
	}
}
