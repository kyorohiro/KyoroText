package info.kyorohiro.helloworld.display.simple;

public abstract class SimpleDisplayObject implements SimpleDisplayObjectSpec {
	private int mX=0;
	private int mY=0;

	public int getX() {
		return mX;
	}
	
	public int getY() {
		return mY;
	}

	public void setPoint(int x, int y){
		mX = x;
		mY = y;
	}
	
	public int getWidth() {
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public abstract void paint(SimpleGraphics graphics);

	/**
	 * todo rewrite and look at touch event framework  
	 * @param x
	 * @param y
	 * @param action
	 * @return touch point is in a DisplayObject then true, else false. 
	 */
	@Deprecated
	public boolean onTouchTest(int x, int y, int action) {
		return false;
	}

	/**
	 * @return touch point is in a DisplayObject then true, else false. 
	 */
	public boolean onKeyUp(int keycode) {
		return false;
	}
	/**
	 * @return touch point is in a DisplayObject then true, else false. 
	 */
	public boolean onKeyDown(int keycode) {
		return false;
	}
}
