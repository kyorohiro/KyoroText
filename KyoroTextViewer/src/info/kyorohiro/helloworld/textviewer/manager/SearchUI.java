package info.kyorohiro.helloworld.textviewer.manager;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;

public class SearchUI extends SimpleDisplayObjectContainer {

	private boolean mIsVisible = true;

	public SearchUI(int width, int height) {
		addChild(new BG());
		//addChild(new Next());
		//addChild(new Prev());
		setRect(width, height);
	}
/*
	public void isVisible(boolean visible) {
		mIsVisible = visible;
	}

	public boolean isVisible() {
		return mIsVisible;
	}
*/
	private class BG extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			android.util.Log.v("kiyo","----BG#paint()----");
//			graphics.clipRect(0, 0, getWidth(), getHeight());
			graphics.setColor(Color.RED);
			graphics.drawCircle(0, 0, 100);
			graphics.setStyle(SimpleGraphics.STYLE_LINE);
			graphics.setStrokeWidth(1);
			graphics.startPath();
			graphics.moveTo(0, 0);
			graphics.lineTo(SearchUI.this.getWidth(), 0);
			graphics.lineTo(SearchUI.this.getWidth(), SearchUI.this.getHeight());
			graphics.lineTo(0, SearchUI.this.getHeight());
			graphics.lineTo(0, 0);
			graphics.endPath();
//			graphics.clipRect(-1, -1, -1, -1);
		}
	}

	private class Next extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
		}
	}

	private class Prev extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
		}
	}

}
