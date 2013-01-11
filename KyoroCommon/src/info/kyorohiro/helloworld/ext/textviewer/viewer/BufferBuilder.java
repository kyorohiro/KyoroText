package info.kyorohiro.helloworld.ext.textviewer.viewer;

import info.kyorohiro.helloworld.display.simple.SimpleApplication;
import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.SimpleGraphicUtil;
import info.kyorohiro.helloworld.display.simple.sample.BreakText;
import info.kyorohiro.helloworld.display.simple.sample.MyBreakText;
import info.kyorohiro.helloworld.display.widget.lineview.LineViewBufferSpec;
import info.kyorohiro.helloworld.text.KyoroString;

import java.io.File;
import java.io.FileNotFoundException;

public class BufferBuilder {
	public static int COLOR_FONT1 = SimpleGraphicUtil.parseColor("#dd0044ff");
	public static int COLOR_FONT2 = SimpleGraphicUtil.parseColor("#ddff0044");
	private SimpleApplication mApplication = null;
	private File mFile = new File("dummy");
	private String mCharset = "utf8";

	public BufferBuilder(File file) {
		mFile = file;
	}

	public BufferBuilder setFile(File file) {
		mFile = file;
		return this;
	}


	public BufferBuilder setCharset(String charset) {
		mCharset = charset;
		return this;
	}

	public LineViewBufferSpec readFile(SimpleFont font, int fontSize, int width) throws FileNotFoundException, NullPointerException {
		LineViewBufferSpec mBuffer = null;
		File file = mFile;
		String charset = mCharset;
		if (file == null) {
			throw new NullPointerException("kyoro text --1--");
		}
		if (!file.canRead() || !file.exists() || !file.isFile()) {
			throw new FileNotFoundException("kyoro text --2--");
		}
		
		try {
			MyBreakText mBreakText = new MyBreakText(font);
			mBreakText.getSimpleFont().setFontSize(fontSize);
			mBreakText.getSimpleFont().setAntiAlias(true);
			mBreakText.setBufferWidth(width);
			int cash2 = (int)(mBreakText.getWidth()*2/6);
			mBuffer = new TextViewerBufferWithColorFilter(2000, cash2, mBreakText, file, charset);
		} catch (FileNotFoundException e) {
			FileNotFoundException fnfe = new FileNotFoundException("--3--");
			fnfe.setStackTrace(e.getStackTrace());
			throw fnfe;
		}

		return mBuffer;
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
