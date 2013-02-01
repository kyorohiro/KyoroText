package info.kyorohiro.helloworld.ext.textviewer.viewer;

import info.kyorohiro.helloworld.display.simple.SimpleFont;
import info.kyorohiro.helloworld.display.simple.sample.MyBreakText;
import info.kyorohiro.helloworld.io.VirtualFile;

import java.io.File;
import java.io.FileNotFoundException;

public class BufferBuilder {

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
		if (file == null) {
			throw new NullPointerException("kyoro text --1--");
		}
		if (!file.getBase().canRead() || !file.getBase().exists() || !file.getBase().isFile()) {
			throw new FileNotFoundException("kyoro text --2--"+file.getBase().getAbsolutePath());
		}
		try {
			MyBreakText mBreakText = new MyBreakText(font);
			mBreakText.getSimpleFont().setFontSize(fontSize);
			mBreakText.getSimpleFont().setAntiAlias(true);
			mBreakText.setBufferWidth(width);
			int cash2 = (int)(mBreakText.getWidth()*2/6);
			mBuffer = new TextViewerBuffer(2000, cash2, mBreakText, file, charset);
		} catch (FileNotFoundException e) {
			FileNotFoundException fnfe = new FileNotFoundException("--3--");
			fnfe.setStackTrace(e.getStackTrace());
			throw fnfe;
		} finally {
		}

		return mBuffer;
	}

}
