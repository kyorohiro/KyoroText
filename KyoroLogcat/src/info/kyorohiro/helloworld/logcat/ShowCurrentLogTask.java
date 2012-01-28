package info.kyorohiro.helloworld.logcat;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import info.kyorohiro.helloworld.logcat.logcat.LogcatCyclingLineDataList;
import info.kyorohiro.helloworld.logcat.util.Logcat;
import info.kyorohiro.helloworld.logcat.util.Logcat.LogcatException;

public class ShowCurrentLogTask extends Thread {
	private final Logcat mLogcat = new Logcat();
	private LogcatCyclingLineDataList mData;
	private String mOption = "-n";

	public ShowCurrentLogTask(LogcatCyclingLineDataList data, String option) {
		mData = data;
		if(option != null) {
			mOption = option;
		}
	}

	public void terminate() {
		if(mLogcat != null){
			mLogcat.terminate();
		}
		interrupt();
	}

	public void run() {
		mLogcat.start(mOption);
		try {
			InputStream stream = mLogcat.getInputStream();
//			DataInputStream input  = new DataInputStream(stream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			while(mLogcat.isAlive()) {
				if(!reader.ready()){
					Thread.sleep(200);
				}
				mData.addLinePerBreakText(reader.readLine());
			}
		} catch (LogcatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// expected exception
		} finally {
			mLogcat.terminate();
		}
	}
}
