package info.kyorohiro.helloworld.logcat.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;
import info.kyorohiro.helloworld.util.BigLineData;

public class ShowFileContentTask extends Thread {
	private FlowingLineData mData;
	private BigLineData mLineData = null;
	
	public ShowFileContentTask(FlowingLineData data, File file) throws FileNotFoundException {
		super();
		mData = data;
		 mLineData = new BigLineData(file,"utf8");
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
		try {
			mData.clear();
			while(true) {
				if(mLineData.isEOF()){
					break;
				}
				CharSequence line = mLineData.readLine();
				mData.addLinePerBreakText(line);
				Thread.sleep(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		} finally {
			try {
				mLineData.close();
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
}
