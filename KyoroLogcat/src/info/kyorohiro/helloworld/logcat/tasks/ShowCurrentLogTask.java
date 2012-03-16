package info.kyorohiro.helloworld.logcat.tasks;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;
import info.kyorohiro.helloworld.logcat.util.Logcat;
import info.kyorohiro.helloworld.logcat.util.Logcat.LogcatException;

public class ShowCurrentLogTask extends Thread implements TaskInter {
	private final Logcat mLogcat = new Logcat();
	private FlowingLineData mData;
	private String mOption = "-n";

	public ShowCurrentLogTask(FlowingLineData data, String option) {
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
			InputStream output = mLogcat.getInputStream();
			InputStream error = mLogcat.getErrorStream();

			BufferedReader outputReader = new BufferedReader(new InputStreamReader(output));
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(error));
			while(true) {
				if(!outputReader.ready()&&!errorReader.ready()){
					if(!mLogcat.isAlive()){
						break;
					}
					Thread.sleep(400);
					Thread.yield();
				}
				else {
					if(outputReader.ready()){
						mData.addLinePerBreakText(outputReader.readLine());
						Thread.yield();
					}
					if(errorReader.ready()){
						mData.addLinePerBreakText(errorReader.readLine());
						Thread.yield();
					}
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
