package info.kyorohiro.helloworld.j2se.adapter;

import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimplePoint;
import info.kyorohiro.helloworld.display.simple.SimpleStage;

public class SimpleStageForJ2SE implements SimpleStage {
	private int mClearColor = 0x00;
	private SimpleDisplayObjectContainer mRoot = new SimpleDisplayObjectContainer();

	@Override
	public boolean isAlive() {
		return false;
	}


	@Override
	public void setColor(int clearColor) {
		mClearColor = clearColor;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
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
	public void stop() {
		// TODO Auto-generated method stub
		
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

	private Thread mCurrentThread = null;
	private class Animation implements Runnable {
		@Override
		public void run() {
			try {
				System.out.println("--begin animation--");
				Thread currentThread = null;
				while(true) {
					mCurrentThread = currentThread;
					if(currentThread == null || currentThread != Thread.currentThread()) {
						break;
					}
					
					
				}
			} finally {
				System.out.println("--end animation--");
			}
		}
	}
}
