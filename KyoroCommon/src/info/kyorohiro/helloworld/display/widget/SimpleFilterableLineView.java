package info.kyorohiro.helloworld.display.widget;

import java.util.regex.Pattern;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineView;

public class SimpleFilterableLineView extends SimpleDisplayObjectContainer {

	private FlowingLineData mInputtedText = null;
	private FlowingLineView mViewer = null;
	private int mTextSize = 14;

	public SimpleFilterableLineView(FlowingLineData lineData) {
		mInputtedText = lineData;
		if (mInputtedText == null) {
			mInputtedText = new FlowingLineData(3000, 1000, mTextSize);
		}
		mViewer = new FlowingLineView(mInputtedText);
		addChild(new Layout());
		addChild(mViewer);
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			SimpleFilterableLineView.this.mInputtedText.setWidth(graphics.getWidth()*9/10);
		}
	}
	public FlowingLineData getCyclingStringList() {
		return mInputtedText;
	}

	public FlowingLineData getCyclingFlowingLineData() {
		return mInputtedText;
	}

	public void setPosition(int position) {
		mViewer.setPosition(position);
	}

	public int getPosition() {
		return mViewer.getPosition();
	}

	public void startFilter(Pattern nextFilter) {
		if (nextFilter == null || mInputtedText == null) {
			return;
		}
		Pattern currentFilter = mInputtedText.getFilterText();

		if (!equalFilter(currentFilter, nextFilter)) {
			mInputtedText.stop();
			mInputtedText.setFileterText(nextFilter);
			mInputtedText.start();
		}
		if (mInputtedText.taskIsAlive()) {
			mViewer.setCyclingList(mInputtedText.getDuplicatingList());
		} else {
			mViewer.setCyclingList(mInputtedText.getDuplicatingList());
		}
	}

	private boolean equalFilter(Pattern currentFilter, Pattern nextFilter) {
		if (nextFilter == null || nextFilter == null) {
			return true;
		}

		String currentPattern = "";
		String nextPattern = "" + nextFilter.pattern();
		if (currentFilter != null) {
			currentPattern = "" + currentFilter.pattern();
		}

		if (currentPattern.equals(nextPattern)) {
			return true;
		} else {
			return false;
		}
	}

}