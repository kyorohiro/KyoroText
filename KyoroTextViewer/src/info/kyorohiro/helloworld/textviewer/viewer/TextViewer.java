package info.kyorohiro.helloworld.textviewer.viewer;

import java.io.File;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleImage;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController;
import info.kyorohiro.helloworld.display.widget.SimpleCircleController.CircleControllerAction;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndMoveActionForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.MyTouchAndZoomForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.ScrollBar;
import info.kyorohiro.helloworld.io.BigLineData;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;
import info.kyorohiro.helloworld.textviewer.R;
import info.kyorohiro.helloworld.util.CyclingList;
import info.kyorohiro.helloworld.util.CyclingListInter;

public class TextViewer extends SimpleDisplayObjectContainer {
	public static int COLOR_BG = Color.parseColor("#FF000000");
	public static int COLOR_FONT1 = Color.parseColor("#ff80c9f4");
	public static int COLOR_FONT2 = Color.parseColor("#fff480c9");

	private String mCurrentCharset = "utf8";

	private CyclingListInter<LineViewData> mBuffer = null;
	private LineView mLineView = null;
	private int mBufferWidth = 0;
	private SimpleCircleController mCircleController = null;
	private ScrollBar mScrollBar = null;
	private SimpleImage mBGImage = null;
	private SimpleImage mScrollImage = null;
	private int mCurrentFontSize = KyoroSetting.CURRENT_FONT_SIZE_DEFAULT;
	private String mCurrentPath = "";
	private int mMergine = 0;

	public TextViewer(int textSize, int width, int mergine) {
		mCurrentFontSize = textSize;
		mCurrentCharset = KyoroSetting.getCurrentCharset();
		mBuffer = getStartupMessageBuffer();
		mBufferWidth = width-mergine*2;
		mMergine = mergine;

		mLineView = new LineView(mBuffer, textSize,200);
		mLineView.isTail(false);
		mLineView.setBgColor(COLOR_BG);

		mScrollBar = new ScrollBar(mLineView);
		addChild(mLineView);
		addChild(new TouchAndMoveActionForLineView(mLineView));
		addChild(new MyTouchAndZoomForLineView(mLineView));
		addChild(mCircleController = new SimpleCircleController());
		addChild(new LayoutManager());
		addChild(mScrollBar);
		mCircleController.setEventListener(new MyCircleControllerEvent());
	}

	@Override
	public void start() {
		Resources res = KyoroApplication.getKyoroApplication().getResources();
		InputStream is = res.openRawResource(R.drawable.tex2res4);
		mBGImage = new SimpleImage(is);
		mLineView.setBGImage(mBGImage);		
		InputStream isScroll = res.openRawResource(R.drawable.tex2res2);
		mScrollImage = new SimpleImage(isScroll);
		//mScrollBar.setBGImage(mScrollImage);
	}

	@Override
	public void stop() {
		mLineView.setBGImage(null);
		if(!mBGImage.getImage().isRecycled()){
			mBGImage.getImage().recycle();
		}
		if(!mScrollImage.getImage().isRecycled()){
			mScrollImage.getImage().recycle();
		}
	}

	public String getCurrentPath() {
		return mCurrentPath;
	}
	@Override
	public void setRect(int w, int h) {
		super.setRect(w, h);
		mLineView.setRect(w, h);
	}

	public void setCurrentFontSize(int fontSize){
		mCurrentFontSize = fontSize;
		mLineView.setTextSize(fontSize);
	}

	public void setCharset(String charset) {
		if(charset == null|| charset.equals("")){
			mCurrentCharset = "utf8";
		}else{
			mCurrentCharset = charset;
		}
	}

	public LineView getLineView() {
		return mLineView;
	}

	public void restart() {
		if(mCurrentPath != null && !mCurrentPath.equals("")){
			readFile(new File(mCurrentPath));
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
		mCurrentPath = file.getAbsolutePath();
		KyoroSetting.setCurrentFile(file.getAbsolutePath());
		mBuffer = new ColorFilteredBuffer(20*BigLineData.FILE_LIME, mCurrentFontSize, mBufferWidth, file, mCurrentCharset);
		CyclingListInter<LineViewData> prevBuffer = mLineView.getCyclingList();
		mLineView.setCyclingList(mBuffer);
		prevBuffer.clear();
		if(mBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) mBuffer).startReadFile();
		}
		if(prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).dispose();
		}
	}

	private class LayoutManager extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			int x = graphics.getWidth() - mCircleController.getWidth() / 2;
			int y = graphics.getHeight() - mCircleController.getHeight() / 2;
			TextViewer.this.mCircleController.setPoint(x, y);
			setTextViewSize(graphics);
			updateCircleControllerSize(graphics);
			paintScroll(graphics);
		}

		private void setTextViewSize(SimpleGraphics graphics) {
			int textSize = TextViewer.this.mCurrentFontSize;
			int width = TextViewer.this.mLineView.getWidth();//SimpleDisplayObject.getStage(TextViewer.this).getWidth();//graphics.getWidth();
			int w = TextViewer.this.mBufferWidth + mMergine*2;
			TextViewer.this.mLineView.setTextSize(textSize*width/w);
		}

		private void updateCircleControllerSize(SimpleGraphics graphics) {
			int min = graphics.getWidth();
			if(min>graphics.getHeight()){
				min = graphics.getHeight();
			}
			min = min/2;
			if(min<mCircleController.getWidth()){
				mCircleController.setRadius(min/2);
			}
		}

		public void paintScroll(SimpleGraphics graphics) {
			CyclingListInter<?> viewerBuffer = TextViewer.this.mLineView.getCyclingList();
			int bufferSize = viewerBuffer.getNumberOfStockedElement(); 
			int beginPosition = TextViewer.this.mLineView.getShowingTextStartPosition();
			int endPosition = TextViewer.this.mLineView.getShowingTextEndPosition();
			mScrollBar.setStatus(beginPosition, endPosition, bufferSize);
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

	private static class ColorFilteredBuffer extends TextViewerBuffer {
		public ColorFilteredBuffer(int listSize, int textSize, int screenWidth,
				File path, String charset) {
			super(listSize, textSize, screenWidth, path, charset);
		}

		@Override
		public synchronized void head(LineViewData element) {
			super.head(element);
			element.setColor(COLOR_FONT2);
		}
		@Override
		public synchronized void add(LineViewData element) {
			super.add(element);
			element.setColor(COLOR_FONT1);
		}
	}

	private CyclingList<LineViewData> getStartupMessageBuffer() {
		String[] message = {
				"Please open file\n",
				"Current charset is "+mCurrentCharset+"\n", 
				"..\n",
				"Sorry, this application is pre-alpha version",
				"Testing and Developing.. now",
				"Please mail kyorohiro.android@gmail.com, ", 
				"If you have particular questions or comments, ",
				"please don't hesitate to contact me. Thank you. \n"
		};
		int color[] = {
				Color.CYAN,
				Color.YELLOW,
				Color.RED,
				Color.RED,
				Color.RED,
				Color.RED,
				Color.RED,
				Color.RED,				
		};
		CyclingList<LineViewData> startupMessage = new CyclingList<LineViewData>(100);
		for(int i=0;i<message.length;i++){
			String m = message[i];
			int crlf = LineViewData.INCLUDE_END_OF_LINE;
			if(!m.endsWith("\n")){
				crlf = LineViewData.EXCLUDE_END_OF_LINE;
			}
			startupMessage.add(new LineViewData(m, color[i], crlf));
		}
		return startupMessage;
	}

}
