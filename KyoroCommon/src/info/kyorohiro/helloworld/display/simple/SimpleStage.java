package info.kyorohiro.helloworld.display.simple;

import info.kyorohiro.helloworld.android.adapter.SimpleGraphicsForAndroid;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SimpleStage extends EditableSurfaceView {
//SurfaceView {
	private InitialCallBack mInitialCallBack = null;
	private Thread mCurrentThread = null;
	private SimpleDisplayObjectContainer mRoot = new  SimpleDisplayObjectContainer();

	public SimpleStage(Context context) {
		super(context);
		mRoot.setParent(this);
		mInitialCallBack = new InitialCallBack();
		getHolder().addCallback(mInitialCallBack);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.setClickable(true);
		this.setFocusable(true);
	}

	public synchronized void start() {
		mCurrentThread = new Thread(new Animation());
		mCurrentThread.start();
	}

	public SimpleDisplayObjectContainer getRoot(){
		return mRoot;
	}

	public synchronized void stop() {
		Thread tmp = mCurrentThread;
		mCurrentThread = null;
		if (tmp != null && tmp.isAlive()) {
			tmp.interrupt();
		}
	}

	private class Animation implements Runnable {
		public void run() {
			// wait by finish initial function.
			SimpleStage.this.mInitialCallBack.waitForSurfaceCreated();

			SurfaceHolder holder = SimpleStage.this.getHolder();
			try {
				while (true) {
					if (mCurrentThread == null || mCurrentThread != Thread.currentThread()) {
						break;
					}
					Canvas canvas = holder.lockCanvas();
					try {
						doDraw(canvas);
					} finally {
						holder.unlockCanvasAndPost(canvas);
					}
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	private void doDraw(Canvas canvas) {
		canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
		mRoot.paint(new SimpleGraphicsForAndroid(canvas, 0, 0));
	}

	final static int MULTI_TOUCH_MAX = 5;
	public static SimplePoint[] mMultiTouchPoints = new SimplePoint[MULTI_TOUCH_MAX];
	static {
		for(int i=0;i<mMultiTouchPoints.length;i++){
			mMultiTouchPoints[i] = new SimplePoint(0, 0, false);
		}
	}

	public boolean isSupportMultiTouch() {
		if(Build.VERSION.SDK_INT >= 7) {
			return true;
		} else {
			return false;
		}
	}

	public SimplePoint [] getMultiTouchEvent() {
		return mMultiTouchPoints;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isSupportMultiTouch()) {
			int count = event.getPointerCount();
			for(int j=0;j<mMultiTouchPoints.length;j++){
				mMultiTouchPoints[j].isVisible(false);
			}
			for(int i=0; i<count; i++) {
				int id = event.getPointerId(i);
				if(id<mMultiTouchPoints.length) {
					mMultiTouchPoints[id].setPoint((int)event.getX(i), (int)event.getY(i));
					mMultiTouchPoints[id].isVisible(true);
					//android.util.Log.v("kiyohiro","["+id+"]="+event.getX(i)+","+event.getY());
				}
			}
		}

		mRoot.onTouchTest(
				(int) event.getX() - mRoot.getX(),
				(int) event.getY() - mRoot.getY(),
				event.getAction());
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		getRoot().onKeyDown(keyCode);
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		getRoot().onKeyUp(keyCode);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		float y = event.getY();
		float x = event.getX();
	/*	if(x*x < y*y){
			if(event.getY() > 0){
				getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
				getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
			}
			else if(event.getY() < 0) {
				getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
				getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
			}			
		}
		else {*/
			if(event.getX() > 0){
				getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
				getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
			}
			else if(event.getX() < 0) {
				getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
				getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
			}
//		}
		return super.onTrackballEvent(event);
	}

	private class InitialCallBack implements SurfaceHolder.Callback {
		private boolean initialized = false;

		public synchronized void waitForSurfaceCreated() {
			if (initialized == true) {
				return;
			}

			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		private synchronized void notifyAboutWaitForSurfaceCreated() {
			initialized = true;
			notifyAll();
		}

		public void surfaceCreated(SurfaceHolder holder) {
			notifyAboutWaitForSurfaceCreated();
			Canvas canvas = holder.lockCanvas();
			try {
				doDraw(canvas);
			} finally {
				holder.unlockCanvasAndPost(canvas);
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {initialized = false;}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		}
	}
}