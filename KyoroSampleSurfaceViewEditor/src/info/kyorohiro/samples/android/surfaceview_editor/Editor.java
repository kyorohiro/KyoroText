package info.kyorohiro.samples.android.surfaceview_editor;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.inputmethod.InputConnection;

public class Editor extends EditableSurfaceView {
	private Thread mCurrentThread = null;

	public Editor(Context context) {
		super(context);
		setIMEController(new IMEController());
	}

	// 
	// Thread Controll
	//
	public boolean isAlive() {
		if (mCurrentThread == null || !mCurrentThread.isAlive()) {
			return false;
		} else {
			return true;
		}
	}
	public synchronized void start() {
		if (!isAlive()) {
			mCurrentThread = new Thread(new Animation());
			mCurrentThread.start();
		}
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

	//
	// Animation Task
	//
	private class Animation implements Runnable {
		public void run() {
			SurfaceHolder holder = Editor.this.getHolder();
			try {
				Canvas canvas = null;
				while (true) {
					if (mCurrentThread == null || mCurrentThread != Thread.currentThread()) {
						break;
					}
					try {
						canvas = holder.lockCanvas();
						if(canvas != null) {
							doDraw(canvas);
						}
					} finally {
						if (canvas != null) {
							holder.unlockCanvasAndPost(canvas);
						}
					}
					Thread.sleep(200);
				}
			} catch (InterruptedException e) {
			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	//
	// draw
	//
	private void doDraw(Canvas canvas) {
		//android.util.Log.v("kiyo","doDraw="+canvas);
		
		// clear background
		canvas.drawColor(Color.WHITE, Mode.SRC);
		Paint p = new Paint();
		
		// draw text
		p.setColor(Color.RED);
		canvas.drawCircle(109, 100, 200, p);

		// getIMEStack and draw
		drawText(canvas);
	}
	
	private Buffer mBuffer = new Buffer();
	private void drawText(Canvas canvas) {
		MyInputConnection c = getCurrentInputConnection();
		if(c == null) {
			return;
		}
		CharSequence composing = c.getComposingText();
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		//
		canvas.drawText("composingtext = "+composing, 100, 100, p);
		canvas.drawText("mCursor x,y=x:"+mBuffer.getX()+",y:"+mBuffer.getY(), 100, 100+p.getTextSize(), p);
		
		
		CommitText commitText = c.popFirst();
		// show commited text
		while(commitText != null) {
			if(commitText.isKeyCode()) {
				android.util.Log.v("kiyo","#commitText=keucode:"+commitText.getKeyCode());
				switch(commitText.getKeyCode()) {
				case KeyEvent.KEYCODE_ENTER:
					mBuffer.crlf();
					break;
				case KeyEvent.KEYCODE_DPAD_UP:
					mBuffer.up();
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					mBuffer.dowm();
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					mBuffer.right();
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					mBuffer.left();
					break;
				case KeyEvent.KEYCODE_DEL:
				case KeyEvent.KEYCODE_BACK:
					mBuffer.delete();
					break;
				}
					
			}
			else {
				String commit = ""+commitText.getText();
				mBuffer.commit(commit);
			}
 			commitText = c.popFirst();
		}

		mBuffer.drawText(canvas, 120, 120, p);
	}
}
