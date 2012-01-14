package info.kyorohiro.helloworld.display.widget;

import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.util.CyclingList;

public class SimpleLineChart extends SimpleDisplayObjectContainer {

	private Line mLine = null;
	private ChartData mData = null;
	private int mPosition = 0;
	private int mUnitW = 20;

	public SimpleLineChart() {
		mLine = new Line();
		mData = new ChartData(1000);
		addChild(mLine);
	}

	public void addCharDatam(CharDatam datam){
		mData.add(datam);
	}

	public ChartData getChartData() {
		return mData;
	}

	class Line extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			int scale = 50;
			int length = mData.getNumberOfStockedElement();
			if(length<2){
				return;
			}

			int startY = graphics.getHeight()/2 + mData.get(length-1).getValue()/scale;
			int startX = graphics.getWidth()/2;


			setPoint(10, startY);

			graphics.drawBackGround(Color.parseColor("#FFFFFF"));
			graphics.setColor(Color.parseColor("#FF0000"));
			graphics.setStrokeWidth(1);


			int pre = 0;
			int cur = 0;
			int max = length-1;
			for(int i=length-1;i>1;i--){
				pre = -1*mData.get(i-1).getValue();
				cur = -1*mData.get(i).getValue();
				pre /=scale;
				cur /=scale;
				graphics.drawLine(startX-(max-i+1)*mUnitW, pre, startX-(max-i)*mUnitW, cur);
			}
		}

	}
	
	static public class ChartData extends CyclingList<CharDatam> {
		public ChartData(int listSize) {
			super(listSize);
		}
	}
	

	static public class CharDatam {
		private String mTag = "";
		private int mValue = 0;
		private long mTime = 0;

		public CharDatam(String tag, int value) {
			mValue = value;
			mTag = tag;
			mTime = System.currentTimeMillis();
		}

		public int getValue() {
			return mValue;
		}

		public long getTime() {
			return mTime;
		}

		public String getTag() {
			return mTag;
		}

	}
}
