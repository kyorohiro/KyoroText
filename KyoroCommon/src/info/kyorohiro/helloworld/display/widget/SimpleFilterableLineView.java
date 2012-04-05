package info.kyorohiro.helloworld.display.widget;

import java.util.regex.Pattern;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineData;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;

public class SimpleFilterableLineView extends SimpleDisplayObjectContainer {

	private FlowingLineData mInputtedText = null;
	private FlowingLineView mViewer = null;
	private int mTextSize = 16;

	public SimpleFilterableLineView(FlowingLineData lineData) {
		mInputtedText = lineData;
		if (mInputtedText == null) {
			mInputtedText = new FlowingLineData(3000, 1000, mTextSize);
		}
		mTextSize = mInputtedText.getTextSize();
		mViewer = new FlowingLineView(mInputtedText, mTextSize);
		
		addChild(new Layout());
		onAddViewer();
		addChild(mViewer);
	}

	protected void onAddViewer() {
		
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

	public LineView getLineView() {
		return mViewer.getLineView();
	}

/*
	public void setScale(float scale) {
		mViewer.setScale(scale);
	}

	public float getScale() {
		return mViewer.getScale();
	}


	public int getTextSize() {
		return mViewer.getTextSize();
	}

	public void setTextSize(int textSize) {
		mViewer.setTextSize(textSize);
	}

	
	public void setPositionY(int position) {
		mViewer.setPositionY(position);
	}

	public void setPositionX(int x) {
		mViewer.setPositionX(x);
	}

	public int getPositionX() {
		return mViewer.getPositionX();
	}
	public int getPositionY() {
		return mViewer.getPositionY();
	}
	*/

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
			mViewer.getLineView().setCyclingList(mInputtedText.getDuplicatingList());
		} else {
			mViewer.getLineView().setCyclingList(mInputtedText.getDuplicatingList());
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
