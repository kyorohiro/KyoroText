package info.kyorohiro.helloworld.logcat.util;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

import android.view.ViewDebug.ExportedProperty;
import info.kyorohiro.helloworld.pfdep.android.adapter.SimpleFontForAndroid;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.simple.sample.MyBreakText;
import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineBuffer;
import info.kyorohiro.helloworld.display.widget.flowinglineview.FlowingLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndMoveActionForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndZoomForLineView;
import info.kyorohiro.helloworld.logcat.KyoroLogcatSetting;
import info.kyorohiro.helloworld.logcat.util.SimpleFilterableLineView;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class SimpleFilterableLineView extends SimpleDisplayObjectContainer {

	private FlowingLineBuffer mInputtedText = null;
	private FlowingLineView mViewer = null;
	private int mTextSize = 16;
	private int mBaseWidth = 400;
	private LineViewBufferSpecAdapterForFlowingLineBuffer currentLVBSAFFLB = null;

	public SimpleFilterableLineView(FlowingLineBuffer lineData, int baseWidth) {
		mInputtedText = lineData;
		mBaseWidth = baseWidth;
		if (mInputtedText == null) {
			mInputtedText = new FlowingLineBuffer(3000, 1000, mTextSize);
		}
		
		mTextSize = mInputtedText.getTextSize();
		mViewer = new FlowingLineView(currentLVBSAFFLB=new LineViewBufferSpecAdapterForFlowingLineBuffer(mInputtedText, mTextSize, mBaseWidth*9/10), 1000);//mTextSize);
		mViewer.getLineView().setMergine(mBaseWidth/20);
		addChild(new TouchAndMoveActionForLineView(this.getLineView()));
		addChild(new TouchAndZoomForLineView(this.getLineView()));	
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
			SimpleFilterableLineView.this.getLineView().setMergine((int)(width/20));

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
		//
		// todo refactaring
		KyoroLogcatSetting.setCurrentFind(nextFilter.pattern());

		Pattern currentFilter = mInputtedText.getFilterText();

		if (!equalFilter(currentFilter, nextFilter)) {
			mInputtedText.stop();
			mInputtedText.setFileterText(nextFilter);
			mInputtedText.start();
		}


		// 以下はリークしてるかも
		mViewer.getLineView().setLineViewBufferSpec(
				currentLVBSAFFLB=new LineViewBufferSpecAdapterForFlowingLineBuffer(mInputtedText.getDuplicatingList(), 
						 mInputtedText.getTextSize(), mBaseWidth*9/10));
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

	@Deprecated
	public void setFontSize(float size) {
		currentLVBSAFFLB.setFontSize(size);
	}
	//
	// メモリーリークが発生しそうなので、WeakReferencecを利用している。
	//
	// MyBreakText を使うことをやめる。
	// そうすると、すっきりする部分がある。
	// とりあえず、setFontxxxはなくセル。
	@Deprecated
	public static class LineViewBufferSpecAdapterForFlowingLineBuffer implements LineViewBufferSpec {
//		private WeakReference<CyclingListInter<KyoroString>> mBuffer = null;
		private WeakReference<CyclingListInter<KyoroString>> mBuffer = null;
		private MyBreakText mBreakText = new MyBreakText(new SimpleFontForAndroid());

		public LineViewBufferSpecAdapterForFlowingLineBuffer(CyclingListInter<KyoroString> buffer, int textsize, int width) {
			mBuffer = new WeakReference<CyclingListInter<KyoroString>>(buffer);
			mBreakText.setBufferWidth(width);
			mBreakText.getSimpleFont().setFontSize(textsize);
		}

		public void setFontSize(float size){
			mBreakText.getSimpleFont().setFontSize(size);
		}

		@Override
		public BreakText getBreakText() {
			return mBreakText;
		}

		@Override
		public int getNumOfAdd() {
			CyclingListInter<KyoroString> b = mBuffer.get();
			if(b != null) {
				return b.getNumOfAdd();
			} else  {
				return 0;
			}
		}

		@Override
		public void clearNumOfAdd() {
			CyclingListInter<KyoroString> b = mBuffer.get();
			if(b != null) {
				b.clearNumOfAdd();
			}
		}

		@Override
		public int getNumberOfStockedElement() {
			CyclingListInter<KyoroString> b = mBuffer.get();
			if(b != null) {
				return b.getNumberOfStockedElement();
			} else  {
				return 0;
			}
		}

		@Override
		public KyoroString get(int i) {
			CyclingListInter<KyoroString> b = mBuffer.get();
			if(b != null) {
				return b.get(i);
			}
			return null;
		}

		@Override
		public int  getMaxOfStackedElement() {
			CyclingListInter<KyoroString> b = mBuffer.get();
			if(b != null) {
				return b.getMaxOfStackedElement();
			}
			else {
				return -1;
			}
		}

		@Override
		public void isSync(boolean isSync) {
			// 常にSYNC
		}
		@Override
		public boolean isSync() {
			return true;
		}
		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLoading() {
			return true;
		}
		
	}
}
