package info.kyorohiro.helloworld.test;

import android.content.Intent;
import android.test.ServiceTestCase;
import info.kyorohiro.helloworld.logcat.KyoroLogcatService;

public class KyoroLogcatServiceTest extends ServiceTestCase<KyoroLogcatService> {

	public KyoroLogcatServiceTest() {
		super(KyoroLogcatService.class);
		// TODO Auto-generated constructor stub
	}
	
	public void testHello() {
        Intent startIntent = new Intent();
        startIntent.setClass(getContext(), KyoroLogcatService.class);
        startService(startIntent); 
        try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
