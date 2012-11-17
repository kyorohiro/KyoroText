package info.kyorohiro.helloworld.textviewer.viewer;

import java.io.File;
import java.io.FileNotFoundException;

import android.graphics.Color;
import android.view.MotionEvent;
import info.kyorohiro.helloworld.android.adapter.MyBreakText;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObject;
import info.kyorohiro.helloworld.display.simple.SimpleDisplayObjectContainer;
import info.kyorohiro.helloworld.display.simple.SimpleGraphics;
import info.kyorohiro.helloworld.display.simple.SimpleTypeface;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.display.widget.lineview.LineView;
import info.kyorohiro.helloworld.display.widget.lineview.ManagedLineViewBuffer;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndMoveActionForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.TouchAndZoomForLineView;
import info.kyorohiro.helloworld.display.widget.lineview.ScrollBar;
import info.kyorohiro.helloworld.display.widget.lineview.edit.EditableLineView;
import info.kyorohiro.helloworld.io.BreakText;
import info.kyorohiro.helloworld.text.KyoroString;
import info.kyorohiro.helloworld.textviewer.KyoroApplication;
import info.kyorohiro.helloworld.textviewer.KyoroSetting;

public class TextViewer extends SimpleDisplayObjectContainer {
//	public static int COLOR_BG = Color.parseColor("#FFFFFFBB");
//	public static int COLOR_FONT1 = Color.parseColor("#ff1188dd");
//	public static int COLOR_FONT2 = Color.parseColor("#ffdd1188");
	public static int COLOR_BG = Color.parseColor("#FFE7DDAA");
	public static int COLOR_FONT1 = Color.parseColor("#dd0044ff");
	public static int COLOR_FONT2 = Color.parseColor("#ddff0044");

	private String mCurrentCharset = "utf8";
	private ManagedLineViewBuffer mBuffer = null;
	private EditableLineView mLineView = null;
	private int mBufferWidth = 0;
	private ScrollBar mScrollBar = null;
	private int mCurrentFontSize = KyoroSetting.CURRENT_FONT_SIZE_DEFAULT;
	private String mCurrentPath = "";
	private int mMergine = 0;
	private MyBreakText mBreakText = new MyBreakText();

	public TextViewer(LineViewBufferSpec buffer, int textSize, int width, int mergine) {
		init(buffer, textSize, width, mergine);
	}

	public void init(LineViewBufferSpec buffer, int textSize, int width, int mergine) {
		mCurrentFontSize = textSize;
		mCurrentCharset = KyoroSetting.getCurrentCharset();
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
			readFile(new File(mCurrentPath));
		}
	}

	public boolean readFile(File file) {
		return readFile(file, true);
	}

	public boolean readFile(File file, boolean updataCurrentPath) {

		if (file == null) {
			KyoroApplication.showMessage("file can not read null file");
			return false;
		}
		if (!file.canRead() || !file.exists() || !file.isFile()) {
			KyoroApplication.showMessage("file can not read " + file.toString());
			return false;
		}
		mCurrentPath = file.getAbsolutePath();
		
		// todo following code dependent application layer.
		// refactring target
		if(updataCurrentPath){
			File datadata = KyoroApplication.getKyoroApplication().getFilesDir();
			File parent = file.getParentFile();
			File grandpa = parent.getParentFile();
			if(!datadata.equals(parent)
					&&!(grandpa!=null&&grandpa.equals(datadata))){
				KyoroSetting.setCurrentFile(file.getAbsolutePath());
			}
		}
		try {
			mBreakText.getSimpleFont().setFontSize(mCurrentFontSize);
			mBreakText.getSimpleFont().setAntiAlias(true);
			mBreakText.setBufferWidth(mBufferWidth);
			mBuffer = new ManagedLineViewBuffer(new TextViewerBufferWithColorFilter(3000, mBreakText, file, mCurrentCharset));
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
		public TextViewerBufferWithColorFilter(int listSize, BreakText breakText,
				File path, String charset) throws FileNotFoundException {
			super(listSize, breakText, path, charset);
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
