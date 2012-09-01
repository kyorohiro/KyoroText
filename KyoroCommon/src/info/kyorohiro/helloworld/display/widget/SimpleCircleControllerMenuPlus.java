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
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private CircleMenuItem mListener = null;

	public void setCircleMenuItem(CircleMenuItem listener) {
		mListener = listener;
	}

	@Override
	public void paint(SimpleGraphics graphics) {
		super.paint(graphics);
	}

	public void addCircleMenu(int id, String title) {
		Item i = new Item();
		i.id = id;
		i.title = title;
		itemList.add(i);
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
			x -= getCenterX();
			y -= getCenterY();
			int radius = SimpleCircleControllerMenuPlus.this.getMinRadius();
			int radiusN = SimpleCircleControllerMenuPlus.this.getMaxRadius() * 3;
			if (action == MotionEvent.ACTION_DOWN) {
				if (mFocus == false && x * x + y * y < radius * radius) {
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
					// android.util.Log.v("kiyo","okm x="+x+",y="+y);
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
			int radiusN = SimpleCircleControllerMenuPlus.this.getMaxRadius() * 3 * 3 / 4;
			if (mFocus) {
				graphics.setColor(Color.GREEN);
				graphics.setStrokeWidth(1);
				graphics.drawCircle(x, y, radiusN * 4 / 5);
				graphics.drawCircle(x, y, radiusN * 3 / 5);

				int div = 12;
				double angle = Math.PI * 2 / div;

				// Œ»ÝˆÊ’u
				double p = 0;
				p = Math.atan2(-mButton.getYY(), mButton.getXX());
				int curDegree = (int) Math.toDegrees(p);// - 90;

				// 90“x‚©‚ç€–Ú’Ç‰Á
				curDegree -= 90 - (360 / div / 2);
				//
				int base = ((360 * 2 + curDegree) % 360) / (360 / div);
				int selected = base;
				// android.util.Log.v("kiyo","po="+selected+","+(360 /
				// div)+","+curDegree);

				for (int i = 0; i < div; i++) {
					double a = angle * i + Math.PI / 2;
					double extra = 0;
					extra = Math.PI / 36 / 2;
					if (i - 1 == selected) {
						a += extra;
					} else if (i + 1 == selected) {
						a -= extra;
					}
					if (i == selected) {
						graphics.setColor(Color.YELLOW);
					} else {
						graphics.setColor(Color.GREEN);
					}

					graphics.drawLine(
							(int) (radius * Math.cos(a))+ getCenterX(),
							(int) (radius * -1 * Math.sin(a))+ getCenterY(),
							(int) (radiusN * Math.cos(a))+ getCenterX(),
							(int) (radiusN * -1 * Math.sin(a))+ getCenterY());
					if (i < itemList.size()) {
						graphics.drawText(itemList.get(i).title,
								(int) (radiusN * Math.cos(a)) + getCenterX(),
								(int) (radiusN * -1 * Math.sin(a))
										+ getCenterY());
					}

				}

				int radiusM = SimpleCircleControllerMenuPlus.this
						.getMaxRadius();
				if (mButton.getXX() * mButton.getXX() + mButton.getYY()
						* mButton.getYY() > radiusM * radiusM||(mButton.getXX()>=getWidth()+getMinRadius()/2&&mButton.getYY()>=getHeight()+getMinRadius()/2)) {
					if (selected < itemList.size()) {
						if (mListener.selected(itemList.get(selected).id,
								itemList.get(selected).title)) {
							mFocus = false;
						}
					} else if (selected >= itemList.size() * 2) {
						if(isMinimized()) {
							maxmize();
							mFocus = false;
						} else {
							minmize();
							mFocus = false;
						}
					}
				}
			}
		}
	}

	public interface CircleMenuItem {
		public boolean selected(int id, String title);
	}

	public static class Item {
		public String title;
		public int id;
	}
}
