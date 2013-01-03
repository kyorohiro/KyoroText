package info.kyorohiro.helloworld.ext.textviewer.viewer;

import java.io.File;
import java.io.FileNotFoundException;

import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.simple.sample.MyBreakText;
import info.kyorohiro.helloworld.display.widget.editview.EditableLineView;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.ManagedLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.extraparts.ScrollBar;
import info.kyorohiro.helloworld.display.widget.lineview.extraparts.TouchAndMoveActionForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.extraparts.TouchAndZoomForLineView;
import info.kyorohiro.helloworld.text.KyoroString;
//import info.kyorohiro.helloworld.textviewer.KyoroSetting;

public class TextViewer extends SimpleDisplayObjectContainer {
	public static int COLOR_BG = SimpleGraphicUtil.parseColor("#FFE7DDAA");
	public static int COLOR_FONT1 = SimpleGraphicUtil.parseColor("#dd0044ff");
	public static int COLOR_FONT2 = SimpleGraphicUtil.parseColor("#ddff0044");

	private String mCurrentCharset = "utf8";
	private ManagedLineViewBuffer mBuffer = null;
	private EditableLineView mLineView = null;
	private int mBufferWidth = 0;
	private ScrollBar mScrollBar = null;
	private int mCurrentFontSize = 12;//KyoroSetting.CURRENT_FONT_SIZE_DEFAULT;
	private String mCurrentPath = "";
	private int mMergine = 0;
	private MyBreakText mBreakText = null;

	public TextViewer(LineViewBufferSpec buffer, int textSize, int width, int mergine, SimpleFont font, String charset) {
		mBreakText = new MyBreakText(font);
		init(buffer, textSize, width, mergine, charset);
	}

	private boolean mIsExtraUI = true;
	public boolean IsExtraUI() {
		return mIsExtraUI;
	}
	public void IsExtraUI(boolean on) {
		mIsExtraUI = on;
	}

	public void init(LineViewBufferSpec buffer, int textSize, int width, int mergine, String charset) {
		mCurrentFontSize = textSize;
		mCurrentCharset = charset;//KyoroSetting.getCurrentCharset();
		mBuffer = new ManagedLineViewBuffer(buffer);
		mBufferWidth = width - mergine * 2;
		mMergine = mergine;

		mLineView = new EditableLineView(mBuffer.getBase(), textSize, 200);
		mLineView.isTail(false);
		mLineView.setBgColor(COLOR_BG);
		setRect(width, width/2);
		mScrollBar = new ScrollBar(mLineView);
		addChild(new TouchAndMoveActionForLineView(mLineView));
		addChild(new TouchAndZoomForLineView(mLineView));
		addChild(mLineView);
		addChild(new LayoutManager());
		addChild(mScrollBar);
		//ytodo
		mBreakText.getSimpleFont().setAntiAlias(true);
		mBreakText.getSimpleFont().setFontSize(mCurrentFontSize);
	}

	// following code is refactring target
	public boolean isEdit() {
		return mLineView.isEdit();
	}

	private boolean mGuard = false;
	public void isGuard(boolean guard) {
		mGuard = guard;
	}

	public boolean isGuard() {
		return mGuard;
	}

	public File asisGetBufferPath() {
		if(mAsisTextBuffer != null) {
			return mAsisTextBuffer.getBigLineData().getPath();
		} else {
			return new File("");
		}
	}
	public void asisChangeBufferPath(File bufferPath) throws FileNotFoundException {
		if(mAsisTextBuffer != null) {
			mAsisTextBuffer.getBigLineData().asisChangePath(bufferPath);
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

	public String getCharset() {
		return mCurrentCharset;
	}

	public EditableLineView getLineView() {
		return mLineView;
	}

	public ManagedLineViewBuffer getManagedLineViewBuffer() {
		return mBuffer;
	}

	public void restart() {
		if (mCurrentPath != null && !mCurrentPath.equals("")) {
			try {
				readFile(new File(mCurrentPath), true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	private TextViewerBuffer mAsisTextBuffer = null;
	public boolean readFile(File file, boolean updataCurrentPath) throws FileNotFoundException, NullPointerException {

		if (file == null) {
			throw new NullPointerException("kyoro text --1--");
		}
		if (!file.canRead() || !file.exists() || !file.isFile()) {
			throw new FileNotFoundException("kyoro text --2--");
		}
		mCurrentPath = file.getAbsolutePath();
		
		try {
			mBreakText.getSimpleFont().setFontSize(mCurrentFontSize);
			mBreakText.getSimpleFont().setAntiAlias(true);
			mBreakText.setBufferWidth(mBufferWidth);
			int cash2 = (int)(mBreakText.getWidth()*2/6);
			mBuffer = new ManagedLineViewBuffer(mAsisTextBuffer = new TextViewerBufferWithColorFilter(2000, cash2, mBreakText, file, mCurrentCharset));
		} catch (FileNotFoundException e) {
			FileNotFoundException fnfe = new FileNotFoundException("--3--");
			fnfe.setStackTrace(e.getStackTrace());
			throw fnfe;
		}
		LineViewBufferSpec prevBuffer = TextViewer.this.mLineView.getLineViewBuffer();
		mLineView.setLineViewBufferSpec(mBuffer);
		if (prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).clear();

		}
		if (mBuffer.getBase() instanceof TextViewerBuffer) {
			((TextViewerBuffer) mBuffer.getBase()).startReadFile();
		}
		if (prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).dispose();
		}
		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
		LineViewBufferSpec prevBuffer = getLineView().getLineViewBuffer();
		prevBuffer.dispose();
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
			int viewerTextSize = (textSize * width / w);
			if(viewerTextSize<textSize*3/4) {
				viewerTextSize = textSize*3/4;
			}
			TextViewer.this.mLineView.setTextSize(viewerTextSize);
			TextViewer.this.mLineView.setMergine(mMergine);
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
		public TextViewerBufferWithColorFilter(int listSize, int cash2, BreakText breakText,
				File path, String charset) throws FileNotFoundException {
			super(listSize, cash2,breakText, path, charset);
		}

		@Override
		public synchronized void head(KyoroString element) {
			super.head(element);
			element.setColor(COLOR_FONT2);
		}

		@Override
		public synchronized void add(KyoroString element) {
			super.add(element);
			element.setColor(COLOR_FONT1);
		}
	}
}
