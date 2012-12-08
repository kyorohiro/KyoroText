package info.kyorohiro.helloworld.j2se.adapter;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimplePoint;
import info.kyorohiro.helloworld.display.simple.SimpleStage;

public class SimpleStageForJ2SE extends JPanel implements SimpleStage {
	private int mClearColor = 0x00;
	private int mSleep = 50;
	private SimpleDisplayObjectContainer mRoot = new SimpleDisplayObjectContainer();

	
	@Override
	public boolean isAlive() {
		if (mCurrentThread == null || !mCurrentThread.isAlive()) {
			return false;
		} else {
			return true;
		}
	}


	@Override
	public void setColor(int clearColor) {
		mClearColor = clearColor;
	}

	@Override
	public synchronized void start() {
		if (!isAlive()) {
			mCurrentThread = new Thread(new Animation());
			mCurrentThread.start();
		}
	}

	@Override
	public void resetTimer() {
		// nothing 
	}

	@Override
	public SimpleDisplayObjectContainer getRoot() {
		return mRoot;
	}

	@Override
	public synchronized void stop() {
		if (isAlive()) {
			Thread tmp = mCurrentThread;
			mCurrentThread = null;
			if (tmp != null && tmp.isAlive()) {
				tmp.interrupt();
			}
		}
	}
	
	@Override
	public void showInputConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideInputConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MyInputConnection getCurrentInputConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSupportMultiTouch() {
		// j2se not support multitouch
		return false;
	}

	@Override
	public SimplePoint[] getMultiTouchEvent() {
		// j2se not support multitouch
		return new SimplePoint[0];
	}

	private void doDraw() {
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		//System.out.println("--begin paint--");
		mRoot.paint(new SimpleGraphicsForJ2SE((Graphics2D)g, 0, 0));
		//System.out.println("--end paint--");
	}

	private Thread mCurrentThread = null;
	private class Animation implements Runnable {
		@Override
		public void run() {
			try {
				System.out.println("--begin animation--");
				Thread currentThread = null;
				while(true) {
					currentThread = mCurrentThread;
					if(currentThread == null || currentThread != Thread.currentThread()) {
						break;
					}
					doDraw();
					try {
						Thread.sleep(mSleep);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
			catch(Throwable t) {
				System.out.println("--error animation--");
				t.printStackTrace();
			}
			finally {
				System.out.println("--end animation--");
			}
		}
	}
}
