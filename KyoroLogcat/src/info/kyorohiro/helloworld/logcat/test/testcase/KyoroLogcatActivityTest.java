package info.kyorohiro.helloworld.logcat.test.testcase;

import info.kyorohiro.helloworld.android.base.TestActivity;
import info.kyorohiro.helloworld.android.base.TestResult;
import info.kyorohiro.helloworld.logcat.KyoroLogcatActivity;

public class KyoroLogcatActivityTest extends android.test.ActivityInstrumentationTestCase2<KyoroLogcatActivity>{

	public KyoroLogcatActivityTest(){
	     super(null, KyoroLogcatActivity.class);
	}


	public void testHello() {
		TestActivity activity = this.getActivity();
		TestResult result = activity.startTest();
		assertTrue(TestResult.TEST_PASSED == result.getResult(30000));
		
	}
	
	
}
