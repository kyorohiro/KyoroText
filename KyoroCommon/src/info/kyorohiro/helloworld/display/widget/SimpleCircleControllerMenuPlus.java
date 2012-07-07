package info.kyorohiro.helloworld.display.widget;

import java.util.ArrayList;
import java.util.Vector;

import android.graphics.Color;
import android.view.MotionEvent;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;

public class SimpleCircleControllerMenuPlus extends SimpleCircleController {

	private boolean mFocus = false;
	private SelectButton mButton = new SelectButton();
	private SelectMenu mMenu = new SelectMenu();
	private ArrayList<CircleMenuItem> itemList = new ArrayList<CircleMenuItem>();

	public void addCircleMenu(CircleMenuItem item) {
		itemList.add(item);
	}

	public void clearCircleMenu() {
		itemList.clear();
	}

	public SimpleCircleControllerMenuPlus() {
		super();
		addChild(mButton);
		addChild(mMenu);
	}

	public class SelectButton extends SimpleDisplayObject {
		private int mPrevX = 0;
		private int mPrevY = 0;
		private int mCurrentX = 0;
		private int mCurrentY = 0;

		public int getXX() {
			return mCurrentX;
		}

		public int getYY() {
			return mCurrentY;
		}

		@Override
		public boolean includeParentRect() {
			return false;
		}

		@Override
		public void paint(SimpleGraphics graphics) {
			int x = SimpleCircleControllerMenuPlus.this.getCenterX();
			int y = SimpleCircleControllerMenuPlus.this.getCenterY();
			int radius = SimpleCircleControllerMenuPlus.this.getMinRadius();
			graphics.setColor(Color.GREEN);
			graphics.setStrokeWidth(3);
			graphics.drawCircle(x + mCurrentX, y + mCurrentY, radius * 4 / 5);
			graphics.drawCircle(x + mCurrentX, y + mCurrentY, radius * 3 / 5);
		}

		@Override
		public boolean onTouchTest(int x, int y, int action) {
			int radius = SimpleCircleControllerMenuPlus.this.getMinRadius();
			int radiusN = SimpleCircleControllerMenuPlus.this.getMaxRadius() * 3;
			if (action == MotionEvent.ACTION_DOWN) {
				if (mFocus==false &&x * x + y * y < radius * radius) {
					mFocus = true;
					mPrevX = x;
					mPrevY = y;
					mCurrentX = 0;
					mCurrentY = 0;
					return true;
				}
			} else if (action == MotionEvent.ACTION_MOVE) {
				if (mFocus == true && x * x + y * y < radiusN * radiusN) {
					mCurrentX = x - mPrevX;
					mCurrentY = y - mPrevY;
					android.util.Log.v("kiyo","okm x="+x+",y="+y);
					return true;
				} else {
					mFocus = false;
					mCurrentX = 0;
					mCurrentY = 0;
					return false;
				}
			} else if (action == MotionEvent.ACTION_UP) {
				mFocus = false;
				mCurrentX = 0;
				mCurrentY = 0;
			}
			return super.onTouchTest(x, y, action);
		}
		
	}

	public class SelectMenu extends SimpleDisplayObject {
		@Override
		public boolean includeParentRect() {
			return false;
		}

		@Override
		public void paint(SimpleGraphics graphics) {
			int x = SimpleCircleControllerMenuPlus.this.getCenterX();
			int y = SimpleCircleControllerMenuPlus.this.getCenterY();
			int radius = SimpleCircleControllerMenuPlus.this.getMaxRadius();
			int radiusN = SimpleCircleControllerMenuPlus.this.getMaxRadius() * 3;
			if (mFocus) {
				graphics.setColor(Color.GREEN);
				graphics.setStrokeWidth(1);
				graphics.drawCircle(x, y, radiusN * 4 / 5);
				graphics.drawCircle(x, y, radiusN * 3 / 5);

				int len = 3;//itemList.size();
				int div = 4;//(len + 1);
				double angle = Math.PI/(2*div);

				double p = Math.atan2(-mButton.getYY(), mButton.getXX());
				int curDegree = (int) Math.toDegrees(p)-90;
				if(curDegree <=0){
					curDegree = 1;
				}
				int base = curDegree/(90/8);
				int selected =(base+1)/2;
				android.util.Log.v("kiyo","base="+base+","+selected+","+curDegree+","+ Math.toDegrees(p));

				for (int i = 0; i < div*2; i++) {
					double a = angle*i+Math.PI/2;
					if(i==selected){
						graphics.setColor(Color.YELLOW);
					} else {
						graphics.setColor(Color.GREEN);						
					}
					
					graphics.drawLine(
							(int) (radius * Math.cos(a)),
							(int) (radius * -1 * Math.sin(a)),
							(int) (radiusN * Math.cos(a)),
							(int) (radiusN * -1 * Math.sin(a)));
				}


				for (int i = 0; i < len; i++) {
					if(i==selected){
						graphics.setColor(Color.YELLOW);
					} else {
						graphics.setColor(Color.GREEN);						
					}
					double a = angle*i+Math.PI/2;
					graphics.drawText(
							itemList.get(i).getTitle(),
							(int) (radiusN * Math.cos(a)),
							(int) (radiusN * -1 * Math.sin(a)));
				}

				if(selected<itemList.size()){
					itemList.get(selected).selected(selected);
				}
			}
		}
	}

	public interface CircleMenuItem {
		public int getid();
		public String getTitle();
		public boolean selected(int id);
	}
}
