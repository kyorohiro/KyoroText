package info.kyorohiro.helloworld.display.widget;

import java.util.regex.Pattern;
import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class FlowingLineViewer extends SimpleDisplayObjectContainer {

	private CyclingFlowingLineData mInputtedText = null;
	private LineViewer mViewer = null;
	private int mTextSize = 14;

	public FlowingLineViewer(CyclingFlowingLineData lineData) {
		mInputtedText = lineData;
		if (mInputtedText == null) {
			mInputtedText = new CyclingFlowingLineData(3000, 1000, mTextSize);
		}
		mViewer = new LineViewer(mInputtedText);
		addChild(mViewer);
		addChild(new Layout());
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			FlowingLineViewer.this.mInputtedText.setWidth(graphics.getWidth()*9/10);
		}
	}
	public CyclingFlowingLineData getCyclingStringList() {
		return mInputtedText;
	}

	public CyclingFlowingLineData getCyclingFlowingLineData() {
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

	/**
	 * 			FlowingLineViewer.this.mInputtedText.setWidth(mWidth - margine);
			CyclingListInter<FlowingLineData> showingText = null;
			if (mInputtedText.taskIsAlive()) {
				showingText = mInputtedText.getDuplicatingList();
			} else {
				showingText = mInputtedText;
			}

	 */
}
