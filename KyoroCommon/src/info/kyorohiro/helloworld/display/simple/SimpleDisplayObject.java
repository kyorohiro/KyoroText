package info.kyorohiro.helloworld.display.simple;

public abstract class SimpleDisplayObject implements SimpleDisplayObjectSpec {
	private int mX=0;
	private int mY=0;
	private int mW=0;
	private int mH=0;
	private Object mParent = null;

	public static SimpleStage getStage(SimpleDisplayObjectSpec object) {
		SimpleDisplayObjectSpec t = object;
		while(true) {
			Object o = t.getParent();
			if(o != null&& o instanceof SimpleDisplayObjectSpec ) {
				t = (SimpleDisplayObjectSpec)o;
			}
			else if(o != null && o instanceof SimpleStage) {
				return (SimpleStage) o;
			}
			else {
				break;
			}
		}
		return null;
	}

	public void dispose() {
		
	};

	public int getX() {
		return mX;
	}
	
	public int getY() {
		return mY;
	}
	
	public Object getParent() {
		return mParent;
	}

	public void setParent(Object parent) {
		mParent = parent;
	}

	public void setPoint(int x, int y){
		mX = x;
		mY = y;
	}

	public void setRect(int w, int h) {
		mW = w;
		mH = h;
	}

	public int getWidth() {
		return mW;
	}

	public int getHeight() {
		return mH;
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
