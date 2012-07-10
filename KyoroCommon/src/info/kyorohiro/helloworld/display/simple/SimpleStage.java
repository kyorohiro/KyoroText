package info.kyorohiro.helloworld.display.simple;

import info.kyorohiro.helloworld.android.adapter.SimpleGraphicsForAndroid;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
public class SimpleStage extends EditableSurfaceView {
//SurfaceView {
	private InitialCallBack mInitialCallBack = null;
	private Thread mCurrentThread = null;
	private SimpleDisplayObjectContainer mRoot = new  SimpleDisplayObjectContainer();
	private int mSleep = 50;

	public SimpleStage(Context context) {
		super(context);
		mRoot.setParent(this);
		mInitialCallBack = new InitialCallBack();
		getHolder().addCallback(mInitialCallBack);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		this.setClickable(true);
		this.setFocusable(true);
	}

	public boolean isAlive() {
		if(mCurrentThread == null || !mCurrentThread.isAlive()){
			return false;
		}else {
			return true;
		}
	}

	public synchronized void start() {
		if(!isAlive()){
			mCurrentThread = new Thread(new Animation());
			mCurrentThread.start();
		} else {
			mCountForLogicalSleep = 0;
		}
	}

	public SimpleDisplayObjectContainer getRoot(){
		return mRoot;
	}

	public synchronized void stop() {
		if(isAlive()){
			Thread tmp = mCurrentThread;
			mCurrentThread = null;
			if (tmp != null && tmp.isAlive()) {
				tmp.interrupt();
			}
		}
	}

	private static int mCountForLogicalSleep = 0;
	protected void logicalSleepForCpuUage() throws InterruptedException {
		if(mCountForLogicalSleep<60) {
			Thread.sleep(mSleep);
		}
		else if(mCountForLogicalSleep<2*120){
			Thread.sleep(mSleep*4);
		}
		else if(mCountForLogicalSleep<4*240){
			Thread.sleep(mSleep*6);
		}
		else if(mCountForLogicalSleep<480){
			Thread.sleep(mSleep*8);
		}
		else if(mCountForLogicalSleep<960){
			Thread.sleep(mSleep*12);
		} else {
			mCountForLogicalSleep++;
		}
	}

	private class Animation implements Runnable {
		public void run() {
			// wait by finish initial function.
			SimpleStage.this.mInitialCallBack.waitForSurfaceCreated();

			SurfaceHolder holder = SimpleStage.this.getHolder();
			try {
				Canvas canvas = null;
				 mCountForLogicalSleep = 0;
				getRoot().start();
				while (true) {
					if (mCurrentThread == null || mCurrentThread != Thread.currentThread()) {
						break;
					}
					try {
						canvas = holder.lockCanvas();
						doDraw(canvas);
					} 
					finally {
						if(canvas != null){
							holder.unlockCanvasAndPost(canvas);
						}
					}
					logicalSleepForCpuUage();
				}
			} catch (InterruptedException e) {
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				getRoot().stop();
			}
		}
	}

	private void doDraw(Canvas canvas) {
		canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
		mRoot.paint(new SimpleGraphicsForAndroid(canvas, 0, 0));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mCountForLogicalSleep = 0;
		boolean ret = super.onTouchEvent(event);
		mRoot.onTouchTest(
				(int) event.getX() - mRoot.getX(),
				(int) event.getY() - mRoot.getY(),
				event.getAction());
		if(isAlive()){
			return ret;
		}else {
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		mCountForLogicalSleep = 0;
		getRoot().onKeyDown(keyCode);
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		mCountForLogicalSleep = 0;
		getRoot().onKeyUp(keyCode);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		mCountForLogicalSleep = 0;
		if(event.getX() > 0){
			getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
			getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		else if(event.getX() < 0) {
			getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
			getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
		}
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