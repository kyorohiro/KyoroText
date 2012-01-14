package info.kyorohiro.meminfo;

import info.kyorohiro.helloworld.base.TestActivity;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleLineChart;
import android.app.Activity;
import android.os.Bundle;

public class KyoroMemoryInfoActivity extends TestActivity {
	private SimpleStage stage = null;
	private SimpleLineChart chart = null;

	private SimpleCircleController circle = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stage = new SimpleStage(getApplicationContext());
        circle = new SimpleCircleController();
        chart = new  SimpleLineChart();
        stage.getRoot().addChild(chart);
        stage.getRoot().addChild(circle);
        setContentView(stage);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	stage.start();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	stage.stop();
    }
}