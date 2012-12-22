package info.kyorohiro.helloworld.ext.textviewer.manager;

import java.io.File;

import info.kyorohiro.helloworld.display.simple.SimpleFont;

public abstract class TextViewBuilder {
	public abstract SimpleFont newSimpleFont();
	public abstract void copyStart();
	public abstract void pastStart();
	public abstract File getFilesDir();
	public abstract boolean currentBrIsLF();
	public abstract String getCurrentCharset();
	public abstract void setCurrentFile(String path);
}
