package info.kyorohiro.helloworld.logcat.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import info.kyorohiro.helloworld.display.widget.CyclingFlowingLineData;
import info.kyorohiro.helloworld.util.BigLineData;

public class ShowFileContentTask extends Thread {
	private CyclingFlowingLineData mData;
	private BigLineData mLineData = null;
	
	public ShowFileContentTask(CyclingFlowingLineData data, File file) throws FileNotFoundException {
		super();
		mData = data;
		 mLineData = new BigLineData(file);
	}


	public void terminate() {
		try {
			if(mLineData != null){
				mLineData.close();
			}
			mLineData = null;
			interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		android.util.Log.v("kiyohiro","--run()---");
		try {
			while(true) {
				if(mLineData.isEOF()){
					break;
				}
				android.util.Log.v("kiyohiro","--01--");
				String line = mLineData.readLine();
				android.util.Log.v("kiyohiro","--"+line+"--");
				
				mData.addLinePerBreakText(line);
				Thread.sleep(0);
			}
		} catch (IOException e) {
			android.util.Log.v("kiyohiro","--IOException---");
			e.printStackTrace();
		} catch (InterruptedException e) {
			android.util.Log.v("kiyohiro","--InterrupedException---");
		} finally {
			android.util.Log.v("kiyohiro","-final-");
		}
	}
}
