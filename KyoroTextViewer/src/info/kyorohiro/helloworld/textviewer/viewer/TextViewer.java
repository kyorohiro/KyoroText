package info.kyorohiro.helloworld.textviewer.viewer;

import java.io.File;
import java.util.regex.Matcher;

import android.graphics.Color;
import android.os.Environment;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndMove;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndZoom;
import info.kyorohiro.helloworld.display.widget.lineview.ScrollBar;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.util.BigLineData;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class TextViewer extends SimpleDisplayObjectContainer {
	public static int COLOR_BG = Color.parseColor("#FF101030");
	public static int COLOR_FONT1 = Color.parseColor("#ff80c9f4");
	public static int COLOR_FONT2 = Color.parseColor("#fff480c9");

	private CyclingList<FlowingLineDatam> mBuffer = null;
	private LineView mLineView = null;
	private int mWidth = 0;
	private int mTextSize = 0;
	private String mCharset = "utf8";
	private SimpleCircleController mCircleController = null;
	private ScrollBar mScrollBar = null;

	public TextViewer(int textSize, int screenWidth) {
		mWidth = screenWidth;
		mTextSize = textSize;
		mCharset = KyoroSetting.getCurrentCharset();
		File testDir = Environment.getExternalStorageDirectory();
		File f = new File(testDir, "a.txt");

		mBuffer = new CyclingList<FlowingLineDatam>(100);// new
															// TextViewerBuffer(1000,
															// textSize,
															// screenWidth, f);
		mBuffer.add(new FlowingLineDatam("please open file\n current charset is "+mCharset, Color.CYAN,
				FlowingLineDatam.INCLUDE_END_OF_LINE));
		mLineView = new LineView(mBuffer, textSize);
		mScrollBar = new ScrollBar(mLineView);
		addChild(mLineView);
		addChild(new MyTouchAndMove(mLineView));
		addChild(new MyTouchAndZoom(mLineView));
		addChild(mCircleController = new SimpleCircleController());
		addChild(new Layout());
		addChild(mScrollBar);
		mCircleController.setEventListener(new MyCircleControllerEvent());
		mLineView.isTail(false);
		mLineView.setBgColor(COLOR_BG);
	}

	public void setCharset(String charset) {
		if (charset == null) {
			mCharset = "utf8";
		} else {
			mCharset = charset;
		}
	}

	public void readFile(File file) {
		mBuffer = new ColorFilteredBuffer(10*BigLineData.FILE_LIME, mTextSize, mWidth, file, mCharset);
		mLineView.setCyclingList(mBuffer);
		((TextViewerBuffer) mBuffer).startReadForward(-1);
		KyoroApplication.showMessage("charset="+mCharset);
	}

	@Override
	public void setRect(int w, int h) {
		super.setRect(w, h);
		mLineView.setRect(w, h);
	}

	public static class ColorFilteredBuffer extends TextViewerBuffer {
		public ColorFilteredBuffer(int listSize, int textSize, int screenWidth,
				File path, String charset) {
			super(listSize, textSize, screenWidth, path, charset);
		}

		@Override
		public synchronized void head(FlowingLineDatam element) {
			super.head(element);
			element.setColor(COLOR_FONT2);
		}
		@Override
		public synchronized void add(FlowingLineDatam element) {
			super.add(element);
			element.setColor(COLOR_FONT1);
		}
	}
	
	public LineView getLineView() {
		return mLineView;
	}

	private class Layout extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			int x = graphics.getWidth() - mCircleController.getWidth() / 2;
			int y = graphics.getHeight() - mCircleController.getHeight() / 2;
			TextViewer.this.mCircleController.setPoint(x, y);
			{
				int min = graphics.getWidth();
				if(min>graphics.getHeight()){
					min = graphics.getHeight();
				}
				min = min/4;
				if(min<mCircleController.getWidth()){
					mCircleController.setRect(min, min);
				}
			}
			// todo unefficient
			paintScroll(graphics);			
		}
		public void paintScroll(SimpleGraphics graphics) {
			CyclingListInter<?> list = TextViewer.this.mLineView.getCyclingList();
			int size = list.getNumberOfStockedElement(); 
			mScrollBar.setStatus(
					TextViewer.this.mLineView.getShowingTextStartPosition(),
					TextViewer.this.mLineView.getShowingTextEndPosition(), size);
			TextViewer.this.mLineView.setRect(graphics.getWidth(), graphics.getHeight());
		}
	}

	private class MyCircleControllerEvent implements SimpleCircleController.CircleControllerAction {
		public void moveCircle(int action, int degree, int rateDegree) {
			if (action == CircleControllerAction.ACTION_MOVE) {
				getLineView().setPositionY(getLineView().getPositionY() + rateDegree*2);
			}
		}

		public void upButton(int action) {
			getLineView().setPositionY(getLineView().getPositionY() + 5);
		}

		public void downButton(int action) {
			getLineView().setPositionY(getLineView().getPositionY() - 5);
		}
	}
}
