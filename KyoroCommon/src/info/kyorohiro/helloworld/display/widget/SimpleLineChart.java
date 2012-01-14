package info.kyorohiro.helloworld.display.widget;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;

public class SimpleLineChart extends SimpleDisplayObjectContainer{

	private Line line = null;
	public SimpleLineChart() {
		line = new Line();
		addChild(line);
	}

	static class Line extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.drawBackGround(Color.parseColor("#FF0000"));
		}

	}
}
