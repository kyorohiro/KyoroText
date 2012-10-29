package info.kyorohiro.helloworld.display.simple;

import info.kyorohiro.helloworld.android.adapter.EditableSurfaceView;
import info.kyorohiro.helloworld.android.adapter.SimpleGraphicsForAndroid;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * todo Androidに依存している部分は分離する
 */
public class SimpleStage extends EditableSurfaceView {
	// SurfaceView {
	private InitialCallBack mInitialCallBack = null;
	private Thread mCurrentThread = null;
	private SimpleDisplayObjectContainer mRoot = new SimpleDisplayObjectContainer();
	private int mSleep = 50;
	private int mBgColor = 0xFFFFFFFF;

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
		if (mCurrentThread == null || !mCurrentThread.isAlive()) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized void setColor(int bgcolor) {
		mBgColor = bgcolor;
	}
	public synchronized void start() {
		if (!isAlive()) {
			mCurrentThread = new Thread(new Animation());
			mCurrentThread.start();
			mCountForLogicalSleep = 0;
		} else {
			mCountForLogicalSleep = 0;
		}
	}

	@Deprecated
	public synchronized void resetTimer() {
		mCountForLogicalSleep = 0;
	}

	public SimpleDisplayObjectContainer getRoot() {
		return mRoot;
	}

	public synchronized void stop() {
		if (isAlive()) {
			Thread tmp = mCurrentThread;
			mCurrentThread = null;
			if (tmp != null && tmp.isAlive()) {
				tmp.interrupt();
			}
		}
	}

	private static int mCountForLogicalSleep = 0;

	protected void logicalSleepForCpuUage() throws InterruptedException {
		if (mCountForLogicalSleep < 30) {
			Thread.sleep(mSleep);
		} else if (mCountForLogicalSleep < 60) {
			// Thread.sleep(mSleep*4);
			sleepPlus(8);
		} else if (mCountForLogicalSleep < 120) {
			// Thread.sleep(mSleep*8);
			sleepPlus(16 * 2);
		} else if (mCountForLogicalSleep < 240) {
			// Thread.sleep(mSleep*16);
			sleepPlus(32 * 2);
		} else if (mCountForLogicalSleep < 480) {
			// Thread.sleep(mSleep*20);//
			sleepPlus(64 * 2);
		}
		if (mCountForLogicalSleep < 9999) {
			mCountForLogicalSleep++;
		}
	}

	private void sleepPlus(int num) throws InterruptedException {
		for (int i = 0; i < num && mCountForLogicalSleep >= 50; i++) {
			Thread.sleep(mSleep);
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
//				long startTimeForSpeedCheck = 0;
//				long endTimeForSpeedCheck = 0;
				while (true) {
					if (mCurrentThread == null
							|| mCurrentThread != Thread.currentThread()) {
						break;
					}
//					startTimeForSpeedCheck = System.currentTimeMillis();
					try {
						canvas = holder.lockCanvas();
						doDraw(canvas);
					} finally {
						if (canvas != null) {
							holder.unlockCanvasAndPost(canvas);
						}
					}
//					endTimeForSpeedCheck = System.currentTimeMillis();
//					android.util.Log.v("time","time="+(endTimeForSpeedCheck-startTimeForSpeedCheck));
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
		canvas.drawColor(mBgColor,Mode.SRC);
		mRoot.paint(new SimpleGraphicsForAndroid(canvas, 0, 0));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mCountForLogicalSleep = 0;
		boolean ret = super.onTouchEvent(event);
		mRoot.onTouchTest((int) event.getX() - mRoot.getX(), (int) event.getY()
				- mRoot.getY(), event.getAction());
		if (isAlive()) {
			return ret;
		} else {
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
		if (event.getX() > 0) {
			getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_DOWN);
			getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN);
		} else if (event.getX() < 0) {
			getRoot().onKeyUp(KeyEvent.KEYCODE_DPAD_UP);
			getRoot().onKeyDown(KeyEvent.KEYCODE_DPAD_UP);
		}
		return super.onTrackballEvent(event);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		resetTimer();
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

		public void surfaceDestroyed(SurfaceHolder holder) {
			initialized = false;
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}
	}
}