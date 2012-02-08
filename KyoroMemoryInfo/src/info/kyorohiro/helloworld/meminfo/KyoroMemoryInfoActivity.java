package info.kyorohiro.helloworld.meminfo;

import info.kyorohiro.helloworld.android.base.TestActivity;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.FlowingLineViewWithFilter;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleLineChart;
import info.kyorohiro.helloworld.meminfo.tasks.ShowMemoryInfoTask;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class KyoroMemoryInfoActivity extends TestActivity {
	private SimpleStage mStage = null;
	private SimpleLineChart mChart = null;
	private FlowingLineViewWithFilter mList = null;

	private SimpleCircleController mCircle = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStage = new SimpleStage(getApplicationContext());
        mCircle = new SimpleCircleController();
        mChart = new SimpleLineChart();
        mList  = new FlowingLineViewWithFilter(null);
        mStage.getRoot().addChild(mList);
        mStage.getRoot().addChild(mChart);
        mStage.getRoot().addChild(mCircle);
        setContentView(mStage);

        
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        mList.getCyclingFlowingLineData().setWidth(display.getWidth()*9/10);
        mList.getCyclingFlowingLineData().addLinePerBreakText("---------------------a---------------------");
        mList.getCyclingFlowingLineData().addLinePerBreakText("---------------------b1---------------------"+
        		"---------------------b2---------------------"+"---------------------b3---------------------");
        mList.getCyclingFlowingLineData().addLinePerBreakText("---------------------c---------------------");
        mList.getCyclingFlowingLineData().addLinePerBreakText("---------------------d---------------------");
        mList.getCyclingFlowingLineData().addLinePerBreakText("---------------------e---------------------");
        mList.getCyclingFlowingLineData().addLinePerBreakText("---------------------f---------------------");

    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mStage.start();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	mStage.stop();
    }
    
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	super.onPrepareOptionsMenu(menu);
    	menu.add("start");
    	return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	if(item.getTitle().equals("start")){
    		Thread th = new ShowMemoryInfoTask(this.getApplicationContext(), mChart.getChartData());
    		th.start();
    	}
    	return super.onMenuItemSelected(featureId, item);
    }    

}