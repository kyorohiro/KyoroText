package info.kyorohiro.helloworld.io;

import junit.framework.TestCase;

public class TestForVirtualFile extends TestCase {

	public void log(String message) {
		android.util.Log.v("kiyo", "TestForViertualFile:"+message);
	}

	public void testHello() {
		log("testHello");
	}

}
