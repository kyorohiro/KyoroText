package info.kyorohiro.helloworld.textviewer.viewer;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Environment;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.FlowingLineDatam;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndMove;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndZoom;
import info.kyorohiro.helloworld.display.widget.lineview.ScrollBar;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.R;
import info.kyorohiro.helloworld.util.BigLineData;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class TextViewer extends SimpleDisplayObjectContainer {
	//	public static int COLOR_BG = Color.parseColor("#FF101030");
	public static int COLOR_BG = Color.parseColor("#FF808080");
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
		mBuffer.add(new FlowingLineDatam("Please open file", Color.CYAN,
				FlowingLineDatam.INCLUDE_END_OF_LINE));
		mBuffer.add(new FlowingLineDatam("Current charset is "+mCharset, Color.YELLOW,
				FlowingLineDatam.INCLUDE_END_OF_LINE));
		mBuffer.add(new FlowingLineDatam("..", Color.RED,
				FlowingLineDatam.INCLUDE_END_OF_LINE));
		mBuffer.add(new FlowingLineDatam("Sorry, this application is pre-alpha version ", Color.RED,
				FlowingLineDatam.INCLUDE_END_OF_LINE));
		mBuffer.add(new FlowingLineDatam("Testing and Developing.. now", Color.RED,
				FlowingLineDatam.INCLUDE_END_OF_LINE));
		mBuffer.add(new FlowingLineDatam("Please mail kyorohiro.android@gmail.com, ", Color.RED,
				FlowingLineDatam.EXCLUDE_END_OF_LINE));
		mBuffer.add(new FlowingLineDatam("If you have particular questions or comments, ", Color.RED,
				FlowingLineDatam.EXCLUDE_END_OF_LINE));
		mBuffer.add(new FlowingLineDatam("please don't hesitate to contact me. Thank you. ", Color.RED,
				FlowingLineDatam.INCLUDE_END_OF_LINE));
		
		mLineView = new LineView(mBuffer, textSize,10*BigLineData.FILE_LIME);
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

	private SimpleImage mBGImage = null;
	private SimpleImage mScrollImage = null;
	public void start() {
		Resources res = KyoroApplication.getKyoroApplication().getResources();
		InputStream is =
//			res.openRawResource(R.drawable.ic_launcher);
//			res.openRawResource(R.drawable.tex2res2);
		    res.openRawResource(R.drawable.tex2res4);
		mBGImage = new SimpleImage(is);
		mLineView.setBGImage(mBGImage);		
		InputStream isScroll =
//			res.openRawResource(R.drawable.ic_launcher);
//			res.openRawResource(R.drawable.tex2res2);
		    res.openRawResource(R.drawable.tex2res2);
		mScrollImage = new SimpleImage(isScroll);
		mScrollBar.setBGImage(mScrollImage);		
	}
	public void stop() {
		mLineView.setBGImage(null);
		if(mBGImage.getImage().isRecycled()){
			mBGImage.getImage().recycle();
		}
	}

	public void setCharset(String charset) {
		if (charset == null) {
			mCharset = "utf8";
		} else {
			mCharset = charset;
		}
	}

	public void readFile(File file) {
		if(file == null) {
			KyoroApplication.showMessage("file can not read null file");
			return;
		}
		if(!file.canRead() || !file.exists()|| !file.isFile()){
			KyoroApplication.showMessage("file can not read " + file.toString());
			return;
		}
		mBuffer = new ColorFilteredBuffer(10*BigLineData.FILE_LIME, mTextSize, mWidth, file, mCharset);
		mLineView.setCyclingList(mBuffer);
		mLineView.setPositionX(0);
		mLineView.setPositionY(10);
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
				min = min/2;
				if(min<mCircleController.getWidth()){
					mCircleController.setRadius(min/2);
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
