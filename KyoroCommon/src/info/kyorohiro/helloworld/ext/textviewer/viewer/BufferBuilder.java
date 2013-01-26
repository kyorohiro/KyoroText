package info.kyorohiro.helloworld.ext.textviewer.viewer;

//import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.CrossCuttingProperty;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.simple.sample.MyBreakText;
//import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.io.VirtualFile;
import info.kyorohiro.helloworld.text.KyoroString;

import java.io.File;
import java.io.FileNotFoundException;

public class BufferBuilder {
	public static int COLOR_FONT1 = SimpleGraphicUtil.parseColor("#dd0044ff");
	public static int COLOR_FONT2 = SimpleGraphicUtil.parseColor("#ddff0044");
//	private SimpleApplication mApplication = null;
	private VirtualFile mFile = null;
	private String mCharset = "utf8";

	public BufferBuilder(File file) {
		mFile = new VirtualFile(file, 0);
	}

	public BufferBuilder(VirtualFile file) {
		mFile = file;
	}

	public BufferBuilder setFile(VirtualFile file) {
		mFile = file;
		return this;
	}


	public BufferBuilder setCharset(String charset) {
		mCharset = charset;
		return this;
	}

	public TextViewerBuffer readFile(SimpleFont font, int fontSize, int width) throws FileNotFoundException, NullPointerException {
		TextViewerBuffer mBuffer = null;
		VirtualFile file = mFile;
		String charset = mCharset;
//		android.util.Log.v("kiyo","BRFrf: start 00001-1-");
		if (file == null) {
			throw new NullPointerException("kyoro text --1--");
		}
		if (!file.getBase().canRead() || !file.getBase().exists() || !file.getBase().isFile()) {
			throw new FileNotFoundException("kyoro text --2--"+file.getBase().getAbsolutePath());
		}
//		android.util.Log.v("kiyo","BRFrf: start 00001-2-");		
		try {
			MyBreakText mBreakText = new MyBreakText(font);
//			android.util.Log.v("kiyo","BRFrf: start 00001-3-");
			mBreakText.getSimpleFont().setFontSize(fontSize);
			mBreakText.getSimpleFont().setAntiAlias(true);
			mBreakText.setBufferWidth(width);
//			android.util.Log.v("kiyo","BRFrf: start 00001-4-");
			int cash2 = (int)(mBreakText.getWidth()*2/6);
			mBuffer = new TextViewerBufferWithColorFilter(2000, cash2, mBreakText, file, charset);
//			android.util.Log.v("kiyo","BRFrf: start 00001-5-");
		} catch (FileNotFoundException e) {
			FileNotFoundException fnfe = new FileNotFoundException("--3--");
			fnfe.setStackTrace(e.getStackTrace());
			throw fnfe;
		} finally {
//			android.util.Log.v("kiyo","BRFrf: start 00001-6-");
		}

		return mBuffer;
	}
	private static class TextViewerBufferWithColorFilter extends TextViewerBuffer {
		public TextViewerBufferWithColorFilter(int listSize, int cash2, BreakText breakText,
				VirtualFile path, String charset) throws FileNotFoundException {
			super(listSize, cash2,breakText, path, charset);
		}

		@Override
		public synchronized void head(KyoroString element) {
			super.head(element);
			{
				CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
				int c = cp.getProperty(TextViewer.KEY_TEXTVIEWER_FONT_COLOR2, COLOR_FONT2);
				element.setColor(c);
			}
		}

		@Override
		public synchronized void add(KyoroString element) {
			super.add(element);
			{
				CrossCuttingProperty cp = CrossCuttingProperty.getInstance();
				int c = cp.getProperty(TextViewer.KEY_TEXTVIEWER_FONT_COLOR1, COLOR_FONT1);
				element.setColor(c);
			}
		}
	}
}
