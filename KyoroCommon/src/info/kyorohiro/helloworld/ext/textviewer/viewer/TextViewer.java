package info.kyorohiro.helloworld.ext.textviewer.viewer;

import java.io.File;
import java.io.FileNotFoundException;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
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
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferList;
import info.kyorohiro.helloworld.ext.textviewer.manager.BufferManager;
import info.kyorohiro.helloworld.io.VirtualFile;
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
	private BreakText mBreakText = null;
	private float mMininumScale = 0.75f;//3/4;

	public TextViewer(TextViewerBuffer buffer, int textSize, int width, int mergine, String charset) {
		mBreakText = buffer.getBreakText();
		buffer.getBreakText().getSimpleFont();
		init(buffer, textSize, width, mergine, charset);
	}

	private boolean mIsExtraUI = true;
	public boolean IsExtraUI() {
		return mIsExtraUI;
	}
	public void IsExtraUI(boolean on) {
		mIsExtraUI = on;
	}

	public void init(TextViewerBuffer buffer, int textSize, int width, int mergine, String charset) {
		mCurrentFontSize = textSize;
		mCurrentCharset = charset;
		mMergine = mergine;
		mBuffer = new ManagedLineViewBuffer(buffer);
		mBufferWidth = width - mergine * 2;

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

	public TextViewerBuffer getTextViewerBuffer() {
		if(mBuffer != null && mBuffer.getBase() != null){
			TextViewerBuffer buffer = (TextViewerBuffer)mBuffer.getBase();
			return buffer;
		} else {
			return null;
		}		
	}
	public File asisGetBufferPath() {
		if(mBuffer != null && mBuffer.getBase() != null){
			TextViewerBuffer buffer = (TextViewerBuffer)mBuffer.getBase();
			return buffer.getBigLineData().getPath();
		} else {
			return new File("");
		}
	}
	public void asisChangeBufferPath(File bufferPath) throws FileNotFoundException {
		if(mBuffer != null && mBuffer.getBase() != null){
			TextViewerBuffer buffer = (TextViewerBuffer)mBuffer.getBase();
			buffer.getBigLineData().asisChangePath(new VirtualFile(bufferPath,0));
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

	public void asisSetBufferWidth(int bufferWidth) {
		mBreakText.setWidth(bufferWidth);
		mBufferWidth = bufferWidth;
	}

	public void setMininumScale(float scale) {
		mMininumScale = scale;
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

	public boolean restart() {
		try {		
			if(getCurrentPath() != null) {
				return readFile(new File(getCurrentPath()));
			} 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean readFile(File file) throws FileNotFoundException, NullPointerException {
		return readFile(new VirtualFile(file,500));
	}

	public boolean readFile(VirtualFile file) throws FileNotFoundException, NullPointerException {	
		mCurrentPath = file.getBase().getAbsolutePath();
		BufferBuilder builder = new BufferBuilder(file);
		builder.setCharset(mCurrentCharset);
	
		try {
			LineViewBufferSpec buffer = builder.readFile(mBreakText.getSimpleFont(), mCurrentFontSize, mBufferWidth);
			return updateBuffer(new ManagedLineViewBuffer(buffer));
		} catch (FileNotFoundException e) {
			FileNotFoundException fnfe = new FileNotFoundException("--3--");
			fnfe.setStackTrace(e.getStackTrace());
			throw fnfe;
		}
	}

	public boolean updateBuffer(ManagedLineViewBuffer buffer) {
		mBuffer = buffer;
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
			if(viewerTextSize<(int)(textSize*mMininumScale)) {
				viewerTextSize = (int)(textSize*mMininumScale);
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
	
	
}
