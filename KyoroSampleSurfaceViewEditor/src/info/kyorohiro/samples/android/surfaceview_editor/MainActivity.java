package info.kyorohiro.samples.android.surfaceview_editor;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	private Editor mViewer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mViewer = new Editor(this));
	}

	@Override
	protected void onResume() {
		super.onResume();
		mViewer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mViewer.stop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mViewer.showInputConnection();
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
