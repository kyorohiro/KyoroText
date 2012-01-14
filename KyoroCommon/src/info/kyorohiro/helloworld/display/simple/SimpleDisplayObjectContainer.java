package info.kyorohiro.helloworld.display.simple;

import java.util.ArrayList;

public class SimpleDisplayObjectContainer extends SimpleDisplayObject {

	private ArrayList<SimpleDisplayObject> mMyChildren = new ArrayList<SimpleDisplayObject>();

	/**
	 * 
	 */
	public void paintGroup(SimpleGraphics graphics) {
		for(SimpleDisplayObject child: mMyChildren){
			if(child != null) {
				SimpleGraphics childGraphics = new SimpleGraphics(graphics.getCanvas(),
						child.getX()+graphics.getGlobalX(), 
						child.getY()+graphics.getGlobalY());
				child.paint(childGraphics);
			}
		}
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		paintGroup(graphics);
	}

	@Override
	public boolean onKeyDown(int keycode) {
		boolean  evaluated = false;
		for(SimpleDisplayObject child: mMyChildren){
			if(child != null) {
				evaluated = child.onKeyDown(keycode);
				if(evaluated == true){
					//break; todo 
				}
			}
		}
		return evaluated;
	}

	@Override
	public boolean onKeyUp(int keycode) {
		boolean  evaluated = false;
		for(SimpleDisplayObject child: mMyChildren){
			if(child != null) {
				evaluated = child.onKeyUp(keycode);
				if(evaluated == true){
					//break; todo 
				}
			}
		}
		return evaluated;
	}

	public boolean onTouchTest(int x, int y, int action) {
		boolean  touched = false;
		for(SimpleDisplayObject child: mMyChildren){
			if(child != null) {
				touched = child.onTouchTest(x-child.getX(), y-child.getY(), action);
				if(touched == true){
					//break; todo 
				}
			}
		}
		return touched;
	}

	public void addChild(SimpleDisplayObject child) {
		mMyChildren.add(child);
	}

}
