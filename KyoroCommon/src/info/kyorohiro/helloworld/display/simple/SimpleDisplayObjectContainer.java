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
				SimpleGraphics childGraphics = graphics.getChildGraphics(
						graphics,
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

	@Override
	public int getWidth() {
		int[] bound = getBound();
		return bound[1]-bound[0];
	}

	@Override
	public int getHeight() {
		int[] bound = getBound();
		return bound[3]-bound[2];
	}

	private int[] getBound(){
		int[] bound = new int[]{-1,-1,-1,-1};
		int[] tmp = new int[4];

		int[] pa1 = {0,2};  
		int[] pa2 = {1,3};  

		for(SimpleDisplayObject child: mMyChildren){
			if(child != null) {
				tmp[0] = child.getX();
				tmp[1] = child.getX() + child.getWidth();
				tmp[2] = child.getY();
				tmp[3] = child.getY() + child.getHeight();

				for(int i:pa1) {
					if(bound[i]< 0 || tmp[i] < bound[i]){
						bound[i] = tmp[i];
					}
				}
				
				for(int i:pa2) {
					if(bound[i]< 0 || tmp[i] > bound[i]){
						bound[i] = tmp[i];
					}
				}
				
			}
		}
		return bound;
	}
}
