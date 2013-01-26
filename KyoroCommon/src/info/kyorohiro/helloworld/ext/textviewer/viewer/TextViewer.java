package info.kyorohiro.helloworld.ext.textviewer.viewer;

import java.io.File;
import java.io.FileNotFoundException;

import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
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
	public static final String KEY_TEXTVIEWER_BGCOLOR = "TV_BGCOLOR";
	public static final String KEY_TEXTVIEWER_SCROLLBAR_COLOR = "TV_SCROLLBAR_COLOR";
	public static final String KEY_TEXTVIEWER_FONT_COLOR1 = "TV_FONT_COLOR1";
	public static final String KEY_TEXTVIEWER_FONT_COLOR2 = "TV_FONT_COLOR2";
	public static final String KEY_TEXTVIEWER_FONT_COLOR3 = "TV_FONT_COLOR3";

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
		{
		CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
		int c = cp.getProperty(KEY_TEXTVIEWER_BGCOLOR, COLOR_BG);
		mLineView.setBgColor(c);
		}
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
		return readFile(new VirtualFile(file,0));
	}

	public boolean readFile(VirtualFile file) throws FileNotFoundException, NullPointerException {	
//		android.util.Log.v("kiyo","RFrf: start 00001-1-");
		mCurrentPath = file.getBase().getAbsolutePath();
		BufferBuilder builder = new BufferBuilder(file);
		builder.setCharset(mCurrentCharset);
//		android.util.Log.v("kiyo","RFrf: start 00001-2-");	
		try {
//			android.util.Log.v("kiyo","RFrf: start 00001-3-");
			LineViewBufferSpec buffer = builder.readFile(mBreakText.getSimpleFont(), mCurrentFontSize, mBufferWidth);
//			android.util.Log.v("kiyo","RFrf: start 00001-4-");
			return updateBuffer(new ManagedLineViewBuffer(buffer));
		} catch (FileNotFoundException e) {
			FileNotFoundException fnfe = new FileNotFoundException("--3--");
			fnfe.setStackTrace(e.getStackTrace());
			throw fnfe;
		} finally {
//			android.util.Log.v("kiyo","RFrf: start 00001-5-");
		}
	}

	public boolean updateBuffer(ManagedLineViewBuffer buffer) {
		mBuffer = buffer;
//		android.util.Log.v("kiyo","uprf: start 00001-1-");
		LineViewBufferSpec prevBuffer = TextViewer.this.mLineView.getLineViewBuffer();
//		android.util.Log.v("kiyo","uprf: start 00001-2-");
		mLineView.setLineViewBufferSpec(mBuffer);
//		android.util.Log.v("kiyo","uprf: start 00001-3-");

		if (prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).clear();
		}
//		android.util.Log.v("kiyo","uprf: start 00001-4-");
		if (mBuffer.getBase() instanceof TextViewerBuffer) {
			((TextViewerBuffer) mBuffer.getBase()).startReadFile();
		}
//		android.util.Log.v("kiyo","uprf: start 00001-5-");
		if (prevBuffer instanceof TextViewerBuffer) {
			((TextViewerBuffer) prevBuffer).dispose();
		}
//		android.util.Log.v("kiyo","uprf: start 00001-6-");
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

			{
				CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
				int c = cp.getProperty(KEY_TEXTVIEWER_BGCOLOR, COLOR_BG);
				SimpleDisplayObject.getStage(this).setColor(c);
			}
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
			{
				CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
				int c = cp.getProperty(KEY_TEXTVIEWER_SCROLLBAR_COLOR, COLOR_FONT1);
				mScrollBar.setColor(c);
			}
		}
	}
	
	
}
