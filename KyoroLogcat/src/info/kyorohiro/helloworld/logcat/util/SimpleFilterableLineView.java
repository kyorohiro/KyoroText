package info.kyorohiro.helloworld.logcat.util;

import java.util.regex.Pattern;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineBuffer;
import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.logcat.KyoroLogcatSetting;
import info.kyorohiro.helloworld.logcat.util.SimpleFilterableLineView;

public class SimpleFilterableLineView extends SimpleDisplayObjectContainer {

	private FlowingLineBuffer mInputtedText = null;
	private FlowingLineView mViewer = null;
	private int mTextSize = 16;
	private int mBaseWidth = 400;

	public SimpleFilterableLineView(FlowingLineBuffer lineData, int baseWidth) {
		mInputtedText = lineData;
		if (mInputtedText == null) {
			mInputtedText = new FlowingLineBuffer(3000, 1000, mTextSize);
		}
		mTextSize = mInputtedText.getTextSize();
		mViewer = new FlowingLineView(mInputtedText, mTextSize);
		mBaseWidth = baseWidth;
		addChild(new Layout());
		onAddViewer();
		addChild(mViewer);
	}

	protected void onAddViewer() {
		
	}

	public class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			double width = graphics.getWidth();
			double fontSize = KyoroLogcatSetting.getFontSize();
			fontSize = fontSize*width/(double)mBaseWidth;
//			SimpleFilterableLineView.this.mInputtedText.setWidth(graphics.getWidth()*9/10);
			SimpleFilterableLineView.this.mInputtedText.setWidth((int)(mBaseWidth*9/10));
			SimpleFilterableLineView.this.getLineView().setTextSize((int)(fontSize));
		}
	}

	public FlowingLineBuffer getCyclingStringList() {
		return mInputtedText;
	}

	public LineView getLineView() {
		return mViewer.getLineView();
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
