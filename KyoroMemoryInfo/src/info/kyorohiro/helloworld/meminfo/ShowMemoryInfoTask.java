package info.kyorohiro.helloworld.meminfo;

import java.util.List;

import info.kyorohiro.helloworld.display.widget.SimpleLineChart.CharDatam;
import info.kyorohiro.helloworld.display.widget.SimpleLineChart.ChartData;
import info.kyorohiro.helloworld.meminfo.util.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public class ShowMemoryInfoTask extends Thread {
	
	private Context mContext = null;
	private ChartData mChart = null;

	public ShowMemoryInfoTask(Context context, ChartData chart) {
		mContext = context;
		mChart = chart;
	}

	@Override
	public void run() {
		super.run();
		MemoryInfo me = new MemoryInfo();
		try {
			while(true) {
				List<RunningAppProcessInfo> infos =  me.getRunningAppList(mContext);
				for(RunningAppProcessInfo info : infos) {
					for(String pkg : info.pkgList){
						if(pkg.equals("info.kyorohiro.helloworld.logcat")){
							Log.v("kiyohiro", pkg);
							android.os.Debug.MemoryInfo _minfo = me.getMemInfoData(mContext, info.pid);
							mChart.add(new CharDatam(0, _minfo.getTotalPrivateDirty()));
						}
					}
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
