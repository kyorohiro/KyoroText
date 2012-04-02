package info.kyorohiro.helloworld.memo;

import java.io.File;
import java.io.FileNotFoundException;
 
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer;
import info.kyorohiro.helloworld.android.util.SimpleFileExplorer.SelectedFileAction;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleEditText;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.display.widget.FlowingLineViewWithFile;
import info.kyorohiro.helloworld.display.widget.SimpleFilterableLineView;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;
import info.kyorohiro.helloworld.memo.task.ShowFileContentTask;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class KyoroMemoActivity extends Activity {

	public static final String MENU_LABEL_OPEN_FILE = "open file";

	private SimpleStage mStage = null;
	private SimpleEditText mViewer = null;
	private SimpleCircleController mCircle = null;
	private FlowingLineData mInputtedText = new FlowingLineData(
			1000, 1000, 14);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mStage = new SimpleStage(this);
		mViewer = new SimpleEditText(mStage);
		mCircle = new SimpleCircleController();

		mStage.getRoot().addChild(new Layout());
		mStage.getRoot().addChild(mViewer);
		mStage.getRoot().addChild(mCircle);
		mCircle.setEventListener(new MyCircleControllerEvent());
		setContentView(mStage);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mStage != null) {
			mStage.start();
		}
	}

	@Override
	protected void onPause() {
		if (mStage != null) {
			mStage.stop();
		}
		super.onStop();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(MENU_LABEL_OPEN_FILE);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item != null) {
			if (MENU_LABEL_OPEN_FILE.equals("" + item.getTitle())) {
				SimpleFileExplorer explorer = new SimpleFileExplorer(this,
						this, Environment.getExternalStorageDirectory(),
						SimpleFileExplorer.MODE_FILE_SELECT);
				explorer.show();
				explorer.setOnSelectedFileAction(new SelectedFileAction() {
					@Override
					public boolean onSelectedFile(File file, String action) {
						if (file == null || !file.exists() || !file.isFile()
								|| file.isDirectory()) {
							return false;
						}
						ShowFileContentTask task;
						try {
							mViewer.start(file);
							return true;
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							return false;
						}
					}
				});

			}
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private class MyCircleControllerEvent implements
			SimpleCircleController.CircleControllerAction {
		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				mViewer.setPosition(mViewer.getPosition() + rateDegree * 2);
			}
		}

		public void upButton(int action) {
			mViewer.setPosition(mViewer.getPosition() + 5);
		}

		public void downButton(int action) {
			mViewer.setPosition(mViewer.getPosition() - 5);
		}
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			int x = graphics.getWidth() - mCircle.getWidth() / 2;
			int y = graphics.getHeight() - mCircle.getHeight() / 2;
			KyoroMemoActivity.this.mCircle.setPoint(x, y);
		}
	}
}