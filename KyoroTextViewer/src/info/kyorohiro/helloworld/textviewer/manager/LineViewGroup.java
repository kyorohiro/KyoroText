package info.kyorohiro.helloworld.textviewer.manager;

import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;

public class LineViewGroup extends SimpleDisplayObjectContainer{

	private TextViewer mTextViewer = null;
	public LineViewGroup(TextViewer textViewer) {
		doAddSeparator();
		addChild(textViewer);
	}

	private void doAddSeparator() {
		addChild(new SeparateUI(this));
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		int y=0;
		android.util.Log.v("kiyo","num="+numOfChild()+","+graphics.getHeight()+","+getHeight(false));
		for(int i=0;i<numOfChild();i++) {
			if(getChild(i) instanceof TextViewer||getChild(i) instanceof LineViewGroup) {
				getChild(i).setPoint(0, y);
				getChild(i).setRect(getWidth(false), getHeight(false)/(numOfChild()-1));
				y +=this.getHeight(false)/(numOfChild()-1);
			}
		}
		super.paint(graphics);
	}

	@Override
	public void addChild(SimpleDisplayObject child) {
		if(child instanceof TextViewer) {
			mTextViewer = (TextViewer)child;
			int num = super.numOfChild()-1;
		 	if(num<0){num = 0;}
			super.insertChild(num, child);
		} else if(child instanceof LineViewGroup) {
			int num = super.numOfChild()-1;
		 	if(num<0){num = 0;}
			super.insertChild(num, child);			
		}else {
			super.addChild(child);
		}
	}

	public void divide(SeparateUI separate) {
		addChild(new LineViewGroup(mTextViewer));
		addChild(new LineViewGroup(LineViewManager.getManager().newTextViewr()));
		removeChild(mTextViewer);
		mTextViewer = null;
	}

	public void combine(SeparateUI separate) {
		Object parent = getParent();
		Object child = getChild(0);
		//if(parent instanceof LineViewGroup//&&child instanceof LineViewGroup
		//		){
		if(child != null) {
			// refactaring
			((SimpleDisplayObjectContainer)parent).removeChild(this);
			((SimpleDisplayObjectContainer)parent).addChild((SimpleDisplayObject)child);
		}
		//}
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if(MotionEvent.ACTION_DOWN == action){
			focusTest(x, y);
		}
		return super.onTouchTest(x, y, action);
	}

	private void focusTest(int x, int y) {
		for(int i=0;i<numOfChild();i++) {
			if(getChild(i) instanceof TextViewer) {
				int cx = ((TextViewer)getChild(i)).getX();
				int cy = ((TextViewer)getChild(i)).getY();
				int cw = ((TextViewer)getChild(i)).getWidth();
				int ch = ((TextViewer)getChild(i)).getHeight();
				if(cx<x&&x<cx+cw) {
					if(cy<y&&y<cy+ch){
						LineViewManager.getManager().changeFocus((TextViewer)getChild(i));
						break;
					}
				}
			}
		}		
	}

}
