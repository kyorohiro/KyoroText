package info.kyorohiro.helloworld.logcat.test.testcase;

import android.os.Bundle;
import info.kyorohiro.helloworld.base.TestActivity;
import info.kyorohiro.helloworld.base.TestResult;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.logcat.display.widget.LogcatViewer;
import info.kyorohiro.helloworld.logcat.util.CyclingStringList;

public class LogcatViewerTest extends android.test.ActivityInstrumentationTestCase2<LogcatViewerTest.LogcatViewerTestActivity>{

	public LogcatViewerTest() {
		super(null, LogcatViewerTest.LogcatViewerTestActivity.class);
	}

	public static class LogcatViewerTestActivity extends TestActivity {
		private CyclingStringList mData = null;
		private LogcatViewer mConsole = null;
		private SimpleStage mStage = null;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mConsole = new LogcatViewer(1000);
			mData = mConsole.getCyclingStringList();
			mStage = new SimpleStage(this);
			mStage.getRoot().addChild(mConsole);
			setContentView(mStage);
			mStage.start();
		}
		
		public CyclingStringList getData() {
			return mData; 
		}

		@Override
		protected void onDestroy() {
			super.onDestroy();
			mStage.stop();
		}
	}
	
	public void testHello(){
		LogcatViewerTestActivity activity = getActivity();
		TestResult result = activity.startTest();
		
		Thread th = new Thread(new TestHello(activity.getData()));
		th.start();
		if(TestResult.TEST_PASSED == result.getResult(60000)){
			;
		} else {
			assertTrue(false);
		}
	}

	class TestHello implements Runnable {
		private  CyclingStringList mData = null;
		private String createTestString(String text, int num){
			StringBuilder b = new StringBuilder();
			for(int i=0;i<num;i++){
				b.append(text);
			}
			return b.toString();
		}
		public TestHello(CyclingStringList data){
			mData = data;
		}

		public void run() {
			int i=0;
			while(true){
				mData.addLinePerBreakText("------------------abcdef---------------------"+(i++));
				mData.addLinePerBreakText(createTestString("abcdefghijklmnopqrstuvwxyz0123456789",20));
				mData.addLinePerBreakText("");
				mData.addLinePerBreakText(null);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
