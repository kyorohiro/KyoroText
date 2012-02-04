package info.kyorohiro.helloworld.logcat.tasks;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import info.kyorohiro.helloworld.display.widget.CyclingFlowingLineData;
import info.kyorohiro.helloworld.logcat.util.Logcat;
import info.kyorohiro.helloworld.logcat.util.Logcat.LogcatException;

public class ShowCurrentLogTask extends Thread {
	private final Logcat mLogcat = new Logcat();
	private CyclingFlowingLineData mData;
	private String mOption = "-n";

	public ShowCurrentLogTask(CyclingFlowingLineData data, String option) {
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
		mData.clear();
		try {
			InputStream stream = mLogcat.getInputStream();
//			DataInputStream input  = new DataInputStream(stream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			while(true) {
				if(!reader.ready()){
					if(!mLogcat.isAlive()){
						break;
					}
					Thread.sleep(200);
				}
				else {
					mData.addLinePerBreakText(reader.readLine());
					Thread.yield();
				}
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
