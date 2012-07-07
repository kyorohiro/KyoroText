package info.kyorohiro.helloworld.logcat.util;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineBuffer;
import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.logcat.KyoroLogcatSetting;
import info.kyorohiro.helloworld.logcat.util.SimpleFilterableLineView;
import info.kyorohiro.helloworld.util.CyclingListInter;
import info.kyorohiro.helloworld.util.LineViewBufferSpec;

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
		mViewer = new FlowingLineView(new LineViewBufferSpecAdapterForFlowingLineBuffer(mInputtedText), mTextSize);
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


		// 以下はリークしてるかも
		mViewer.getLineView().setLineViewBufferSpec(
				new LineViewBufferSpecAdapterForFlowingLineBuffer(mInputtedText.getDuplicatingList()));

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

	//
	// メモリーリークが発生しそうなので、WeakReferencecを利用している。
	public static class LineViewBufferSpecAdapterForFlowingLineBuffer implements LineViewBufferSpec {
		private WeakReference<CyclingListInter<LineViewData>> mBuffer = null;
		public LineViewBufferSpecAdapterForFlowingLineBuffer(CyclingListInter<LineViewData> buffer) {
			mBuffer = new WeakReference<CyclingListInter<LineViewData>>(buffer);
		}

		@Override
		public BreakText getBreakText() {
			return null;
		}

		@Override
		public int getNumOfAdd() {
			CyclingListInter<LineViewData> b = mBuffer.get();
			if(b != null) {
				return b.getNumOfAdd();
			} else  {
				return 0;
			}
		}

		@Override
		public void clearNumOfAdd() {
			CyclingListInter<LineViewData> b = mBuffer.get();
			if(b != null) {
				b.clearNumOfAdd();
			}
		}

		@Override
		public int getNumberOfStockedElement() {
			CyclingListInter<LineViewData> b = mBuffer.get();
			if(b != null) {
				return b.getNumberOfStockedElement();
			} else  {
				return 0;
			}
		}

		@Override
		public LineViewData[] getElements(LineViewData[] ret, int start, int end) {
			CyclingListInter<LineViewData> b = mBuffer.get();
			if(b != null) {
				return b.getElements(ret, start, end);
			}
			return new LineViewData[0];
		}

		@Override
		public LineViewData get(int i) {
			CyclingListInter<LineViewData> b = mBuffer.get();
			if(b != null) {
				return b.get(i);
			}
			return null;
		}
		
	}
}
