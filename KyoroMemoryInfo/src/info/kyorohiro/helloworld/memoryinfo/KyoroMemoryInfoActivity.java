package info.kyorohiro.helloworld.memoryinfo;

import info.kyorohiro.helloworld.android.base.MainActivity;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.memoryinfo.display.ChartData;
import info.kyorohiro.helloworld.memoryinfo.display.LineChart;
import info.kyorohiro.helloworld.memoryinfo.parts.MainActivitySelectPackageAction;
import info.kyorohiro.helloworld.memoryinfo.parts.MainActivityStartAction;
import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class KyoroMemoryInfoActivity extends MainActivity {
    /** Called when the activity is first created. */
	
	private SimpleStage mStage = null;
	private LineChart mLineChart = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLineChart  = new LineChart(new ChartData(KyoroSetting.getData(KyoroSetting.TAG_FILE_PATH)), 400, 400);
        mLineChart.setLabel(""+KyoroSetting.getData(KyoroSetting.TAG_PACKAGE));
        mStage = new SimpleStage(this);
        mStage.getRoot().addChild(mLineChart);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        setContentView(mStage);
        setMenuAction(new MainActivityStartAction());
        setMenuAction(new MainActivitySelectPackageAction());
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if(mStage != null){
    		mStage.start();
    	}
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if(mStage != null){
    		mStage.stop();
    	}
    }
}