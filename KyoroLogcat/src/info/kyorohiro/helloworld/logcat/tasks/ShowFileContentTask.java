package info.kyorohiro.helloworld.logcat.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineBuffer;
import info.kyorohiro.helloworld.io.BigLineData;

public class ShowFileContentTask extends Thread implements TaskInter {
	private FlowingLineBuffer mData;
	private BigLineData mLineData = null;
	
	public ShowFileContentTask(FlowingLineBuffer data, File file) throws FileNotFoundException {
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
		BigLineData lineData = mLineData; 
		try {
			mData.clear();
			while(mLineData!=null) {
				if(lineData.isEOF()){
					break;
				}
				CharSequence line = lineData.readLine();
				mData.addLinePerBreakText(line);
				Thread.sleep(0);
				Thread.yield();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
		} finally {
			try {
				lineData.close();
			} catch(Throwable t){
				t.printStackTrace();
			}
		}
	}
}
