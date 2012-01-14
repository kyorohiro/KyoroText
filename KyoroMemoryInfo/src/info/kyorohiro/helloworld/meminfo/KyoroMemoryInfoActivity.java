package info.kyorohiro.helloworld.meminfo;

import info.kyorohiro.helloworld.base.TestActivity;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleLineChart;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class KyoroMemoryInfoActivity extends TestActivity {
	private SimpleStage mStage = null;
	private SimpleLineChart mChart = null;

	private SimpleCircleController circle = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStage = new SimpleStage(getApplicationContext());
        circle = new SimpleCircleController();
        mChart = new  SimpleLineChart();
        mStage.getRoot().addChild(mChart);
        mStage.getRoot().addChild(circle);
        setContentView(mStage);
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