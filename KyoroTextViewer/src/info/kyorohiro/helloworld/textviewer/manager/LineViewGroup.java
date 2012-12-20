package info.kyorohiro.helloworld.textviewer.manager;

//import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleMotionEvent;
import info.kyorohiro.helloworld.ext.textviewer.viewer.TextViewer;

public class LineViewGroup extends SimpleDisplayObjectContainer{

	private TextViewer mTextViewer = null;
	private SeparateUI mSeparate = null;

	public LineViewGroup(TextViewer textViewer) {
		doAddSeparator();
		addChild(textViewer);
	}

	private void doAddSeparator() {
		SeparateUI s=new SeparateUI(this);
		addChild(s);
	}

	// following code isrefactring targe
	// isEdit() and IsGuard is same code posint.
	public boolean isGuard() {
		if(isGuard(getChild(0))){
			return true;
		}
		else if(isGuard(getChild(1))){
			return true;
		}
		else {
			return false;
		}
	}

	private static boolean isGuard(SimpleDisplayObject child) {
		if(child == null){
			return false;
		}
		if(child instanceof TextViewer){
			TextViewer v = (TextViewer)child;
			return v.isGuard();
		}
		else if(child instanceof LineViewGroup){
			LineViewGroup v = (LineViewGroup)child;
			return v.isGuard();
		}
		else  {
			return false;
		}
	}

	public boolean isEdit() {
		if(isEdit(getChild(0))){
			return true;
		}
		else if(isEdit(getChild(1))){
			return true;
		}
		else {
			return false;
		}
	}

	private static boolean isEdit(SimpleDisplayObject child) {
		if(child == null){
			return false;
		}
		if(child instanceof TextViewer){
			TextViewer v = (TextViewer)child;
			return v.isEdit();
		}
		else if(child instanceof LineViewGroup){
			LineViewGroup v = (LineViewGroup)child;
			return v.isEdit();
		}
		else  {
			return false;
		}
	}

	@Override
	public void paint(SimpleGraphics graphics) {
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
		}
		else if(child instanceof SeparateUI) {
			mSeparate = (SeparateUI)child;
			super.addChild(child);
		}
		else if(child instanceof LineViewGroup) {
			int num = super.numOfChild()-1;
		 	if(num<0){num = 0;}
			super.insertChild(num, child);			
		}
		else {
			super.addChild(child);
		}
	}

	public void divide(SeparateUI separate) {
		
		// todo following yaxtuke sigoto
		if(numOfChild()>=3){
			return;
		}
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
		SimpleDisplayObject child = null;
		SimpleDisplayObject kill = null;
		
		if(separate.getPersentY()>0.5){
			child = getChild(0);
			kill = getChild(1);
		} else{
			child = getChild(1);
			kill = getChild(0);
		}
		if(!LineViewManager.getManager().notifyEvent(child, kill)){
			mSeparate.resetPosition();
			return;
		}
		if(child != null){
			// refactaring
			int index = ((SimpleDisplayObjectContainer)parent).getIndex(this);
			this.removeChild(child);
			((SimpleDisplayObjectContainer)parent).insertChild(index, child);//addChild((SimpleDisplayObject)child);
			((SimpleDisplayObjectContainer)parent).removeChild(this);
			if(includeFocusingChild()) {
				chFocus((SimpleDisplayObject)parent); 
			}
			dispose();
		} 
	}

	private boolean includeFocusingChild() {
		SimpleDisplayObject o = LineViewManager.getManager().getFocusingTextViewer();
		SimpleDisplayObject c = LineViewManager.getManager().getFocusingTextViewer();
		SimpleDisplayObject root = SimpleDisplayObject.getStage(o).getRoot();
		while(o != null && o!=root) {
			if(c == o){
				return true;
			}
			o = (SimpleDisplayObject)o.getParent();
		}
		return false;
	}

	private boolean  chFocus(SimpleDisplayObject parent) {
		if(parent instanceof TextViewer) {
			TextViewer v = (TextViewer)parent;
			v.getLineView().isFocus(true);
			LineViewManager.getManager().changeFocus(v);
			return true;
		}
		if(parent instanceof SimpleDisplayObjectContainer){
			SimpleDisplayObjectContainer _parent = (SimpleDisplayObjectContainer)parent;
			for(int i=0;i<_parent.numOfChild();i++){
				if(_parent.getChild(i) instanceof TextViewer || _parent.getChild(i) instanceof LineViewGroup) {
					if(chFocus(_parent.getChild(i))){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onTouchTest(int x, int y, int action) {
		if(SimpleMotionEvent.ACTION_DOWN == action){
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
