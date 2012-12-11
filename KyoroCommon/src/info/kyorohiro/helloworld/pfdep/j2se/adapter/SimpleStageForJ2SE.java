package info.kyorohiro.helloworld.pfdep.j2se.adapter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.im.InputMethodRequests;
import java.awt.im.InputSubset;
import java.text.AttributedCharacterIterator;

import javax.management.Attribute;
import javax.swing.JPanel;

import info.kyorohiro.helloworld.display.simple.MyInputConnection;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleMotionEvent;
import info.kyorohiro.helloworld.display.simple.SimplePoint;
import info.kyorohiro.helloworld.display.simple.SimpleStage;

public class SimpleStageForJ2SE extends EditableComponent implements SimpleStage, 
MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int mClearColor = 0xFFAAAAFF;
	private int mSleep = 50;
	private SimpleDisplayObjectContainer mRoot = new SimpleDisplayObjectContainer();

	public SimpleStageForJ2SE() {
		super("nnn", true);
		setFocusable(true);
		mRoot.setParent(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	   	setEnabled(false);
	}
	
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
	   	setEnabled(true);
		// TODO Auto-generated method stub
        //Character.Subset[] subsets = { InputSubset.KANJI };
       // this.getInputContext().setCompositionEnabled(true);
       //this.addInputMethodListener(this);
       // this.getInputContext().setCharacterSubsets(subsets);
	}

	@Override
	public void hideInputConnection() {
	   	setEnabled(false);
		// TODO Auto-generated method stub
	}

	@Override
	public MyInputConnection getCurrentInputConnection() {
		// TODO now 
		return super.getCurrentInputConnection();
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

/*	private void doIME() {
		InputMethodRequests req = getInputMethodRequests();
		if(req == null) {
			return;
		}
		int len = req.getCommittedTextLength();
		AttributedCharacterIterator.Attribute[] attributes = new AttributedCharacterIterator.Attribute[len];
		req.getCommittedText(0, len, attributes);
		
		for(int i=0;i<len;i++) {
			System.out.println(attributes[i].toString());
		}
	}*/
	private void doDraw() {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
//	@Override
//	public void paint(Graphics g) {
		///*
		setVisible(true);
//		setPreferredSize(new Dimension(400, 400));
		g.setColor(new Color(mClearColor));
		g.clearRect(0, 0, getWidth(), getHeight());
	//	System.out.println("--begin paint--"+0+","+ 0+","+getWidth()+","+ getHeight());
		mRoot.paint(new SimpleGraphicsForJ2SE((Graphics2D)g, 0, 0, getWidth(), getHeight()));
	//	System.out.println("--end paint--");
	 //*/
	}

	private Thread mCurrentThread = null;
	private class Animation implements Runnable {
		@Override
		public void run() {
			try {
//				System.out.println("--begin animation--");
				Thread currentThread = null;
				while(true) {
					currentThread = mCurrentThread;
					if(currentThread == null || currentThread != Thread.currentThread()) {
						break;
					}
//					doIME();
					doDraw();
					try {
						Thread.sleep(mSleep);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
			catch(Throwable t) {
//				System.out.println("--error animation--");
				t.printStackTrace();
			}
			finally {
//				System.out.println("--end animation--");
			}
		}
	}

	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	private boolean mIsEntered = false;
	@Override
	public void mouseEntered(MouseEvent e) {
		mIsEntered = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mIsEntered = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println("mousePressed x="+x+",y="+y+","+mIsEntered);
		if(mIsEntered) {
			getRoot().onTouchTest(x, y, SimpleMotionEvent.ACTION_DOWN);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println("mouseRelessed x="+x+",y="+y+","+mIsEntered);
		if(mIsEntered) {
			getRoot().onTouchTest(x, y, SimpleMotionEvent.ACTION_UP);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println("mouseDragged x="+x+",y="+y+","+mIsEntered);
		if(mIsEntered) {
			getRoot().onTouchTest(x, y, SimpleMotionEvent.ACTION_MOVE);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		System.out.println("mouseMoved x="+x+",y="+y+","+mIsEntered);
	}
}
