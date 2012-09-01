package info.kyorohiro.helloworld.textviewer.viewer;

import java.io.File;
import java.io.FileNotFoundException;
import android.graphics.Color;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.widget.lineview.CursorableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewData;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndMoveActionForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndZoomForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.ScrollBar;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.io.MyBreakText;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;

public class TextViewer extends SimpleDisplayObjectContainer {
	public static int COLOR_BG = Color.parseColor("#FFFFFFBB");
	public static int COLOR_FONT1 = Color.parseColor("#ff1188dd");
	public static int COLOR_FONT2 = Color.parseColor("#ffdd1188");

	private String mCurrentCharset = "utf8";
	private LineViewBufferSpec mBuffer = null;
	private LineView mLineView = null;
	private int mBufferWidth = 0;
	private ScrollBar mScrollBar = null;
	private int mCurrentFontSize = KyoroSetting.CURRENT_FONT_SIZE_DEFAULT;
	private String mCurrentPath = "";
	private int mMergine = 0;
	private MyBreakText mBreakText = new MyBreakText();

	public TextViewer(int textSize, int width, int mergine) {
		mCurrentFontSize = textSize;
		mCurrentCharset = KyoroSetting.getCurrentCharset();
		mBuffer = StartupMessageBuffer.getStartupMessageBuffer();
		mBufferWidth = width - mergine * 2;
		mMergine = mergine;

		mLineView = new CursorableLineView(mBuffer, textSize, 200);
		mLineView.isTail(false);
		mLineView.setBgColor(COLOR_BG);
		setRect(width, width/2);
		mScrollBar = new ScrollBar(mLineView);
		addChild(new TouchAndMoveActionForLineView(mLineView));
		addChild(new TouchAndZoomForLineView(mLineView));
		addChild(mLineView);
		addChild(new LayoutManager());
		addChild(mScrollBar);
	}

	public String getCurrentPath() {
		return mCurrentPath;
	}

	@Override
	public void setRect(int w, int h) {
		super.setRect(w, h);
		mLineView.setRect(w, h);
	}

	public void setCurrentFontSize(int fontSize) {
		mCurrentFontSize = fontSize;
		mLineView.setTextSize(fontSize);
	}

	public void setCharset(String charset) {
		if (charset == null || charset.equals("")) {
			mCurrentCharset = "utf8";
		} else {
			mCurrentCharset = charset;
		}
	}

	public LineView getLineView() {
		return mLineView;
	}

	public void restart() {
		if (mCurrentPath != null && !mCurrentPath.equals("")) {
			readFile(new File(mCurrentPath));
		}
	}

	public boolean readFile(File file) {
		if (file == null) {
			KyoroApplication.showMessage("file can not read null file");
			return false;
		}
		if (!file.canRead() || !file.exists() || !file.isFile()) {
			KyoroApplication.showMessage("file can not read " + file.toString());
			return false;
		}
		mCurrentPath = file.getAbsolutePath();
		KyoroSetting.setCurrentFile(file.getAbsolutePath());
		try {
			mBreakText.setTextSize(mCurrentFontSize);
			mBreakText.setBufferWidth(mBufferWidth);
			mBuffer = new TextViewerBufferWithColorFilter(3000, mBreakText, file, mCurrentCharset);
		} catch (FileNotFoundException e) {
			// don't pass along this code
			KyoroApplication.showMessage("file can not read null filen --007--");
			return false;
		}
		LineViewBufferSpec prevBuffer = TextViewer.this.mLineView.getLineViewBuffer();
		mLineView.setLineViewBufferSpec(mBuffer);
		if (prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).clear();

		}
		if (mBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) mBuffer).startReadFile();
		}
		if (prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).dispose();
		}
		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
		LineViewBufferSpec prevBuffer = TextViewer.this.mLineView.getLineViewBuffer();
		if (prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).dispose();
		}
	}

	private class LayoutManager extends SimpleDisplayObject {
		@Override
		public void paint(SimpleGraphics graphics) {
			setTextViewSize(graphics);
			paintScroll(graphics);
			SimpleDisplayObject.getStage(this).setColor(COLOR_BG);
		}

		private void setTextViewSize(SimpleGraphics graphics) {
			int textSize = TextViewer.this.mCurrentFontSize;
			int width = TextViewer.this.mLineView.getWidth();// SimpleDisplayObject.getStage(TextViewer.this).getWidth();//graphics.getWidth();
			int w = TextViewer.this.mBufferWidth + mMergine * 2;
			TextViewer.this.mLineView.setTextSize(textSize * width / w);
		}


		public void paintScroll(SimpleGraphics graphics) {
			LineViewBufferSpec viewerBuffer = TextViewer.this.mLineView.getLineViewBuffer();
			int bufferSize = viewerBuffer.getNumberOfStockedElement();
			int beginPosition = TextViewer.this.mLineView.getShowingTextStartPosition();
			int endPosition = TextViewer.this.mLineView.getShowingTextEndPosition();
			mScrollBar.setStatus(beginPosition, endPosition, bufferSize);
			mScrollBar.setColor(COLOR_FONT1);
		}
	}

	private static class TextViewerBufferWithColorFilter extends TextViewerBuffer {
		public TextViewerBufferWithColorFilter(int listSize, BreakText breakText,
				File path, String charset) throws FileNotFoundException {
			super(listSize, breakText, path, charset);
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
}
