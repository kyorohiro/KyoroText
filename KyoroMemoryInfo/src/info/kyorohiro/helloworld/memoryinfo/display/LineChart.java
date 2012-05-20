package info.kyorohiro.helloworld.memoryinfo.display;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;

public class LineChart extends SimpleDisplayObjectContainer {
	private ChartData mCharData = null;
	private int mWidth = 100;
	private int mHeight = 100;
	private int mPointX = 0;
	private int mPointY = 0;
	private int mStartY = 0;
	private int mEndY = 24000;
	private int mStartX = 0;
	private int mEndX = 10;
	private int mIntervalX = 10;
	private String mLabel = "--";

	public LineChart(ChartData data, int w, int h) {
		mCharData = data;
		mWidth = w;
		mHeight = h;
		Chart c = new Chart();
		Information info = new Information();

		c.setRect(w, h);
		info.setPoint(0, h);
		info.setRect(w, h);
		addChild(c);
		addChild(info);
	}

	public void setLabel(String label) {
		mLabel = label;
	}

	public class Information extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.setColor(Color.parseColor("#AAAAAAFF"));
			graphics.setStrokeWidth(2);
			graphics.setStyle(SimpleGraphics.STYLE_FILL);

			// draw bg
			graphics.startPath();
			graphics.moveTo(0, 0);
			graphics.lineTo(mWidth, 0);
			graphics.lineTo(mWidth, mHeight);
			graphics.lineTo(0, mHeight);
			graphics.lineTo(0, 0);
			graphics.endPath();

			String[] text = {
					"/dalvikPrivateDirty"    +"\n",
					"/dalvikPss"             +"\n",
					"/dalvikSharedDirty"     +"\n",
					"/nativePrivateDirty"    +"\n",
					"/nativePss"             +"\n",
					"/nativeSharedDirty"     +"\n",
					"/otherPrivateDirty"     +"\n",
					"/otherPss"              +"\n",
					"/totalPrivateDirty"     +"\n",
					"/totalPss"              +"\n",
					"/totalSharedDirty"      +"\n",
			};

			int h=12;
			for(String s:text) {
				h += 12;
				graphics.drawText(s, 10, h);
			}
		}
	}
	public class Chart extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			graphics.setColor(Color.parseColor("#AA0000FF"));
			graphics.setStrokeWidth(2);
			graphics.setStyle(SimpleGraphics.STYLE_FILL);

			// draw bg
			graphics.startPath();
			graphics.moveTo(0, 0);
			graphics.lineTo(mWidth, 0);
			graphics.lineTo(mWidth, mHeight);
			graphics.lineTo(0, mHeight);
			graphics.lineTo(0, 0);
			graphics.endPath();

			// draw label
			graphics.setColor(Color.BLACK);
			graphics.setTextSize(12);
			graphics.drawText("" + mLabel, 12, 24);

			// draw bg
			graphics.setStyle(SimpleGraphics.STYLE_STROKE);
			graphics.startPath();
			int[] data = { 100, 101, 150, 130, 120 };
			graphics.moveTo(0, getHeight() - data[0]);
			for (int i = 1; i < data.length; i++) {
				graphics.lineTo(mIntervalX * i, getHeight() - data[i]);
			}
			graphics.endPath();
		}
	}
}
