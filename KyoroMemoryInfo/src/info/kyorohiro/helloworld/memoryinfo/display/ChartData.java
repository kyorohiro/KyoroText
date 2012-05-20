package info.kyorohiro.helloworld.memoryinfo.display;

import java.io.File;
import java.io.FileNotFoundException;

import info.kyorohiro.helloworld.io.BigLineData;

public class ChartData {

	private BigLineData mLineData = null;
	public ChartData(String path) {
		try {
			mLineData = new BigLineData(new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void next() {
		
	}

	public void jump() {
		
	}

	public long getSize(String value, String property) {
		if("test1".equals(value)){
			return 1000;
		}
		else if("test2".equals(value)){
			return 1000;
		}
		else if("test3".equals(value)){
			return 1000;
		}
		else if("test4".equals(value)){
			return 1000;
		}
		else if("test5".equals(value)){
			return 1000;
		}
		else {
			return 12000;
		}
	}
}
