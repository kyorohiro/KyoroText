package info.kyorohiro.helloworld.test;

import android.os.Bundle;
import info.kyorohiro.helloworld.android.base.TestActivity;
import info.kyorohiro.helloworld.android.base.TestResult;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;
import info.kyorohiro.helloworld.logcat.util.LogcatViewer;

public class LogcatViewerTest extends android.test.ActivityInstrumentationTestCase2<LogcatViewerTest.LogcatViewerTestActivity>{

	public LogcatViewerTest() {
		super(null, LogcatViewerTest.LogcatViewerTestActivity.class);
	}

	public static class LogcatViewerTestActivity extends TestActivity {
		private FlowingLineData mData = null;
		private LogcatViewer mConsole = null;
		private SimpleStage mStage = null;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mConsole = new LogcatViewer();
			mData = mConsole.getCyclingFlowingLineData();
			mStage = new SimpleStage(this);
			mStage.getRoot().addChild(mConsole);
			setContentView(mStage);
			mStage.start();
		}
		
		public FlowingLineData getData() {
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
		private  FlowingLineData mData = null;
		private String createTestString(String text, int num){
			StringBuilder b = new StringBuilder();
			for(int i=0;i<num;i++){
				b.append(text);
			}
			return b.toString();
		}
		public TestHello(FlowingLineData data){
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