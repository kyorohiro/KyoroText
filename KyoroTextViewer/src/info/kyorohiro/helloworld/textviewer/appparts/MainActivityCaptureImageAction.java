package info.kyorohiro.helloworld.textviewer.appparts;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.kyorohiro.helloworld.android.base.MainActivityMenuAction;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleStage;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ViewAnimator;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivityCaptureImageAction implements MainActivityMenuAction {

	public static String TITLE = "capture image";
	private TextViewer mDisplayedTextViewer = null;

	public MainActivityCaptureImageAction(TextViewer viewer) {
		mDisplayedTextViewer = viewer;
	}

	public boolean onPrepareOptionsMenu(Activity activity, Menu menu) {
		menu.add(TITLE);
		return false;
	}

	public boolean onMenuItemSelected(Activity activity, int featureId, MenuItem item) {
		if(item.getTitle().equals(TITLE)) {
			Runnable r = new CaptureTask();
//			activity.runOnUiThread(r);
			Thread t = new Thread(r);
			t.start();
			return true;
		}
		return false;
	}

	public class CaptureTask implements Runnable {
		@Override
		public void run() {
			Bitmap bitmap = Bitmap.createBitmap(
					mDisplayedTextViewer.getWidth(), 
					mDisplayedTextViewer.getHeight(), 
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			try {
				SimpleStage stage = SimpleDisplayObject.getStage(mDisplayedTextViewer);
				//stage.stop();
				//Thread.sleep(3000);
				Class clazz = SimpleStage.class;
				Rect r = new Rect(0, 0, 
						mDisplayedTextViewer.getWidth(), 
						mDisplayedTextViewer.getHeight());
				//SurfaceHolder h = stage.getHolder();
				//Surface s = h.getSurface();
				Canvas a = null;
				try {
//				Surface.screenshot(mDisplayedTextViewer.getWidth(), mDisplayedTextViewer.getHeight());
					//					public static native Bitmap screenshot(int width, int height);
					//a =s.lockCanvas(r);
//					a = h.lockCanvas();
//					Paint p = new Paint();
//					a.drawBitmap(bitmap, 0, 0, p);
					a.setBitmap(bitmap);
					Method method = Surface.class.getMethod("screenshot", int.class, int.class);
					bitmap = (Bitmap)method.invoke(null, mDisplayedTextViewer.getWidth(), mDisplayedTextViewer.getHeight());
					
					saveBitmapTest(bitmap);
					bitmap.recycle();
				}finally {
//					s.unlockCanvas(a);
				//h.unlockCanvasAndPost(a);
				}

				//Method method = clazz.getMethod("draw", Canvas.class);
				//method.invoke(stage, canvas);
				//stage.start();
			} catch(Throwable  e){
				e.printStackTrace();
			}
		}
	}

	public static void saveBitmapTest(Bitmap mBitmap) {
		try {
			File root = Environment.getExternalStorageDirectory();
			Date mDate = new Date();
			SimpleDateFormat fileName = new SimpleDateFormat("yyyy'y'MM'M'dd'd'_HH'H'mm'm'ss's'");
			File path = new File(root, fileName.format(mDate) + ".jpeg");
			if(!path.exists()){
				path.createNewFile();
			}
			FileOutputStream fos = null;
			fos = new FileOutputStream(path);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
}
