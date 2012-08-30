package info.kyorohiro.helloworld.textviewer.manager;

import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.textviewer.viewer.TextViewer;

public class LineViewGroup extends SimpleDisplayObjectContainer{

	private TextViewer mTextViewer = null;
	private SeparateUI mSeparate = null;

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
		SimpleDisplayObject[] obj = new SimpleDisplayObject[2];

		int j=0;
		for(int i=0;i<numOfChild();i++) {
			if(getChild(i) instanceof TextViewer||getChild(i) instanceof LineViewGroup) {
				obj[j] = getChild(i);
				j++;
				if(j>=2){
					break;
				}
			}
		}
		if(mSeparate.isVertical()){
			int z = (int)(getWidth(false)*mSeparate.getPersentY());
			if(j>=2){
				obj[0].setPoint(0, 0);
				obj[0].setRect(z, getHeight(false));
				obj[1].setPoint(z, 0);
				obj[1].setRect(getWidth(false)-z, getHeight(false));
			} else {
				obj[0].setPoint(0, 0);
				obj[0].setRect(getWidth(false), getHeight(false));			
			}
			mSeparate.setPoint(z, mSeparate.getY());
		} else {
			int z = (int)(getHeight(false)*mSeparate.getPersentY());
			if(j>=2){
				obj[0].setPoint(0, 0);
				obj[0].setRect(getWidth(false), z);
				obj[1].setPoint(0, z);
				obj[1].setRect(getWidth(false), getHeight(false)-z);
			} else {
				obj[0].setPoint(0, 0);
				obj[0].setRect(getWidth(false), getHeight(false));			
			}
			mSeparate.setPoint(mSeparate.getX(), z);
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
		} else if(child instanceof SeparateUI) {
			mSeparate = (SeparateUI)child;
			super.addChild(child);
		}
		else {
			super.addChild(child);
		}
	}

	public void divide(SeparateUI separate) {
		if(separate.getPersentY()>0.5){
			addChild(new LineViewGroup(LineViewManager.getManager().newTextViewr()));
			addChild(new LineViewGroup(mTextViewer));
		} else{
			addChild(new LineViewGroup(mTextViewer));
			addChild(new LineViewGroup(LineViewManager.getManager().newTextViewr()));
		}
		removeChild(mTextViewer);
		mTextViewer = null;
	}

	public void combine(SeparateUI separate) {
		Object parent = getParent();
		Object child = null;
		if(separate.getPersentY()>0.5){
			child = getChild(0);
		} else{
			child = getChild(1);
		}
		if(child != null){
			// refactaring
			int index = ((SimpleDisplayObjectContainer)parent).getIndex(this);
			this.removeChild((SimpleDisplayObject)child);
			((SimpleDisplayObjectContainer)parent).insertChild(index, (SimpleDisplayObject)child);//addChild((SimpleDisplayObject)child);
			((SimpleDisplayObjectContainer)parent).removeChild(this);
			
			dispose();
		} 
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
