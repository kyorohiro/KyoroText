package info.kyorohiro.helloworld.display.widget;

import java.util.ArrayList;

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

		public int getX() {
			return mCurrentX;
		}

		public int getY() {
			return mCurrentY;
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
				if (x * x + y * y < radius * radius) {
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

				int len = itemList.size();
				int div = 4;//(len + 1);
				double angle = Math.PI/(2*div);
				for (int i = 0; i < div*2; i++) {
					double a = angle*i+Math.PI/2;
					graphics.drawLine(
							(int) (radius * Math.cos(a)),
							(int) (radius * -1 * Math.sin(a)),
							(int) (radiusN * Math.cos(a)),
							(int) (radiusN * -1 * Math.sin(a)));
				}

				for (int i = 0; i < len; i++) {
					double a = angle*i+Math.PI/2;
					graphics.drawText(
							itemList.get(i).getTitle(),
							(int) (radiusN * Math.cos(a)),
							(int) (radiusN * -1 * Math.sin(a)));
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
